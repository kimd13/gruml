package static_section.extractor.general;

import util.regex.RegexUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectInfoExtractorImpl implements ObjectInfoExtractor{

    private final RegexUtil regexUtil = RegexUtil.getInstance();
    // key is object name, value is methods
    private HashMap<String, List<String>> objectMap = new HashMap<>();

    @Override
    public void extractAllObjectInfo(List<String> objectsAsStrings) {
        for (String object: objectsAsStrings){
            String objectName = extractObjectName(object).get(0);
            List<String> methods = extractMethods(object);
            objectMap.put(objectName, methods);
        }
    }

    @Override
    public List<String> getAllObjects() {
        return new ArrayList<String>(objectMap.keySet());
    }

    @Override
    public List<String> getAllObjectMethods(String objectName) {
        return objectMap.get(objectName);
    }

    private List<String> extractMethods(String target){
        String unfilteredMethodRegex = "(public +|private +|static +|protected +|abstract +|native +|synchronized +)?([a-zA-Z0-9<>._?,]+) +([a-zA-Z0-9_]+) *\\([a-zA-Z0-9<>\\[\\]._?, ]*\\) *([a-zA-Z0-9_ ,]*)";
        String methodNameRegex = "([a-zA-Z0-9_]+) *(\\(.*?\\))";
        List<String> methods = new ArrayList<>();
        List<String> unfilteredMethods = regexUtil.getMatched(unfilteredMethodRegex, target);
        for (String unfilteredMethod: unfilteredMethods){
            String method = (regexUtil.getMatched(methodNameRegex, unfilteredMethod)).get(0);
            methods.add(method);
        }
        return methods;
    }

    private List<String> extractObjectName(String target){
        String objectRegex = "(?<=class |interface )(.[^\\t\\n\\r ]*)";
        return regexUtil.getMatched(objectRegex, target);
    }
}