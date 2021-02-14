package static_section.diagram;

import static_section.diagram.struct.DependencyWorkBook;
import static_section.diagram.struct.ModuleRow;
import static_section.extractor.Extractor;
import static_section.extractor.ExtractorImpl;

public class DependencyDiagramImpl implements DependencyDiagram {

    private final Extractor extractor = new ExtractorImpl();

    @Override
    public void create(String srcPath) {
        extractor.extractAllInfo(srcPath);
        createDependencyWorkbook();
    }

    private void createDependencyWorkbook() {
        DependencyWorkBook dependencyWorkBook = new DependencyWorkBook();
        dependencyWorkBook.createWorkBook("testing");
        dependencyWorkBook.createSpreadsheet("dependency");
        for (String objectName : extractor.getAllObjects()) {
            ModuleRow object = new ModuleRow(ModuleRow.ModuleRowType.OBJECT, objectName);
            dependencyWorkBook.addRow(object);
            for (String methodName : extractor.getAllObjectMethods(objectName)) {
                ModuleRow method = new ModuleRow(ModuleRow.ModuleRowType.METHOD, methodName);
                dependencyWorkBook.addRow(method);
            }
        }
        try {
            dependencyWorkBook.closeWorkBook();
        } catch (Exception e) {
            System.out.println("Oops");
        }
    }
}
