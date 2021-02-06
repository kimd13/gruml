package static_section.extractor.dependency.inheritance.model;

import java.util.ArrayList;
import java.util.List;

public class ClassInfoContainer {
    private Boolean isInterface = false;
    private Boolean isAbstract = false;
    private final List<String> children = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("(isInterface: %s, isAbstract: %s, children: %s)", isInterface, isAbstract, children);
    }

    public Boolean isInterface() {
        return isInterface;
    }

    public Boolean isAbstract() {
        return isAbstract;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setIsInterface(Boolean isInterface) {
        this.isInterface = isInterface;
    }

    public void setIsAbstract(Boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public ClassInfoContainer addChild(String child){
        children.add(child);
        return this;
    }
}
