package Utils.JStyler;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import Utils.ComponentMover;
import Utils.ComponentResizer;
import net.miginfocom.swing.MigLayout;

public class JStyler {

    LinkedList<JPanel> panelThemes = new LinkedList<>();
    LinkedList<JButton> buttonThemes = new LinkedList<>();
    LinkedList<JTable> tableThemes = new LinkedList<>();

    Color defaultColor = new Color(5);

    LinkedList<Color> colors;
    LinkedList<Border> borders;
    LinkedList<Font> fonts;
    Color primaryColor, secondaryColor, accentColor;

    JPanel pnl1Theme, pnl2Theme, pnl3Theme, pnlTitleTheme;
    JButton btnTheme, btnTitleTheme;
    Icon icoMinIcon, icoMaxIcon, icoCloseIcon;

    Color labelColor, panelColor1, panelColor2, panel3BorderColor, panelColor3, buttonBGColor, buttonHoverColor, buttonTextColor, textAreaColor, titleBarColor;
    Font buttonFont, labelFont;
    Border buttonBorder;
    boolean useDefaultTitleBar;

    public JStyler(String path){
        if (path.equals("Default")){
            LoadDefaultTheme();
        }
        else{
            LoadTheme(path);
        }
    }

    public void LoadTheme(String path){
        try{
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            LinkedList<String> temp = new LinkedList<String>();
            String st;
            String currentlyReading = null;
            while ((st = br.readLine()) != null){
                if (st.charAt(0) == '#'){
                    if (currentlyReading != null){
                        createTheme(currentlyReading, temp);
                    }
                    temp = new LinkedList<>();
                    currentlyReading = st;
                }
                else if (!st.isBlank() && !st.startsWith("//")){
                    temp.add(st);
                }

            }
        }
        catch (Exception e){
            System.out.println("broken file");
        }

        titleBarColor = new Color(255,255,255);
        panelColor2 = new Color(255,255,255);
    }

    private void createTheme(String themeType, LinkedList<String> parameters){
        LinkedList<String> temp = new LinkedList<>();
        String componentName = "";
        if (themeType.startsWith("# Button")){
            componentName = themeType.subSequence(8, themeType.length()).toString();
            JButton button = new JButton();
            for (String param : parameters) {
                if (param.startsWith("Foreground Colour=")){
                    String value = param.replace("Foreground Colour=", "");
                    if (!value.isBlank() && value.startsWith("@")){
                        int index = Integer.parseInt(value.replace("@", "").trim());
                        button.setForeground(colors.get(index));
                    }
                    else{
                        button.setForeground(defaultColor);
                    }
                }
                if (param.replace("Foreground Colour=", "").length() != param.length()) {

                }
            }

            temp.add("Foreground Colour=");
            temp.add("Background Colour=");
            temp.add("Border=");
            temp.add("Font=");
        }
        else if (themeType.startsWith("# Panel")){

        }
    }

