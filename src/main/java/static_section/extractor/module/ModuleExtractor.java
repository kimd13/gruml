package static_section.extractor.module;

import java.util.List;

public interface ModuleExtractor {
    void extractAllModulesInfo(String srcPath);
    List<String> getAllModuleNames();
    List<String> getAllModuleObjects(String moduleName);
    List<String> getAllObjectMethods(String moduleName, String objectName);
}
