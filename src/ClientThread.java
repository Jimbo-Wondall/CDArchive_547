import java.net.*;
import java.io.*;

public class ClientThread extends Thread
{
    private Socket socket = null;
    private Network client = null;
    private DataInputStream streamIn = null;
    private boolean isInterrupted = false;

    public ClientThread(Network _client, Socket _socket)
    {
        client = _client;
        socket = _socket;
        open();
        start();
    }

    /**
     * POST: run
     */
    public void open()
    {
        try
        {
            streamIn = new DataInputStream(socket.getInputStream());
        }
        catch (IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);
            //client1.stop();
            client.close();
        }
    }

    public void close()
    {
        try
        {
            if (streamIn != null)
            {
                streamIn.close();
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    public void run()
    {
        while (!isInterrupted)
        {
            try
            {
                client.handle(streamIn.readUTF());
            }
            catch (IOException ioe)
            {
                System.out.println("Listening error: " + ioe.getMessage());
                //client.stop();
                client.close();
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        isInterrupted = true;
    }
}
