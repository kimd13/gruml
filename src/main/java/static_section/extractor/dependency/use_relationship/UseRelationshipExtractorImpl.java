package static_section.extractor.dependency.use_relationship;

import static_section.extractor.Extractor;

import java.util.HashMap;
import java.util.List;

public class UseRelationshipExtractorImpl extends Extractor implements UseRelationshipExtractor {

    private HashMap<String, List<String>> useRelationshipMap;

    @Override
    public void extractAllUseRelationshipInfo(String srcPath) {
        try {
            List<String> paths = fileUtil.getAllFilePaths(srcPath);
            for (String path : paths) {
                String moduleName = fileUtil.getLastSegmentOfPath(path);
                if (moduleName.endsWith(".java")) {
                    String fileContent = fileUtil.readFile(path);
                    String filteredFileContent = removeUnnecessarySubstrings(fileContent);
                    populateUseRelationshipMap(filteredFileContent);
                }
            }
            System.out.println(useRelationshipMap);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void populateUseRelationshipMap(String target){

    }
}
