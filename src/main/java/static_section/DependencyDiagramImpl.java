package static_section;

import static_section.extractor.dependency.inheritance.InheritanceExtractor;
import static_section.extractor.dependency.inheritance.InheritanceExtractorImpl;
import static_section.extractor.dependency.use_relationship.UseRelationshipExtractor;
import static_section.extractor.dependency.use_relationship.UseRelationshipExtractorImpl;
import static_section.extractor.module.ModuleExtractor;
import static_section.extractor.module.ModuleExtractorImpl;

public class DependencyDiagramImpl implements DependencyDiagram {

    private ModuleExtractor moduleExtractor = new ModuleExtractorImpl();
    private InheritanceExtractor inheritanceExtractor = new InheritanceExtractorImpl();
    private UseRelationshipExtractor useRelationshipExtractor = new UseRelationshipExtractorImpl();

    @Override
    public void create(String srcPath) {
        populateDiagrams(srcPath);

    }

    public void populateDiagrams(String srcPath){
        //inheritanceExtractor.extractAllInheritanceInfo(srcPath);
        moduleExtractor.extractAllModulesInfo(srcPath);
    }
}
