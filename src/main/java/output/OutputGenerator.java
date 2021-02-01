package output;

import static_section.DependencyDiagram;
import dynamic_section.SequenceDiagram;

public interface OutputGenerator {
    void initialize(DependencyDiagram dependencyDiagram, SequenceDiagram sequenceDiagram);
    void generateOutputFile(String dst);
}
