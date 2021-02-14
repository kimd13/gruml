package static_section.diagram.struct;

public class ModuleRow {

    private final String value;
    private int columnIndex;

    public ModuleRow(ModuleRowType type, String value){
        this.value = value;
        assignColumnIndex(type);
    }

    private void assignColumnIndex(ModuleRowType type){
        switch (type){
            case OBJECT:
                columnIndex = 0;
                return;
            case METHOD:
                columnIndex = 1;
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
        OBJECT, METHOD
    }
}
