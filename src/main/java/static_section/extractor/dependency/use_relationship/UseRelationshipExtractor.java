package static_section.extractor.dependency.use_relationship;

import java.util.List;
import java.util.Set;

public interface UseRelationshipExtractor {
    void extractAllUseRelationshipInfo(List<String> objectsAsStrings);
    boolean isObjectUsedByAnother(String objectName);
    Set<String> getUsedByObjects(String objectName);
}
