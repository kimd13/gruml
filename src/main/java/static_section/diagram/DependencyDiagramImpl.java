package static_section.diagram;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import static_section.diagram.struct.DependencyChannel;
import static_section.diagram.struct.DependencyWorkBook;
import static_section.diagram.struct.DependencyRow;
import static_section.extractor.Extractor;
import static_section.extractor.ExtractorImpl;

import java.util.List;
import java.util.stream.IntStream;

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
        int sumOfMethodsAndObjects = extractor.getNumberOfObjects() + extractor.getNumberOfMethods();
        List<String> allObjects = extractor.getAllObjects();

        //for (String object: allObjects) {
            //DependencyChannel dependencyChannel = new DependencyChannel(sumOfMethodsAndObjects, 0);
         //   dependencyWorkBook.addColumn(dependencyChannel);
       // }

        for (String objectName : allObjects) {
            DependencyRow object = new DependencyRow(DependencyRow.ModuleRowType.OBJECT, objectName,
                    extractor.isObjectInheritedFrom(objectName), extractor.isObjectUsedByAnother(objectName));
            dependencyWorkBook.addRow(object);
            for (String methodName : extractor.getAllObjectMethods(objectName)) {
                DependencyRow method = new DependencyRow(DependencyRow.ModuleRowType.METHOD, methodName,
                        false, false);
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
