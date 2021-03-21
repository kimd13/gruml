package static_section.diagram.struct;

import java.util.HashMap;

/**
 * Class holds information about Dependency Diagram sections
 * Each section contains a class name followed by its methods
 * e.g. SomeClass
 *          method1()
 *          method2()
 */
public class DependencyDiagramObjectSection {

    private final int dependencyChannelAssignment;
    private final int beginningIndex;
    private int endingIndex;
    private final HashMap<String, Integer> methodIndices = new HashMap<>();

    public DependencyDiagramObjectSection(int dependencyChannelAssignment, int beginningIndex){
        this.dependencyChannelAssignment = dependencyChannelAssignment;
        this.beginningIndex = beginningIndex;
        this.endingIndex = beginningIndex;
    }

    public int getDependencyChannelAssignment() {
        return dependencyChannelAssignment;
    }

    public int getBeginningIndex() {
        return beginningIndex;
    }

    public int getEndingIndex() {
        return endingIndex;
    }

    public int getIndexOfMethod(String methodName){
        return methodIndices.get(methodName);
    }

    // TODO: Must guarantee method names are unique, currently methods are given without args (overloading issues)
    public void inputMethod(String methodName, int index){
        methodIndices.put(methodName, index);
        endingIndex++;
    }
}
