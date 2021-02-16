package static_section.extractor.struct;

import java.util.ArrayList;
import java.util.List;

public class ClassInfoContainer {

    private Boolean isInterface = false;
    private boolean isParent = false;
    private final List<String> children = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("(isInterface: %s, isParent: %s, children: %s)", isInterface, isParent, children);
    }

    public Boolean isInterface() {
        return isInterface;
    }


    public boolean isParent(){
        return isParent;
    }

    public List<String> getChildren() {
        return children;
    }

    public ClassInfoContainer setIsParent(boolean isParent){
        this.isParent = isParent;
        return this;
    }

    public ClassInfoContainer setIsInterface(Boolean isInterface) {
        this.isInterface = isInterface;
        return this;
    }

    public ClassInfoContainer addChild(String child){
        children.add(child);
        return this;
    }
}
