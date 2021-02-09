package static_section;

import static_section.extractor.dependency.inheritance.InheritanceExtractor;
import static_section.extractor.dependency.inheritance.InheritanceExtractorImpl;
import static_section.extractor.dependency.use_relationship.UseRelationshipExtractor;
import static_section.extractor.dependency.use_relationship.UseRelationshipExtractorImpl;
import static_section.extractor.module.ModuleExtractor;
import static_section.extractor.module.ModuleExtractorImpl;
import static_section.model.DependencyWorkBook;
import static_section.model.ModuleRow;

public class DependencyDiagramImpl implements DependencyDiagram {

    private ModuleExtractor moduleExtractor = new ModuleExtractorImpl();
    private InheritanceExtractor inheritanceExtractor = new InheritanceExtractorImpl();
    private UseRelationshipExtractor useRelationshipExtractor = new UseRelationshipExtractorImpl();

    @Override
    public void create(String srcPath) {
        populateDiagrams(srcPath);
        createDependencyWorkbook();
    }

    public void populateDiagrams(String srcPath){
        moduleExtractor.extractAllModulesInfo(srcPath);
        useRelationshipExtractor.extractAllUseRelationshipInfo(srcPath);
        inheritanceExtractor.extractAllInheritanceInfo(srcPath);
    }

    private void createDependencyWorkbook(){
        DependencyWorkBook dependencyWorkBook = new DependencyWorkBook();
        dependencyWorkBook.createWorkBook("testing");
        dependencyWorkBook.createSpreadsheet("dependency");
        for (String moduleName: moduleExtractor.getAllModuleNames()){
            ModuleRow module = new ModuleRow(ModuleRow.ModuleRowType.MODULE, moduleName);
            dependencyWorkBook.addRow(module);
            for (String objectName: moduleExtractor.getAllModuleObjects(moduleName)){
                ModuleRow object = new ModuleRow(ModuleRow.ModuleRowType.OBJECT, objectName);
                dependencyWorkBook.addRow(object);
                for (String methodName: moduleExtractor.getAllObjectMethods(moduleName, objectName)){
                    ModuleRow method = new ModuleRow(ModuleRow.ModuleRowType.METHOD, methodName);
                    dependencyWorkBook.addRow(method);
                }
            }
        }
        try {
            dependencyWorkBook.closeWorkBook();
        } catch (Exception e){
            System.out.println("Oops");
        }
    }
}
