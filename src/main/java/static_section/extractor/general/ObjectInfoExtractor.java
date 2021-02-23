package static_section.extractor.general;

import java.util.List;

public interface ObjectInfoExtractor {
    void extractAllObjectInfo(List<String> objectsAsStrings);
    List<String> getAllObjects();
    List<String> getAllObjectMethods(String objectName);
    int getNumberOfMethods();
    int getNumberOfObjects();
}
