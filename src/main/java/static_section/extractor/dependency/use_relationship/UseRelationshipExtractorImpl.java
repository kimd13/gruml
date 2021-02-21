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
        String useRelationshipRegex = formulateUseRelationshipRegex();
        String objectName = extractObjectName(target).get(0);
        List<String> useRelationships = regexUtil.getMatched(useRelationshipRegex, target);
        List<String> cleanedUseRelationships = cleanedUseRelationships(objectName, useRelationships);
        useRelationshipMap.get(objectName).addAllToUsed(cleanedUseRelationships);
    }

    private List<String> cleanedUseRelationships(String objectName, List<String> useRelationships){
        List<String> strippedFromSpacesAndSelf = strippedFromSpecialCharsAndSelf(objectName, useRelationships);
        return removedDuplicates(strippedFromSpacesAndSelf);
    }

    private List<String> removedDuplicates(List<String> useRelationships){
        return new ArrayList<>(new HashSet<>(useRelationships));
    }

    private List<String> strippedFromSpecialCharsAndSelf(String objectName, List<String> useRelationships){
        List<String> stripped = new ArrayList<>();
        for (String useRelationship: useRelationships){
            String specialCharsRemoved = useRelationship
                    .replace(" ", "")
                    .replace("(", "")
                    .replace("{", "")
                    .replace("<", "")
                    .replace(">", "");
            if (!specialCharsRemoved.equals(objectName)){
                useRelationshipMap.get(specialCharsRemoved).addToUsedBy(objectName);
                stripped.add(specialCharsRemoved);
            }
        }
        return stripped;
    }

    private String formulateUseRelationshipRegex(){
        StringBuilder useRelationshipRegexBuilder = new StringBuilder();
        for (String object: useRelationshipMap.keySet()){
            useRelationshipRegexBuilder.append(object).append("|");
        }
        String useRelationshipRegex = useRelationshipRegexBuilder.toString();
        return "( |\\(|\\<)(" + useRelationshipRegex.substring(0, useRelationshipRegex.length() - 1) + ")( |\\(|\\>| *\\{)";
    }

    private List<String> extractObjectName(String target){
        String objectRegex = "(?<=class |interface )(.[^\\t\\n\\r ]*)";
        return regexUtil.getMatched(objectRegex, target);
    }
}
