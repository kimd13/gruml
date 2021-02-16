package static_section.extractor.general;

import util.regex.RegexUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectInfoExtractorImpl implements ObjectInfoExtractor{

    private final boolean SHOULD_METHODS_INCLUDE_PARAMS = false;
    private final RegexUtil regexUtil = RegexUtil.getInstance();
    // key is object name, value is methods
    private final HashMap<String, List<String>> objectMap = new HashMap<>();

    @Override
    public void extractAllObjectInfo(List<String> objectsAsStrings) {
        for (String object: objectsAsStrings){
            String objectName = extractObjectName(object).get(0);
            List<String> methods = extractMethods(objectName, object, SHOULD_METHODS_INCLUDE_PARAMS);
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

    private List<String> extractMethods(String objectName, String target, boolean withParams){
        String completeMethodRegex = "(public +|private +|static +|protected +|abstract +|native +|synchronized +)?([a-zA-Z0-9<>._?,]+) +([a-zA-Z0-9_]+) *\\([a-zA-Z0-9<>\\[\\]._?, ]*\\) *([a-zA-Z0-9_ ,]*) *(\\{)";
        String methodNameRegex = "([a-zA-Z0-9_]+) *(\\(.*?\\))";
        List<String> methods = new ArrayList<>();
        List<String> completeMethods = regexUtil.getMatched(completeMethodRegex, target);
        for (String completeMethod: completeMethods){
            String method = (regexUtil.getMatched(methodNameRegex, completeMethod)).get(0);
            if (!isConstructor(objectName, method)){
                if (withParams) {
                    methods.add(method);
                } else {
                    methods.add(removeParams(method) + "()");
                }
            }
        }
        return methods;
    }

    private boolean isConstructor(String objectName, String method){
        String justName = removeParams(method);
        return justName.equals(objectName);
    }

    private String removeParams(String method){
        return method.split("\\(")[0];
    }

    private List<String> extractObjectName(String target){
        String objectRegex = "(?<=class |interface )(.[^\\t\\n\\r ]*)";
        return regexUtil.getMatched(objectRegex, target);
    }
}
