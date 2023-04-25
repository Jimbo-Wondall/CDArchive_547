import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Network
{
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ClientThread client = null;
    private String serverName = "localhost";
    private int serverPort = 4444;
    private MainFormData parent;

    public Network(MainFormData _parent)
    {
        parent = _parent;
        serverName = "localhost";
        serverPort = 4444;
        connect(serverName, serverPort);
    }

    public void connect(String serverName, int serverPort)
    {
        println("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            println("Connected: " + socket);
            open();
        }
        catch (UnknownHostException uhe)
        {
            println("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ioe)
        {
            println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void send(String... message)
    {
        if (parent != null){
            parent.processLogAdd(message);
        }
        String formattedMessage = String.join("|", message);
        try
        {
            streamOut.writeUTF(formattedMessage);
            streamOut.flush();
        }
        catch (IOException ioe)
        {
            println("Sending error: " + ioe.getMessage());
            close();
        }
    }

    public void handle(String msg)
    {
        if (msg.equals(".bye"))
        {
            println("Good bye. Press EXIT button to exit ...");
            close();
        }
        else
        {
            System.out.println("Handle: " + msg);
            String[] message = msg.split("\\|");
            if (message[2].equals("Item Retrieved") || message[2].equals("Item Returned")) {
                parent.processAction(Integer.parseInt(message[4]), message[2]);
            }
            println(String.join(" - ", message));
        }
    }

    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            client = new ClientThread(this, socket);
        }
        catch (IOException ioe)
        {
            println("Error opening output stream: " + ioe);
        }
    }

    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException ioe)
        {
            println("Error closing ...");
        }
        client.close();
        client.interrupt();
        //client.stop();
    }

    void println(String msg)
    {
        // display.appendText(msg + "\n");
        // lblMessage.setText(msg);
        if (parent != null) {
            parent.processLogAdd(msg);
        }
    }
}
