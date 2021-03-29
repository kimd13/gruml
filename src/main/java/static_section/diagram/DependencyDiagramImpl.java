package static_section.diagram;

import static_section.diagram.struct.DependencyDiagramObjectSection;
import static_section.diagram.struct.DependencyRow;
import static_section.diagram.struct.DependencyWorkBook;
import static_section.extractor.Extractor;
import static_section.extractor.ExtractorImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyDiagramImpl implements DependencyDiagram {

    private final Extractor extractor = new ExtractorImpl();
    private HashMap<String, DependencyDiagramObjectSection> sections = new HashMap<>();

    @Override
    public void create(String srcPath) {
        extractor.extractAllInfo(srcPath);
        DependencyWorkBook dependencyWorkBook = createDependencyWorkbook();
        populateDependencyWorkbook(dependencyWorkBook);
    }

    private DependencyWorkBook createDependencyWorkbook() {
        DependencyWorkBook dependencyWorkBook = new DependencyWorkBook();
        dependencyWorkBook.createWorkBook("testing");
        dependencyWorkBook.createSpreadsheet("dependency");
        return dependencyWorkBook;
    }

    private void populateDependencyWorkbook(DependencyWorkBook dependencyWorkBook) {
        populateSections(dependencyWorkBook);
        markDependencyChannels(dependencyWorkBook);
        closeDependencyWorkbook(dependencyWorkBook);
    }

    private void populateSections(DependencyWorkBook dependencyWorkBook) {
        // the number of objects indicates how many dependency channels (columns) are allocated
        int numberOfObjects = extractor.getNumberOfObjects();
        int sumOfMethodsAndObjects = numberOfObjects + extractor.getNumberOfMethods();
        // numberOfObjects sets how many columns, sumOfMethodsAndObjects sets how many rows for each column
        dependencyWorkBook.setDependencyChannels(numberOfObjects, sumOfMethodsAndObjects);

        for (String objectName : extractor.getAllObjects()) {
            // as DependencyDiagramObjectSections are created, lower the numberOfObjects to match assignment
            numberOfObjects--;
            DependencyDiagramObjectSection section = new DependencyDiagramObjectSection(numberOfObjects, dependencyWorkBook.getRowIndex());
            sections.put(objectName, section);
            addObjectDependencyRow(dependencyWorkBook, objectName);

            for (String methodName : extractor.getAllObjectMethods(objectName)) {
                section.inputMethod(methodName, dependencyWorkBook.getRowIndex());
                addMethodDependencyRow(dependencyWorkBook, methodName);
            }
        }
    }

    private void markDependencyChannels(DependencyWorkBook dependencyWorkBook) {
        for (Map.Entry<String, DependencyDiagramObjectSection> entry : sections.entrySet()) {

            String objectName = entry.getKey();
            List<String> children = extractor.getChildren(objectName);
            Set<String> usedByObjects = extractor.getUsedByObjects(objectName);
            DependencyDiagramObjectSection section = entry.getValue();

            // marks assignment
            dependencyWorkBook.markDependencyChannel(DependencyWorkBook.DependencyChannelMarker.ASSIGNMENT,
                    section.getBeginningIndex(), section.getDependencyChannelAssignment());

            // marks use relationships
            for (String usedBy : usedByObjects) {
                int usedByBeginningIndex = sections.get(usedBy).getBeginningIndex();
                dependencyWorkBook.markDependencyChannel(DependencyWorkBook.DependencyChannelMarker.USES,
                        usedByBeginningIndex, section.getDependencyChannelAssignment());
            }

            // marks inheritance
            for (String child : children) {
                int childBeginningIndex = sections.get(child).getBeginningIndex();
                dependencyWorkBook.markDependencyChannel(DependencyWorkBook.DependencyChannelMarker.INHERITS,
                        childBeginningIndex, section.getDependencyChannelAssignment());
            }
        }
    }

    private void addObjectDependencyRow(DependencyWorkBook dependencyWorkBook, String objectName) {
        DependencyRow object = new DependencyRow(DependencyRow.ModuleRowType.OBJECT, objectName,
                extractor.isObjectInheritedFrom(objectName), extractor.isObjectUsedByAnother(objectName));
        dependencyWorkBook.addRow(object);
    }

    private void addMethodDependencyRow(DependencyWorkBook dependencyWorkBook, String methodName) {
        DependencyRow method = new DependencyRow(DependencyRow.ModuleRowType.METHOD, methodName,
                false, false);
        dependencyWorkBook.addRow(method);
    }

    private void closeDependencyWorkbook(DependencyWorkBook dependencyWorkBook) {
        try {
            dependencyWorkBook.closeWorkBook();
        } catch (Exception e) {
            System.out.println("Oops");
        }
    }
}
