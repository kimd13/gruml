package static_section.extractor.dependency.inheritance;

import java.util.List;

public interface InheritanceExtractor {
    void extractAllInheritanceInfo(String srcPath);
    List<String> getAllObjectNames();
    List<String> getChildren(String name);
    Boolean checkIfAbstract(String name);
    Boolean checkIfInterface(String name);
}
