package static_section.diagram.struct;

import java.util.Arrays;
import java.util.List;

public class DependencyRow {

    public static int IS_USED_BY_ANOTHER_OBJECT_INDEX = 0;
    public static int IS_INHERITED_FROM_INDEX = 1;
    public static int NAME_INDEX = 2;

    public final boolean isInheritedFrom;
    public final boolean isUsedByAnotherObject;
    public String name;
    public ModuleRowType type;

    public DependencyRow(ModuleRowType type, String name, boolean isInheritedFrom, boolean isUsedByAnotherObject){
        this.isInheritedFrom = isInheritedFrom;
        this.isUsedByAnotherObject = isUsedByAnotherObject;
        this.type = type;
        assignName(type, name);
    }

    private void assignName(ModuleRowType type, String name){
        if (type == ModuleRowType.METHOD){
            this.name = String.format("     %s", name);
        } else {
            this.name = name;
        }
    }

    public boolean isObject(){
        return type == ModuleRowType.OBJECT;
    }

    public List<String> getRow(){
        return Arrays.asList(isUsedByAnotherObjectAsString(), isInheritedFromAsString(), name);
    }

    private String isUsedByAnotherObjectAsString(){
        if (isUsedByAnotherObject){
            return String.valueOf('\u2192');
        }
        return "";
    }

    private String isInheritedFromAsString(){
        if (isInheritedFrom){
            return String.valueOf('\u1405');
        }
        return "";
    }

    public enum ModuleRowType{
        OBJECT, METHOD
    }
}
