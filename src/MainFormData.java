import Models.CDModel;
import Utils.*;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class MainFormData {
    MainForm parent;
    Path currentFilePath;
    File currentFile;
    CDModel[] fileData;
    TableDriver tableDriver;
    TableRowSorter<TableDriver> tableSorter;
    int selectedIndex = -1;
    DList linkedMessageList;

    // do this
    Network connection;
    public MainFormData(MainForm _parent){
        parent = _parent;
        //currentFilePath = Paths.get("C:\\Users\\kingt\\OneDrive\\Desktop\\Semester 2 Assessment Files\\Java2\\ICTPRG547\\AT2\\Hand in\\Program Files\\CD_ArchivePrototype_SampleData.txt");
        currentFilePath = Paths.get("C:\\Users\\kingt\\OneDrive\\Desktop\\Semester 2 Assessment Files\\Java2\\ICTPRG547\\AT2\\Resources\\data.csv");
        LoadFile(new File(currentFilePath.toString()));
        linkedMessageList = new DList();

        connection = new Network(this);


    }

    public void displayBlankEntry() {
        displayEntry(-1);
    }
    public void displayEntry(int index) {
        //Object[] currentData;
        String[] currentData;
        CDModel obj = null;
        if (index < 0){
            currentData = new String[9];
            Arrays.fill(currentData, "");
        } else {
            //currentData = tableDriver.tableData.get(index).toStringArray();
            obj = (CDModel) tableDriver.tableData.get(index);
        }
        if (obj == null) {
            parent.txtID.setText("");
            parent.txtTitle.setText("");
            parent.txtAuthor.setText("");
            parent.txtSection.setText("");
            parent.txtXPos.setText("");
            parent.txtYPos.setText("");
            parent.txtBarCode.setText("");
            parent.txtDescription.setText("");
        } else {
            parent.txtID.setText(obj.getByIndex(0).toString());
            parent.txtTitle.setText((obj.getByIndex(1)).toString());
            parent.txtAuthor.setText((obj.getByIndex(2)).toString());
            parent.txtSection.setText((obj.getByIndex(3)).toString());
            parent.txtXPos.setText((obj.getByIndex(4)).toString());
            parent.txtYPos.setText((obj.getByIndex(5)).toString());
            parent.txtBarCode.setText((obj.getByIndex(6)).toString());
            parent.txtDescription.setText((obj.getByIndex(7)).toString());
        }

        //parent.txtID.setText((currentData[0]));
        //parent.txtTitle.setText((currentData[1]));
        //parent.txtAuthor.setText((currentData[2]));
        //parent.txtSection.setText((currentData[3]));
        //parent.txtXPos.setText((currentData[4]));
        //parent.txtYPos.setText((currentData[5]));
        //parent.txtBarCode.setText((currentData[6]));
        //parent.txtDescription.setText((currentData[7]));
    }

    public void filterTable(String input) {
        // tableSorter = new TableRowSorter<>(tableDriver);
        tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + input));
        parent.tblCDArchiveList.setRowSorter(tableSorter);
    }

    public void LoadFile(File file){
        try {
            currentFile = file;
            BufferedReader br = new BufferedReader(new FileReader(file));
            LinkedList<String> temp = new LinkedList<String>();
            String st;
            while ((st = br.readLine()) != null){
                if (tryParseInt(st.split(";")[0])) {
                    temp.add(st);
                }
            }
            fileData = Arrays.stream(temp.toArray(new String[0])).map(l -> new CDModel(l)).toArray(CDModel[]::new);
            br.close();
        }
        catch (Exception e){
            System.out.println("broken file");
        }
        LoadTable();
    }
    public boolean tryParseInt(String value) {
        try {
            int temp = Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void LoadTable(){
        try{
            String[] varTypes = new String[] {
                    "int",
                    "String",
                    "String",
                    "Char",
                    "int",
                    "int",
                    "int",
                    "String",
                    "boolean"
            };
            tableDriver = new TableDriver(fileData, varTypes);
            //tableDriver = new TableDriver(fileLines, varTypes);
            tableSorter = new TableRowSorter<>(tableDriver);
        }
        catch (Exception e){
            tableDriver = null;
            System.out.println("broken file");
        }
    }

    public void sortTableBy(String colName, TableDriver.SortType type) {
        tableDriver.sort(colName, type);
    }

    public void sendMessage(String input) {
        if (selectedIndex >= 0) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

            String returnTime = timeFormat.format(LocalDateTime.now().plusMinutes(5));
            String barcode = tableDriver.getValueAt(selectedIndex, tableDriver.findColumn("BarCode")).toString();
            connection.send(dateFormat.format(LocalDateTime.now()), timeFormat.format(LocalDateTime.now()), "SENT", input, barcode);
        } else {
            JOptionPane.showConfirmDialog(parent, "Please select an item from the list before proceeding", "Error", JOptionPane.DEFAULT_OPTION);
        }
    }

    public void processAction(int barcode, String action) {
        int index = findByBarcode(barcode);
        switch (action) {
            case "Item Retrieved" -> tableDriver.setValueAt(true, index, 8);
            case "Item Returned" -> tableDriver.setValueAt(false, index, 8);
        }
    }

    public int findByBarcode(int barcode) {
        for (int i = 0; i < tableDriver.tableData.size(); i++) {
            if (tableDriver.getValueAt(i, 6).equals(barcode)) {
                return i;
            }
        }
        return -1;
    }

    public void processLogAdd(String... message) {
        linkedMessageList.add(message);
        if (parent.txtProcessLog != null) {
            parent.txtProcessLog.setText(linkedMessageList.toString());
        }
    }
}