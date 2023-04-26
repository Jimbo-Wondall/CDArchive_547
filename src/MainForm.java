import Models.CDModel;
import Models.ITableObject;
import Utils.JStyler.JStyler;
import Utils.TableDriver;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainForm extends JFrame {

    // Title Bar
    JPanel pnlTitleBar;
    JStyler theme;

    // Root Panel
    JPanel pnlRoot;
    MainFormData Data;

    // Panel 1
    JPanel panel1;
    JTable tblCDArchiveList;
    JLabel lblPanel1Title, lblSearch, lblSort;
    JTextField txtTableSearch;
    JButton btnSortByTitle, getBtnSortByAuthor, getBtnSortByBarcode, btnSearch;
    JComboBox<TableDriver.SortType> cboSortType;

    // Panel 2
    JPanel panel2;
    JLabel lblID, lblTitle, lblAuthor, lblSection, lblXPos, lblYPos, lblBarCode, lblDescription;
    JTextField txtID, txtTitle, txtAuthor, txtSection, txtXPos, txtYPos, txtBarCode;
    JTextArea txtDescription;
    JButton btnNewItem, btnSaveUpdate;

    // Panel 3
    JPanel panel3;
    JLabel lblProcessLog, lblDisplayBT, lblHashMap;
    JTextArea txtProcessLog;
    JButton btnProcessLog, btnPreOrder, btnInOrder, btnPostOrder, btnGraphical,
            btnSave, btnDisplay;

    // Panel 4
    JPanel panel4;
    JButton btnRetrieve, btnRemove, btnReturn, btnAddToCollection,
            btnRandomCollectionSort, btnMostlySorted, btnReverseSort;
    JLabel lblPanel4Desc, lblSortSection;
    JTextField txtSortSection;

    public MainForm(){
        setSize(1200,650);
        setLocationRelativeTo(null);
        setLayout(new MigLayout("wrap, insets 0, fill", "", ""));

        theme = new JStyler("Default");
        Data = new MainFormData(this);
        InitGUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        setVisible(true);
    }

    void InitGUI(){
        getContentPane().removeAll();
        repaint();

        CreateTitleBar();
        CreateRootPanel();

    }

    void CreateTitleBar(){
        theme.NewTitleBar(this);
    }

    void CreateRootPanel(){
        pnlRoot = theme.NicePanel1(new MigLayout("wrap, fill", "10[]10[300, grow 0]10", "10[]10[]10"));
        add(pnlRoot, "grow");
        CreatePanel1();
        CreatePanel2();
        CreatePanel3();
        CreatePanel4();
    }

    void CreatePanel1(){
        panel1 = theme.NicePanel2(new MigLayout("wrap, fill", "[][][][][]", "[25][25][200][25]"));

        lblSearch = theme.NiceLabel("Search:");
        panel1.add(lblSearch, "grow");

        txtTableSearch = theme.NiceTextBox();
        txtTableSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                Data.filterTable(txtTableSearch.getText());
            }
        });
        panel1.add(txtTableSearch, "grow");

        btnSearch = theme.NiceButton("Search");
        btnSearch.addActionListener(e -> { });
        panel1.add(btnSearch, "grow, wrap");

        lblPanel1Title = theme.NiceLabel("Archive CDs");
        panel1.add(lblPanel1Title, "grow, span 5");

        tblCDArchiveList = theme.NiceTable(Data.tableDriver);

        tblCDArchiveList.getSelectionModel().addListSelectionListener(event -> {
                    int viewRow = tblCDArchiveList.getSelectedRow();
                    Data.selectedIndex = viewRow;
                    if (viewRow < 0) {
                        Data.displayBlankEntry();
                    } else {
                        int modelRow = tblCDArchiveList.convertRowIndexToModel(viewRow);
                        Data.displayEntry(modelRow);
                    }
                }
        );

        JScrollPane scroll = new JScrollPane(tblCDArchiveList);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel1.add(scroll, "grow, span 5");

        lblSort = theme.NiceLabel("Sort:");
        panel1.add(lblSort, "grow");

        btnSortByTitle  = theme.NiceButton("By Title");
        btnSortByTitle.addActionListener(e -> {
            Data.sortTableBy("Title", (TableDriver.SortType) cboSortType.getSelectedItem());
        });
        panel1.add(btnSortByTitle, "grow");

        getBtnSortByAuthor = theme.NiceButton("By Author");
        getBtnSortByAuthor.addActionListener(e -> {
            Data.sortTableBy("Author", (TableDriver.SortType) cboSortType.getSelectedItem());
        });
        panel1.add(getBtnSortByAuthor, "grow");

        getBtnSortByBarcode = theme.NiceButton("By Barcode");
        getBtnSortByBarcode.addActionListener(e -> {
            Data.sortTableBy("BarCode", (TableDriver.SortType) cboSortType.getSelectedItem());
        });
        panel1.add(getBtnSortByBarcode, "grow");


        cboSortType = new JComboBox<>();
        cboSortType.setModel(new DefaultComboBoxModel<>(TableDriver.SortType.values()));
        panel1.add(cboSortType, "grow");

        pnlRoot.add(panel1, "grow");
    }

    void CreatePanel2(){
        panel2 = theme.NicePanel2(new MigLayout("wrap, fill", "[grow 0][]", ""));

        panel2.add(lblID = theme.NiceLabel("ID:"), "grow");
        panel2.add(txtID = theme.NiceTextBox(), "grow");

        panel2.add(lblTitle = theme.NiceLabel("Title:"), "grow");
        panel2.add(txtTitle = theme.NiceTextBox(), "grow");

        panel2.add(lblAuthor = theme.NiceLabel("Author:"), "grow");
        panel2.add(txtAuthor = theme.NiceTextBox(), "grow");

        panel2.add(lblSection = theme.NiceLabel("Section"), "grow");
        panel2.add(txtSection = theme.NiceTextBox(), "grow");

        panel2.add(lblXPos = theme.NiceLabel("X Position:"), "grow");
        panel2.add(txtXPos = theme.NiceTextBox(), "grow");

        panel2.add(lblYPos = theme.NiceLabel("Y Position:"), "grow");
        panel2.add(txtYPos = theme.NiceTextBox(), "grow");

        panel2.add(lblBarCode = theme.NiceLabel("Bar Code:"), "grow");
        panel2.add(txtBarCode = theme.NiceTextBox(), "grow");

        panel2.add(lblDescription = theme.NiceLabel("Description:"), "grow");
        panel2.add(txtDescription = theme.NiceTextArea(), "grow");
        txtDescription.setLineWrap(true);

        btnNewItem = theme.NiceButton("New Item");
        btnNewItem.addActionListener(e -> {
            tblCDArchiveList.getSelectionModel().clearSelection();
            Data.selectedIndex = -1;
            Data.displayBlankEntry();
        });
        panel2.add(btnNewItem, "span, split, grow");

        btnSaveUpdate = theme.NiceButton("Save / Update");
        btnSaveUpdate.addActionListener(e -> {
            String[] fields = {
                    txtID.getText(),
                    txtTitle.getText(),
                    txtAuthor.getText(),
                    txtSection.getText(),
                    txtXPos.getText(),
                    txtYPos.getText(),
                    txtBarCode.getText(),
                    txtDescription.getText(),
                    "false"
            };
            CDModel data = new CDModel(fields);
            Data.tableDriver.process(data);
        });
        panel2.add(btnSaveUpdate, "grow");
        pnlRoot.add(panel2, "grow");

        Data.displayEntry(1);
    }

    void CreatePanel3(){
        panel3 = theme.NicePanel2(new MigLayout("wrap, fill", "[][][][][][]", "[25][200][25][25]"));

        lblProcessLog = theme.NiceLabel("Process Log:");
        panel3.add(lblProcessLog, "grow");

        btnProcessLog = theme.NiceButton("Process Log");
        btnProcessLog.addActionListener(e -> {});
        panel3.add(btnProcessLog, "grow, wrap");

        txtProcessLog = theme.NiceTextArea();
        JScrollPane processLogScroller = new JScrollPane(txtProcessLog);
        processLogScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        processLogScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel3.add(processLogScroller, "grow, span");

        btnPreOrder = theme.NiceButton("Pre-Order");
        btnPreOrder.addActionListener(e -> {});
        panel3.add(btnPreOrder, "grow");

        btnInOrder = theme.NiceButton("In-Order");
        btnInOrder.addActionListener(e -> {});
        panel3.add(btnInOrder, "grow");

        btnPostOrder = theme.NiceButton("Post-Order");
        btnPostOrder.addActionListener(e -> {});
        panel3.add(btnPostOrder, "grow");

        btnGraphical = theme.NiceButton("Graphical");
        btnGraphical.addActionListener(e -> {});
        panel3.add(btnGraphical, "grow, wrap");

        btnSave = theme.NiceButton("Save");
        btnSave.addActionListener(e -> {});
        panel3.add(btnSave, "grow");

        btnDisplay = theme.NiceButton("Display");
        btnDisplay.addActionListener(e -> {});
        panel3.add(btnDisplay, "grow");

        pnlRoot.add(panel3, "grow");
    }

    void CreatePanel4(){
        panel4 = theme.NicePanel2(new MigLayout("wrap, fill", "[][]", ""));

        lblPanel4Desc = theme.NiceLabel("Automation Action Request for the item above:");
        panel4.add(lblPanel4Desc, "grow, span, wrap");

        btnRetrieve = theme.NiceButton("Retrieve");
        btnRetrieve.addActionListener(e -> { Data.sendMessage("Retrieve Item"); });
        panel4.add(btnRetrieve, "grow, span, split");

        btnRemove = theme.NiceButton("Remove");
        btnRemove.addActionListener(e -> { Data.sendMessage("Remove Item"); });
        panel4.add(btnRemove, "grow, wrap");

        btnReturn = theme.NiceButton("Return");
        btnReturn.addActionListener(e -> { Data.sendMessage("Return Item"); });
        panel4.add(btnReturn, "grow, span, split");

        btnAddToCollection = theme.NiceButton("Add To Collection");
        btnAddToCollection.addActionListener(e -> { Data.sendMessage("New Item"); });
        panel4.add(btnAddToCollection, "grow, wrap");

        lblSortSection = theme.NiceLabel("Sort Section:");
        panel4.add(lblSortSection, "grow");

        txtSortSection = theme.NiceTextBox();
        panel4.add(txtSortSection, "grow");

        btnRandomCollectionSort = theme.NiceButton("Random Collection Sort");
        btnRandomCollectionSort.addActionListener(e -> { });
        panel4.add(btnRandomCollectionSort, "grow, span");

        btnMostlySorted = theme.NiceButton("Mostly Sorted Sort");
        btnMostlySorted.addActionListener(e -> { });
        panel4.add(btnMostlySorted, "grow, span");

        btnReverseSort = theme.NiceButton("Reverse Order Sort");
        btnReverseSort.addActionListener(e -> { });
        panel4.add(btnReverseSort, "grow, span");

        pnlRoot.add(panel4, "grow");
    }
}
