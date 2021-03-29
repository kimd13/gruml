package static_section.extractor;

import java.util.List;
import java.util.Set;

/**
 * Extractor serves as a collection and interface for all other extractors
 * It initializes and provides everything the other extractors need
 * All necessary methods for diagram generation are located here
 */
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