    public void LoadDefaultTheme() {
        //Path filePath = Paths.get("Utils/JStyler/default_theme.txt");
        //LoadTheme(filePath.toAbsolutePath().toString());

        buttonBorder = BorderFactory.createLineBorder(new Color(63, 66, 73), 2);
        panel3BorderColor = new Color(63, 66, 73);
        buttonBGColor = new Color(49, 52, 59);
        panelColor1 = new Color(34,40,44);
        panelColor2 = new Color(21,25,28);
        panelColor3 = new Color(21,25,28);
        titleBarColor = new Color(21,25,28);
        buttonHoverColor = new Color(54,81,207);
        buttonTextColor = new Color(200,210,230);
        textAreaColor = new Color(213, 217, 225);
        labelColor = new Color(200,210,230);
        buttonFont = new Font("Calibri", Font.PLAIN, 11);
        labelFont = new Font("Calibri", Font.BOLD, 16);
        useDefaultTitleBar = false;

        JTable table = new JTable();
        table.isForegroundSet();
        table.setShowHorizontalLines(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setSelectionForeground(Color.white);
        table.setSelectionBackground(Color.red);
        tableThemes.add(table);

        //colors.add(new Color(34,40,44));
        //colors.add(new Color(21,25,28));
        //colors.add(new Color(63, 66, 73));
        //borders.add(BorderFactory.createLineBorder(colors.get(2)));

        //JPanel panel = new JPanel();
        //panel.setBackground(colors.get(0));
        //panelThemes.add(panel);

        //panel = new JPanel();
        //panel.setBackground(colors.get(1));
        //panelThemes.add(panel);

        //panel = new JPanel();
        //panel.setBackground(colors.get(1));
        //panelThemes.add(panel);
    }

    public void NewTitleBar(JFrame frame){
        if (useDefaultTitleBar){
            frame.setUndecorated(false);
            frame.setLayout(new MigLayout("wrap, insets 0, fill", "", ""));
        }
        else{
            frame.setUndecorated(true);
            frame.setLayout(new MigLayout("wrap, insets 0, fill", "[]", "[30:30:30]0[]"));

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panel.setBackground(titleBarColor);
            panel.setMaximumSize(new Dimension(10000, 30));

            ComponentResizer cr = new ComponentResizer();
            cr.registerComponent(frame);
            cr.setMinimumSize(new Dimension(500, 520));
            ComponentMover cm = new ComponentMover(frame, panel);
            cm.setEdgeInsets(new Insets(-10000, -10000, -10000, -10000));

            //JButton btnMinimise = TitleButton(1);
            JButton btnMinimise = NiceButton("Min");
            btnMinimise.addActionListener(e -> {
                frame.setState(JFrame.ICONIFIED);
            });
            panel.add(btnMinimise);

            //JButton btnMaximise = TitleButton(2);
            JButton btnMaximise = NiceButton("Max");
            btnMaximise.addActionListener(e -> {
                frame.setState(JFrame.MAXIMIZED_BOTH);
            });
            panel.add(btnMaximise);

            //JButton btnClose = TitleButton(3);
            JButton btnClose = NiceButton("Close");
            btnClose.addActionListener(e -> {
                System.exit(0);
            });
            panel.add(btnClose);

            frame.add(panel, "grow");
        }
    }
    private JButton TitleButton(int type){
        JButton button = switch (type) {
            case 1 -> new JButton(icoMinIcon);
            case 2 -> new JButton(icoMaxIcon);
            case 3 -> new JButton(icoCloseIcon);
            default -> new JButton("error");
        };
        button.setFont(btnTitleTheme.getFont());
        button.setUI(btnTitleTheme.getUI());
        button.setBorder(btnTitleTheme.getBorder());
        return button;
    }

    public JButton NiceButton(String text){
        JButton button = new JButton(text);
        button.setBackground(buttonBGColor);
        button.setUI(new BasicButtonUI());
        button.setBorder(buttonBorder);
        button.setFont(buttonFont);
        button.setForeground(buttonTextColor);
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(buttonHoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(buttonBGColor);
            }
        });
        return button;
    }

    public JPanel NicePanel(LayoutManager manager, int theme){
        JPanel sourcePanel;
        if (theme > panelThemes.size() || theme < 0){
            sourcePanel = new JPanel();
        }
        else{
            sourcePanel = panelThemes.get(theme);
        }
        JPanel panel = new JPanel(manager);
        panel.setForeground(sourcePanel.getForeground());
        panel.setBackground(sourcePanel.getBackground());
        panel.setBorder(sourcePanel.getBorder());
        panel.setFont(sourcePanel.getFont());
        return panel;
    }

    public JPanel NicePanel1(LayoutManager manager){
        JPanel panel = new JPanel(manager);
        panel.setBackground(panelColor1);
        return panel;
    }
    public JPanel NicePanel2(LayoutManager manager){
        JPanel panel = new JPanel(manager);
        panel.setBackground(panelColor2);
        return panel;
    }
    public JPanel NicePanel3(LayoutManager manager, String title){
        JPanel panel = new JPanel(manager);
        panel.setBackground(panelColor3);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(panel3BorderColor), title));
        return panel;
    }
    public JTextField NiceTextBox(){
        JTextField txtBox = new JTextField();
        txtBox.setBackground(textAreaColor);
        txtBox.setPreferredSize(new Dimension(0,0));
        return txtBox;
    }
    public JTextArea NiceTextArea(int rows, int cols){
        JTextArea txtArea = NiceTextArea();
        txtArea.setRows(rows);
        txtArea.setColumns(cols);
        return txtArea;
    }
    public JTextArea NiceTextArea(){
        JTextArea txtArea = new JTextArea();
        txtArea.setBackground(textAreaColor);
        //txtArea.setPreferredSize(new Dimension(0,0));
        return txtArea;
    }
    public JLabel NiceLabel(String text){
        JLabel label = new JLabel(text);
        label.setFont(labelFont);
        label.setForeground(labelColor);
        return label;
    }
    public JTable NiceTable(TableModel model){
        return NiceTable(model, 0);
    }
    public JTable NiceTable(TableModel model, int theme){
        JTable table = (JTable) cloneSwingComponent(tableThemes.get(theme));
        assert table != null;
        table.setModel(model);
        //table.setPreferredSize(new Dimension(30,200));
        //table.setAutoCreateRowSorter(true);

        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return table;
    }

    private Component cloneSwingComponent(Component inputComponent) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(inputComponent);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Component) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

