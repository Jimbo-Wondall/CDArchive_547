package Models;

public interface ITableObject {
    String[] toStringArray();
    void fromStringArray(String[] input);
    Object[] toObjArray();
    String[] getDataTypes();
    String[] getPropertyNames();
    Object getByIndex(int index);
    void setByIndex(int index, Object input);
    void setID(int id);
    int getID();
}
