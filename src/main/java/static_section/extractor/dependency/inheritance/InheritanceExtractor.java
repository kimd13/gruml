package static_section.extractor.dependency.inheritance;

import java.util.List;

public interface InheritanceExtractor {
    void extractAllInheritanceInfo(List<String> objectsAsStrings);
    List<String> getAllObjectNames();
    List<String> getChildren(String objectName);
    boolean isObjectInheritedFrom(String objectName);
    Boolean checkIfInterface(String objectName);
}
