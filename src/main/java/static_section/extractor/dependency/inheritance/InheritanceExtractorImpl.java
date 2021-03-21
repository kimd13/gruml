package static_section.extractor.dependency.inheritance;

import static_section.extractor.struct.ClassInfoContainer;
import static_section.extractor.struct.InheritanceRelationship;
import util.regex.RegexUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InheritanceExtractorImpl implements InheritanceExtractor {

    private final HashMap<String, ClassInfoContainer> inheritanceMap = new HashMap<>();

    @Override
    public void extractAllInheritanceInfo(List<String> objectsAsStrings) {
        for (String object: objectsAsStrings){
            populateInheritanceMap(object);
        }
    }

    @Override
    public List<String> getChildren(String objectName) {
        return inheritanceMap.get(objectName).getChildren();
    }

    @Override
    public boolean isObjectInheritedFrom(String objectName) {
        ClassInfoContainer classInfoContainer = inheritanceMap.get(objectName);
        if (classInfoContainer == null){
            return false;
        } else {
            return classInfoContainer.isParent();
        }
    }

    @Override
    public Boolean checkIfInterface(String objectName) {
        return inheritanceMap.get(objectName).isInterface();
    }

    public void populateInheritanceMap(String target){
        List<String> inheritanceSubstrings = getInheritanceSubstrings(target);
        List<InheritanceRelationship> inheritanceRelationships = convertInheritanceSubstringsToRelationships(inheritanceSubstrings);
        for (InheritanceRelationship relationship: inheritanceRelationships){
            inheritanceMap.computeIfAbsent(relationship.getParent(), info -> new ClassInfoContainer())
                    .addChild(relationship.getChild())
                    .setIsParent(true)
                    .setIsInterface(relationship.isParentInterface());
        }
    }

    public List<InheritanceRelationship> convertInheritanceSubstringsToRelationships(List<String> inheritanceSubstrings){
        List<InheritanceRelationship> relationships = new ArrayList<>();
        for (String inheritanceSubstring : inheritanceSubstrings) {
            String childName = extractChildName(inheritanceSubstring);
            List<String> interfaceParents = extractParents(inheritanceSubstring, true);
            List<String> classParents = extractParents(inheritanceSubstring, false);
            interfaceParents.forEach(parent -> {
                relationships.add(new InheritanceRelationship(childName, "implements", parent));
            });
            classParents.forEach(parent -> {
                relationships.add(new InheritanceRelationship(childName, "extends", parent));
            });
        }
        return relationships;
    }

    public String extractChildName(String inheritanceSubstring){
        String [] classKeywordRemoved = inheritanceSubstring.split("class");
        return extractFirstWordInString(classKeywordRemoved[1]);
    }

    public List<String> extractParents(String inheritanceSubstring, Boolean getInterfaceParents){
        String splitKeyword =  getInterfaceParents ? "implements" : "extends";
        List<String> parents = new ArrayList<>();
        String [] split = inheritanceSubstring.split(splitKeyword);
        for (int i = 1; i < split.length; i++){
            String parent = extractFirstWordInString(split[i]);
            parents.add(parent);
        }
        return parents;
    }

    public String extractFirstWordInString(String target){
        return target.split(" ")[1];
    }

    public List<String> getInheritanceSubstrings(String target){
        String inheritanceRegex = "(abstract )?class(.[^\\t\\n\\r ]*)(( extends| implements) (.[^\\t\\n\\r ]*))*";
         return RegexUtil.getMatched(inheritanceRegex, target);
    }
}
