package static_section.extractor.module.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectMethodContainer {
    private List<String> methods = new ArrayList<>();

    @Override
    public String toString() {
        return methods.toString();
    }

    public List<String> getMethods() {
        return methods;
    }

    public void addMethod(String method){
        methods.add(method);
    }
}
