package dependency;

import dependency.inheritance.InheritanceDependencyDiagram;
import dependency.inheritance.InheritanceDependencyDiagramImpl;
import dependency.use_relationship.UseRelationshipDependencyDiagram;
import dependency.use_relationship.UseRelationshipDependencyDiagramImpl;

import java.io.File;

public class DependencyDiagramImpl implements DependencyDiagram {

    private InheritanceDependencyDiagram inheritanceDependencyDiagram = new InheritanceDependencyDiagramImpl();
    private UseRelationshipDependencyDiagram useRelationshipDependencyDiagram = new UseRelationshipDependencyDiagramImpl();

    @Override
    public File create(String srcPath) {
        return null;
    }
}
