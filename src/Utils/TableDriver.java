package Utils;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.IntStream;

public class TableDriver extends AbstractTableModel {
    int sortComparisons;
    String[] colNames;
    String[] colTypes;
    public ArrayList<Object[]> tableData;
    ArrayList<Object[]> currentlySorting;
    int sortingCol;

    public TableDriver(String[] input, String[] varTypes) {
        this.colNames = input[0].split(";");
        this.colTypes = varTypes;
        tableData = new ArrayList<>();
        for (int i = 1; i < input.length; i++) {
            insert(input[i].split(";"));
        }
    }

    public void sort(String colName, SortType type){
        int col = findColumn(colName);
        if (col >= 0){
            sort(col, type);
        }
    }
    public void sort(int sortOnCol, SortType type) {
        sortComparisons = 0;
        sortingCol = sortOnCol;
        int maxRadixDigits = 10;
        if (colNames[sortOnCol].equals("BarCode")) {
            maxRadixDigits = 8;
        }
        currentlySorting = new ArrayList<>(tableData);
        switch (type) {
            case Bubble -> bubbleSort();
            case Quick -> quickSort();
            case Radix -> radixSort(maxRadixDigits);
            case Merge -> mergeSort();
            case Insertion -> insertionSort();
        }
        tableData = currentlySorting;
        System.out.println(sortComparisons);
        fireTableDataChanged();
    }
    public void process(String[] input) {
        if (input[0].equals("")) {
            add(input);
        } else {
            update(input);
        }
    }
    public void add(String[] input) {
        insert(input);
        fireTableDataChanged();
    }
    private void insert(String[] input) {
        Object[] item = new Object[colTypes.length];
        item[0] = tableData.size();
        for (int i = 1; i < input.length; i++) {
            item[i] = ConvertToValue(input[i], colTypes[i]);
        }
        tableData.add(item);
    }
    public void update(String[] input) {
        if (input.length != colTypes.length){
            System.out.println("data mismatch");
        }
        else{
            Object[] item = new Object[colTypes.length];
            for (int i = 0; i < input.length; i++) {
                item[i] = ConvertToValue(input[i], colTypes[i]);
            }
            tableData.set((int) item[0], item);
            fireTableDataChanged();
        }

    }
    private Object ConvertToValue(String input, String inputType){
        if (inputType.equals("String")) return input;
        if (inputType.equals("int")) return Integer.parseInt(input);
        if (inputType.equals("Char")) return input.charAt(0);
        if (inputType.equals("boolean")) return input.equals("true");
        return null;
    }

    @Override
    public int getRowCount() {
        return tableData.size();
    }
    @Override
    public int getColumnCount() {
        return colNames.length;
    }
    @Override
    public String getColumnName(int index){
        return colNames[index];
    }
    @Override
    public Class getColumnClass(int columnIndex){
        if (colTypes[columnIndex].equals("String")) return String.class;
        if (colTypes[columnIndex].equals("int")) return int.class;
        if (colTypes[columnIndex].equals("Char")) return char.class;
        if (colTypes[columnIndex].equals("boolean")) return Boolean.class;
        return String.class;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //tableData.get(rowIndex)[columnIndex] = aValue;

        Object[] item = tableData.get(rowIndex);
        item[columnIndex] = aValue;
        tableData.set(rowIndex, item);

        fireTableDataChanged();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return tableData.get(rowIndex)[columnIndex];
    }
    @Override
    public int findColumn(String columnName) {
        return Arrays.stream(colNames).toList().indexOf(columnName);
    }


