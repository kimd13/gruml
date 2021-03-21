package static_section.extractor;

import java.util.List;
import java.util.Set;

public interface Extractor {
    void extractAllInfo(String srcPath);
    List<String> getAllObjects();
    List<String> getAllObjectMethods(String objectName);
    boolean isObjectInheritedFrom(String objectName);
    boolean isObjectUsedByAnother(String objectName);
    int getNumberOfMethods();
    int getNumberOfObjects();
    List<String> getChildren(String objectName);
    Set<String> getUsedByObjects(String objectName);
}
