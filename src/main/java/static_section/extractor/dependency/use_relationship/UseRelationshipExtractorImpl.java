package static_section.extractor.dependency.use_relationship;

import static_section.extractor.Extractor;

import java.util.*;

public class UseRelationshipExtractorImpl extends Extractor implements UseRelationshipExtractor {

    private HashMap<String, List<String>> useRelationshipMap = new HashMap<>();

    @Override
    public void extractAllUseRelationshipInfo(String srcPath) {
        try {
            List<String> paths = fileUtil.getAllFilePaths(srcPath);
            for (String path : paths) {
                String moduleName = fileUtil.getLastSegmentOfPath(path);
                if (moduleName.endsWith(".java")) {
                    String fileContent = fileUtil.readFile(path);
                    String filteredFileContent = removeUnnecessarySubstrings(fileContent);
                    populateUseRelationshipMapKeys(filteredFileContent);
                }
            }
            for (String path : paths) {
                String moduleName = fileUtil.getLastSegmentOfPath(path);
                if (moduleName.endsWith(".java")) {
                    String fileContent = fileUtil.readFile(path);
                    String filteredFileContent = removeUnnecessarySubstrings(fileContent);
                    populateUseRelationshipMapValues(filteredFileContent);
                }
            }
            System.out.println(useRelationshipMap);
        } catch (Exception e){
            System.out.println(e.getMessage());
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
}