    // Sorts
    private void bubbleSort() {
        int size = currentlySorting.size();
        for (int i = 0; i < currentlySorting.size() - 1; i++) {
            size--;
            boolean isSorted = true;
            for (int j = 0; j < size; j++) {
                if (compare(j, j+1)) {
                    swap(j, j+1);
                    isSorted = false;
                }
            }
            if (isSorted) break;
        }
    }
    private void quickSort() {
        Queue<int[]> unsortedArraySegments = new LinkedList<>();
        unsortedArraySegments.add(new int[]{
                0,
                currentlySorting.size() - 1
        });

        while (!unsortedArraySegments.isEmpty()) {
            // Pull the values out of the queue and put them into easy to work with variables
            int[] temp = unsortedArraySegments.remove();
            int startPoint = temp[0];
            int endPoint = temp[1];

            // Select the middle of the array segment to be the pivot point
            int pivotPoint = startPoint + ((endPoint - startPoint) / 2);

            // Swap the selected pivot with the last entry in the array
            swap(pivotPoint, endPoint);

            int firstLargerIndex = startPoint;
            for (int currentPos = startPoint; currentPos < endPoint; currentPos++) {
                if (!compare(currentlySorting.get(currentPos), currentlySorting.get(endPoint))) {
                    if (currentPos != firstLargerIndex) {
                        swap(currentPos, firstLargerIndex);
                    }
                    firstLargerIndex++;
                }
            }
            swap(endPoint, firstLargerIndex);

            if (firstLargerIndex - 1 - startPoint > 0) {
                unsortedArraySegments.add(new int[] {
                        startPoint,
                        firstLargerIndex - 1
                });
            }
            if (endPoint - (firstLargerIndex + 1) > 0) {
                unsortedArraySegments.add(new int[] {
                        firstLargerIndex + 1,
                        endPoint,
                });
            }
        }
    }
    private void radixSort(int maxDigits) {
        int base;
        if (colTypes[sortingCol].equals("int")) {
            base = 10;
        } else if (colTypes[sortingCol].equals("String")) {
            base = 36;
            return;
        } else {
            System.out.println("Radix sort does not support value type: " + colTypes[sortingCol]);
            return;
        }
        int[] sortedIndexes = IntStream.range(0, currentlySorting.size()).toArray();
        ArrayList<ArrayList<Integer>> buckets = new ArrayList<>();
        for (int j = 0; j < base; j++) {
            buckets.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < maxDigits; i++) {
            buckets.forEach(ArrayList::clear);
            for (int sortedIndex : sortedIndexes) {
                char s = '0';
                try {
                    String value = getValueAt(sortedIndex).toString();
                    s = value.charAt(value.length() - 1 - i);
                } catch (Exception ignored) {
                    System.out.println();
                }
                buckets.get(charToInt(s)).add(sortedIndex);
            }
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for (ArrayList<Integer> integers : buckets) {
                temp.addAll(integers);
            }
            sortedIndexes = temp.stream().mapToInt(val -> val).toArray();
        }
        indexesToObjects(sortedIndexes);
    }
    private void mergeSort() {
        ArrayList<Integer> sortedIndexes = merge(0, currentlySorting.size() - 1);;
        ArrayList<Object[]> returnVal = new ArrayList<>();
        for (int i : sortedIndexes) {
            returnVal.add(currentlySorting.get(i));
        }
        currentlySorting = returnVal;

    }
    private ArrayList<Integer> merge(int startPoint, int endPoint) {
        ArrayList<Integer> temp = new ArrayList<>();
        if (startPoint == endPoint) {
            temp.add(startPoint);
        } else {
            int midPoint = startPoint + ((endPoint - startPoint) / 2);
            Queue<Integer> list1 = new LinkedList<>(merge(startPoint, midPoint));
            Queue<Integer> list2 = new LinkedList<>(merge(midPoint + 1, endPoint));

            int num1 = list1.remove();
            int num2 = list2.remove();
            boolean finished = false;

            while(!finished) {
                if (compare(num1, num2)) {
                    temp.add(num2);
                    if (list2.isEmpty()) {
                        temp.add(num1);
                        temp.addAll(list1);
                        finished = true;
                    } else {
                        num2 = list2.remove();
                    }
                } else {
                    temp.add(num1);
                    if (list1.isEmpty()) {
                        temp.add(num2);
                        temp.addAll(list2);
                        finished = true;
                    } else {
                        num1 = list1.remove();
                    }
                }
            }
        }
        return temp;
    }
    private void insertionSort() {
        for (int i = 0; i < currentlySorting.size() - 1; i++) {
            int j = i;
            while (j >= 0 && compare(j, j+1)) {
                swap(j, j+1);
                j--;
            }
        }
    }

    private void vaguelyHeapSort() {
        int[] sortedIndexes = new int[currentlySorting.size()];
        Arrays.fill(sortedIndexes, -1);
        sortedIndexes[0] = 0;
        for (int i = 1; i < sortedIndexes.length; i++) {
            sortedIndexes[i] = i;
            int currentIndex = i;
            int rows = getRows(i);
            for (int j = 0; j < rows; j++) {
                boolean isRightChild = ((currentIndex & 1) == 0);
                int parent = isRightChild ? (currentIndex-2)/2 : (currentIndex-1)/2;
                int neighbour = isRightChild ? currentIndex -1: currentIndex +1;
                // if greater than parent, swap up
                if (compare(sortedIndexes[currentIndex], sortedIndexes[parent])) {
                    int temp = sortedIndexes[currentIndex];
                    sortedIndexes[currentIndex] = sortedIndexes[parent];
                    sortedIndexes[parent] = temp;
                } else {
                    // if there was no bubble up, commence bubble down
                    j = rows + 1;
                }
                // move children into correct places
                if (sortedIndexes[neighbour] != -1) {
                    if (!(compare(sortedIndexes[currentIndex], sortedIndexes[neighbour]) && isRightChild)) {
                        int temp = sortedIndexes[currentIndex];
                        sortedIndexes[currentIndex] = sortedIndexes[neighbour];
                        sortedIndexes[neighbour] = temp;
                    }
                }
                currentIndex = parent;
            }
        }
    }
    private int getRows(int index) {
        return (int)Math.ceil(Math.log(index + 1) / Math.log(2)) - 1;
    }
    private int parentOf(int index) {
        return ((index & 1) == 0) ? (index-2)/2 : (index-1)/2;
    }

