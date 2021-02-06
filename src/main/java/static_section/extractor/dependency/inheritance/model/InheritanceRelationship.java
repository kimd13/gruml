package static_section.extractor.dependency.inheritance.model;

public class InheritanceRelationship {
    private final String child;
    private final String parent;
    private final Boolean isParentInterface;
    private final Boolean isChildAbstract;

    public InheritanceRelationship(String childPrecedent, String child, String extendsOrImplements, String parent){
        this.child = child;
        this.parent = parent;
        this.isParentInterface = determineIsInterface(extendsOrImplements);
        this.isChildAbstract = determineIsAbstract(childPrecedent);
    }

    private Boolean determineIsAbstract(String classNamePrecedent){
        return classNamePrecedent.equals("abstract");
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

    public Boolean isChildAbstract() {
        return isChildAbstract;
    }
}