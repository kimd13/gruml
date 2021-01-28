package output;

import dependency.DependencyDiagram;
import sequence.SequenceDiagram;

public interface OutputGenerator {
    void initialize(DependencyDiagram dependencyDiagram, SequenceDiagram sequenceDiagram);
    void generateOutputFile(String dst);
}