    private void binaryTreeSort() {
        //HeapNode headNode = new HeapNode(0);
        //for (int i = 1; i < currentlySorting.size(); i++) {
        //    headNode.insert(i);
        //}
        //indexesToObjects(headNode.extract().stream().mapToInt(val -> val).toArray());

        //int[] sortedIndexes = new int[currentlySorting.size()];
        //Arrays.fill(sortedIndexes, -1);
        //sortedIndexes[0] = 0;
        //for (int i = 1; i < sortedIndexes.length; i++) {
        //    boolean finished = false;
        //    int j = 0;
        //    while (!finished)  {
        //        int indexValue = sortedIndexes[j];
        //        int leftChildIndex = leftChildOf(j);
        //        int rightChildIndex = rightChildOf(j);
        //        if (compare(indexValue, i)) {
        //            if (sortedIndexes[leftChildIndex] == -1) {
        //                sortedIndexes[leftChildIndex] = i;
        //                finished = true;
        //            } else if (sortedIndexes[rightChildIndex] == -1) {
        //                if (compare(i, sortedIndexes[leftChildIndex])) {
        //                    sortedIndexes[rightChildIndex] = i;
        //                } else {
        //                    sortedIndexes[rightChildIndex] = sortedIndexes[leftChildIndex];
        //                    sortedIndexes[leftChildIndex] = i;
        //                }
        //                finished = true;
        //            } else {
        //                j = (compare(i, sortedIndexes[leftChildIndex])) ? sortedIndexes[rightChildIndex] : sortedIndexes[leftChildIndex];
        //            }
        //        } else {
        //            boolean isDone = false;
        //            int buffer = i;
        //            int k = j;

        //            while(!isDone) {
        //                if (sortedIndexes[k] == -1) {
        //                    sortedIndexes[k] = buffer;
        //                    isDone = true;
        //                } else {
        //                    int temp = sortedIndexes[k];
        //                    sortedIndexes[k] = buffer;
        //                    buffer = temp;
        //                    if (sortedIndexes[leftChildOf(k)] == -1) {
        //                        k = leftChildOf(k);
        //                    } else {
        //                        k = rightChildOf(k);
        //                    }
        //                }
        //            }
        //            finished = true;
        //        }
        //    }
        //}
        //indexesToObjects(sortedIndexes);
    }
    private int leftChildOf(int i) {
        return (i*2)+1;
    }
    private int rightChildOf(int i) {
        return (i*2)+2;
    }


    // Sort helpers
    private void indexesToObjects(int[] indexes) {
        ArrayList<Object[]> returnVal = new ArrayList<>();
        for (int i : indexes) {
            returnVal.add(currentlySorting.get(i));
        }
        currentlySorting = returnVal;
    }
    private int charToInt(char character) {
        int c = Character.digit(character, 36);
        return (c == -1) ? 0: c;
    }
    private Object getValueAt(int index) {
        return currentlySorting.get(index)[sortingCol];
    }
    private void swap(int index1, int index2) {
        Object[] swapContainer = currentlySorting.get(index1);
        currentlySorting.set(index1, Arrays.stream(currentlySorting.get(index2)).toArray());
        currentlySorting.set(index2, Arrays.stream(swapContainer).toArray());
    }
    private boolean compare(int index1, int index2) {
        return compare(currentlySorting.get(index1), currentlySorting.get(index2));
    }
    private boolean compare(Object[] entry1, Object[] entry2) {
        Object obj1 = entry1[sortingCol];
        Object obj2 = entry2[sortingCol];
        String varType = colTypes[sortingCol];
        sortComparisons++;
        switch (varType) {
            case "int":
                return ((int) obj1 > (int) obj2);
            case "String": {
                String str1 = ((String) obj1).toLowerCase();
                String str2 = ((String) obj2).toLowerCase();
                return (str1.compareTo(str2) > 0);
            }
            case "char": {
                String str1 = (((char) obj1) + "").toLowerCase();
                String str2 = (((char) obj2) + "").toLowerCase();
                return (str1.compareTo(str2) > 0);
            }
        }
        return false;
    }

    public enum SortType {
        Bubble,
        Merge,
        Radix,
        Quick,
        Insertion
        //,Heap
    }
}