package static_section.extractor.dependency.use_relationship;

import static_section.extractor.general.ObjectInfoExtractor;
import static_section.extractor.struct.UseRelationshipContainer;
import util.regex.RegexUtil;
import java.util.*;

public class UseRelationshipExtractorImpl implements UseRelationshipExtractor {

    private final RegexUtil regexUtil = RegexUtil.getInstance();
    private final HashMap<String, UseRelationshipContainer> useRelationshipMap = new HashMap<>();
    private final ObjectInfoExtractor objectInfoExtractor;

    public UseRelationshipExtractorImpl(ObjectInfoExtractor objectInfoExtractor){
        this.objectInfoExtractor = objectInfoExtractor;
    }

    @Override
    public void extractAllUseRelationshipInfo(List<String> objectsAsStrings) {
        populateUseRelationshipMapKeys();
        for (String object: objectsAsStrings){
            populateUseRelationshipMapValues(object);
        }
        System.out.println(useRelationshipMap);
    }

    @Override
    public boolean isObjectUsedByAnother(String objectName) {
        return !useRelationshipMap.get(objectName).getUsedBy().isEmpty();
    }

    private void populateUseRelationshipMapKeys(){
        List<String> objectNames = objectInfoExtractor.getAllObjects();
        for (String objectName: objectNames){
            useRelationshipMap.put(objectName, new UseRelationshipContainer());
        }
    }

    private void populateUseRelationshipMapValues(String target){

        String objectName = extractObjectName(target).get(0);
        List<String> objectsFoundInTarget = getObjectsFoundInTarget(removeClassDeclaration(target));
        List<String> cleanedObjectsFoundInTarget = new ArrayList<>();

        for (String objectFoundInTarget: objectsFoundInTarget){
            String cleanedObjectFoundInTarget = strippedFrom(objectFoundInTarget, Arrays.asList(" ", "\\(", "\\{", "\\<", "\\>"));
            if (isNotSelf(objectName, cleanedObjectFoundInTarget)){
                useRelationshipMap.get(cleanedObjectFoundInTarget).addToUsedBy(objectName);
                cleanedObjectsFoundInTarget.add(cleanedObjectFoundInTarget);
            }
        }

        useRelationshipMap.get(objectName).addAllToUsed(removedDuplicates(cleanedObjectsFoundInTarget));
    }

    private String removeClassDeclaration(String target){
        // Must remove class declaration to rid of inheritance dependencies
        return target.replaceFirst("(.*)(\\{)", "");
    }

    private List<String> getObjectsFoundInTarget(String target){
        String objectsInProjectRegex = getObjectsInProjectRegex();
        return regexUtil.getMatched(objectsInProjectRegex, target);
    }

    private boolean isNotSelf(String me, String other){
        return !me.equals(other);
    }

    private String strippedFrom(String target, List<String> words){
        return target.replaceAll(getOrRegex(words), "");
    }

    private List<String> removedDuplicates(List<String> useRelationships){
        return new ArrayList<>(new HashSet<>(useRelationships));
    }

    private String getObjectsInProjectRegex(){
        String orRegex = getOrRegex(useRelationshipMap.keySet());
        return "( |\\(|\\<)" + orRegex + "( |\\(|\\>| *\\{)";
    }

    private String getOrRegex(Iterable<String> words){
        StringBuilder stringBuilder = new StringBuilder();
        for (String word: words){
            stringBuilder.append(word).append("|");
        }
        String string = stringBuilder.toString();
        return "(" + string.substring(0, string.length() - 1) + ")";
    }

    private List<String> extractObjectName(String target){
        String objectRegex = "(?<=class |interface )(.[^\\t\\n\\r ]*)";
        return regexUtil.getMatched(objectRegex, target);
    }
}
