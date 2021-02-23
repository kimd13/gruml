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
    private int numberOfMethods = 0;

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

    @Override
    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    @Override
    public int getNumberOfObjects() {
        return objectMap.size();
    }

    private List<String> extractMethods(String objectName, String target, boolean withParams){
        List<String> filteredMethods = new ArrayList<>();
        List<String> declaredMethods = findDeclaredMethods(target);

        for (String declaredMethod: declaredMethods){
            String methodNameAndParams = extractMethodNameAndParams(declaredMethod);
            if (!isConstructor(objectName, methodNameAndParams)){
                numberOfMethods++;
                filteredMethods.add(getMethodWithOrWithoutParams(withParams, methodNameAndParams));
            }
        }

        return filteredMethods;
    }

    private String getMethodWithOrWithoutParams(boolean withParams, String method){
        if (!withParams) {
            return removeParams(method) + "()";
        }
        return method;
    }

    private String extractMethodNameAndParams(String method){
        String methodNameAndParamsRegex = "([a-zA-Z0-9_]+) *(\\(.*?\\))";
        return regexUtil.getMatched(methodNameAndParamsRegex, method).get(0);
    }

    private List<String> findDeclaredMethods(String target){
        // This regex finds all methods whether or not they are declared or called
        String allMethodRegex = "([a-zA-Z0-9<>_?]+) +([a-zA-Z0-9_]+) *\\(.*?\\) *(\\{|\\;)";
        return removeCalledMethods(regexUtil.getMatched(allMethodRegex, target));
    }

    private List<String> removeCalledMethods(List<String> methods){
        List<String> declaredMethods = new ArrayList<>();
        for (String method: methods){
            if (!method.contains("return ") && !method.contains("new ")){
                declaredMethods.add(method);
            }
        }
        return declaredMethods;
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
