package static_section.extractor.module;

import static_section.extractor.Extractor;
import static_section.extractor.module.model.ModuleObjectContainer;
import static_section.extractor.module.model.ObjectMethodContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleExtractorImpl extends Extractor implements ModuleExtractor {

    private HashMap<String, ModuleObjectContainer> moduleMap = new HashMap<>();

    @Override
    public void extractAllModulesInfo(String srcPath) {
        try {
            List<String> paths = fileUtil.getAllFilePaths(srcPath);
            for (String path : paths) {
                String moduleName = fileUtil.getLastSegmentOfPath(path);
                if (moduleName.endsWith(".java")) {
                    String fileContent = fileUtil.readFile(path);
                    String filteredFileContent = removeUnnecessarySubstrings(fileContent);
                    moduleMap.put(moduleName, extractModuleInfoContainer(filteredFileContent));
                }
            }
            System.out.println(moduleMap);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> getAllModuleNames(){
        return new ArrayList<String>(moduleMap.keySet());
    }

    @Override
    public List<String> getAllModuleObjects(String moduleName){
        return moduleMap.get(moduleName).getAllObjects();
    }

    @Override
    public List<String> getAllObjectMethods(String moduleName, String objectName){
        return moduleMap.get(moduleName).getObject(objectName).getMethods();
    }

    private ModuleObjectContainer extractModuleInfoContainer(String target){
        ModuleObjectContainer moduleObjectContainer = new ModuleObjectContainer();
        List<String> objectNames = extractObjectNames(target);
        for (String name: objectNames){
            ObjectMethodContainer methods = extractMethods(target);
            moduleObjectContainer.addObject(name, methods);
        }
        return moduleObjectContainer;
    }

    private List<String> extractObjectNames(String target){
        String objectRegex = "(?<=class |interface )(.[^\\t\\n\\r ]*)";
        return regexUtil.getMatched(objectRegex, target);
    }

    private ObjectMethodContainer extractMethods(String target){
        String unfilteredMethodRegex = "(public +|private +|static +|protected +|abstract +|native +|synchronized +)?([a-zA-Z0-9<>._?,]+) +([a-zA-Z0-9_]+) *\\([a-zA-Z0-9<>\\[\\]._?, ]*\\) *([a-zA-Z0-9_ ,]*)";
        String methodNameRegex = "([a-zA-Z0-9_]+) *(\\(.*?\\))";
        ObjectMethodContainer objectMethodContainer = new ObjectMethodContainer();
        List<String> unfilteredMethods = regexUtil.getMatched(unfilteredMethodRegex, target);
        for (String unfilteredMethod: unfilteredMethods){
            String method = (regexUtil.getMatched(methodNameRegex, unfilteredMethod)).get(0);
            objectMethodContainer.addMethod(method);
        }
        return objectMethodContainer;
    }
}
