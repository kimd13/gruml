package static_section.dependency.inheritance;

import java.util.List;

public interface InheritanceExtractor {
    void extract(String srcPath);
    List<String> getAll();
    List<String> getChildren(String name);
    Boolean checkIfAbstract(String name);
    Boolean checkIfInterface(String name);
}
