package Models;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CDModel implements ITableObject {
    int ID;
    String Title;
    String Author;
    char Section;
    int X;
    int Y;
    int BarCode;
    String Description;
    boolean OnLoan;

    public CDModel(String... input) {
        if (input.length > 1) {
            fromStringArray(input);
        } else {
            fromStringArray(input[0].split(";"));
        }
    }

    public CDModel() {

    }


    @Override
    public String[] toStringArray() {
        return new String[] {
                String.valueOf(ID),
                Title,
                Author,
                String.valueOf(Section),
                String.valueOf(X),
                String.valueOf(Y),
                String.valueOf(BarCode),
                Description,
                String.valueOf(OnLoan)
        };
    }
    @Override
    public void fromStringArray(String[] input) {
        try {
            if (input[0].isEmpty()) {
                ID = -1;
            } else {
                ID = Integer.parseInt(input[0]);
            }

            Title = input[1];
            Author = input[2];
            Section = input[3].charAt(0);
            X = Integer.parseInt(input[4]);
            Y = Integer.parseInt(input[5]);
            BarCode = Integer.parseInt(input[6]);
            Description = input[7];
            OnLoan = Boolean.parseBoolean(input[8]);
        } catch (Exception e) {
            System.out.println("Incorrect format: " + e.getMessage());
        }
    }
    @Override
    public Object[] toObjArray() {
        return new Object[] { ID, Title, Author, Section, X, Y, BarCode, Description, OnLoan };
    }
    @Override
    public String[] getDataTypes() {
        Field[] properties = this.getClass().getFields();
        return Arrays.stream(properties).map(Field::getClass).map(Class::getSimpleName).toArray(String[]::new);
    }
    @Override
    public String[] getPropertyNames() {
        Field[] properties = this.getClass().getFields();
        return Arrays.stream(properties).map(Field::getName).toArray(String[]::new);
    }
    @Override
    public Object getByIndex(int index) {
        return switch (index) {
            case 0 -> ID;
            case 1 -> Title;
            case 2 -> Author;
            case 3 -> Section;
            case 4 -> X;
            case 5 -> Y;
            case 6 -> BarCode;
            case 7 -> Description;
            case 8 -> OnLoan;
            default -> null;
        };
    }
    @Override
    public void setByIndex(int index, Object input) {
        switch (index) {
            case 0 -> ID = (int) input;
            case 1 -> Title = (String) input;
            case 2 -> Author = (String) input;
            case 3 -> Section = (char) input;
            case 4 -> X = (int) input;
            case 5 -> Y = (int) input;
            case 6 -> BarCode = (int) input;
            case 7 -> Description = (String) input;
            case 8 -> OnLoan = (boolean) input;
        }
    }
    @Override
    public void setID(int id) {
        ID = id;
    }
    @Override
    public int getID() {
        return ID;
    }
}
