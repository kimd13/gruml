package static_section.extractor.module.model;

import java.util.HashMap;

public class ModuleObjectContainer {
    private HashMap<String, ObjectMethodContainer> objects = new HashMap<>();

    @Override
    public String toString() {
        return objects.toString();
    }

    public ObjectMethodContainer getObject(String name){
        return objects.get(name);
    }

    public void addObject(String name, ObjectMethodContainer object){
        objects.put(name, object);
    }
}
