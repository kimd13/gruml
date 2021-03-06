package output;

import static_section.diagram.DependencyDiagram;
import dynamic_section.SequenceDiagram;

public interface OutputGenerator {
    void initialize(DependencyDiagram dependencyDiagram, SequenceDiagram sequenceDiagram);
    void generateOutputFile(String dst);
}
