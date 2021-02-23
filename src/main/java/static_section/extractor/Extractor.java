package static_section.extractor;

import java.util.List;

public interface Extractor {
    void extractAllInfo(String srcPath);
    List<String> getAllObjects();
    List<String> getAllObjectMethods(String objectName);
    boolean isObjectInheritedFrom(String objectName);
    boolean isObjectUsedByAnother(String objectName);
    int getNumberOfMethods();
    int getNumberOfObjects();
}
