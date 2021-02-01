package dependency.inheritance;

import java.util.List;

public interface InheritanceDependencyDiagram {
    void populateDiagram(String srcPath);
    List<String> getAll();
    List<String> getChildren(String name);
    Boolean checkIfAbstract(String name);
    Boolean checkIfInterface(String name);
}
