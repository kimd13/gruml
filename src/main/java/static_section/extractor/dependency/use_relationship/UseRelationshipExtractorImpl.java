package static_section.extractor.dependency.use_relationship;

import util.regex.RegexUtil;

import java.util.*;

public class UseRelationshipExtractorImpl implements UseRelationshipExtractor {

    private final RegexUtil regexUtil = RegexUtil.getInstance();
    private final HashMap<String, List<String>> useRelationshipMap = new HashMap<>();

    @Override
    public void extractAllUseRelationshipInfo(List<String> objectsAsStrings) {
        for (String object: objectsAsStrings){
            populateUseRelationshipMapKeys(object);
        }
        for (String object: objectsAsStrings){
            populateUseRelationshipMapValues(object);
        }
    }

    private void populateUseRelationshipMapKeys(String target){
        List<String> objectNames = extractObjectNames(target);
        for (String objectName: objectNames){
            useRelationshipMap.put(objectName, new ArrayList<>());
        }
    }

    private void populateUseRelationshipMapValues(String target){
        String useRelationshipRegex = formulateUseRelationshipRegex();
        Set<String> useRelationships = new HashSet<>(regexUtil.getMatched(useRelationshipRegex, target));
        // need to match name to useRelationships
        System.out.println(useRelationships);
    }

    private String formulateUseRelationshipRegex(){
        StringBuilder useRelationshipRegexBuilder = new StringBuilder();
        for (String object: useRelationshipMap.keySet()){
            useRelationshipRegexBuilder.append(object).append("|");
        }
        String useRelationshipRegex = useRelationshipRegexBuilder.toString();
        return "(" + useRelationshipRegex.substring(0, useRelationshipRegex.length() - 1) + ")";
    }

    private List<String> extractObjectNames(String target){
        String objectRegex = "(?<=class |interface )(.[^\\t\\n\\r ]*)";
        return regexUtil.getMatched(objectRegex, target);
    }
}
