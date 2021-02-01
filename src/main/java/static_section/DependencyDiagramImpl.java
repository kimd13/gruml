package static_section;

import static_section.dependency.inheritance.InheritanceExtractor;
import static_section.dependency.inheritance.InheritanceExtractorImpl;
import static_section.dependency.use_relationship.UseRelationshipExtractor;
import static_section.dependency.use_relationship.UseRelationshipExtractorImpl;

public class DependencyDiagramImpl implements DependencyDiagram {

    private InheritanceExtractor inheritanceExtractor = new InheritanceExtractorImpl();
    private UseRelationshipExtractor useRelationshipExtractor = new UseRelationshipExtractorImpl();

    @Override
    public void create(String srcPath) {
        populateDiagrams(srcPath);

    }

    public void populateDiagrams(String srcPath){
        inheritanceExtractor.extract(srcPath);
    }
}
