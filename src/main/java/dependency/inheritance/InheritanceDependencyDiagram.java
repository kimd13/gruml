package dependency.inheritance;

import java.util.List;

public interface InheritanceDependencyDiagram {
    void populateDiagram(String srcPath);
    List getInheritanceList(String name);
}
