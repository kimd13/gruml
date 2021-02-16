package static_section.extractor.struct;

public class InheritanceRelationship {

    private final String child;
    private final String parent;
    private final Boolean isParentInterface;

    public InheritanceRelationship(String child, String extendsOrImplements, String parent){
        this.child = child;
        this.parent = parent;
        this.isParentInterface = determineIsInterface(extendsOrImplements);
    }

    private Boolean determineIsInterface(String extendsOrImplements){
        switch (extendsOrImplements){
            case "extends":
                return false;
            case "implements":
                return true;
        }
        return null;
    }

    public String getChild() {
        return child;
    }

    public String getParent() {
        return parent;
    }

    public Boolean isParentInterface(){
        return isParentInterface;
    }
}