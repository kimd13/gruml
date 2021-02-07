package static_section.model;

public class ModuleRow {

    private final String value;
    private int columnIndex;

    public ModuleRow(ModuleRowType type, String value){
        this.value = value;
        assignColumnIndex(type);
    }

    private void assignColumnIndex(ModuleRowType type){
        switch (type){
            case MODULE:
                columnIndex = 0;
                return;
            case OBJECT:
                columnIndex = 1;
                return;
            case METHOD:
                columnIndex = 2;
                return;
        }
    }

    public String getValue() {
        return value;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public enum ModuleRowType{
        MODULE, OBJECT, METHOD
    }
}
