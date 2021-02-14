package static_section.diagram.struct;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DependencyWorkBook {

    private String workbookName;
    private String spreadsheetName;
    private XSSFWorkbook workbook = null;
    private XSSFSheet spreadsheet = null;
    private int rowIndex = 0;

    public void createWorkBook(String workbookName){
        this.workbookName = workbookName;
        workbook = new XSSFWorkbook();
    }

    public void createSpreadsheet(String spreadsheetName){
        this.spreadsheetName = spreadsheetName;
        spreadsheet = workbook.createSheet(spreadsheetName);
    }

    public void closeWorkBook() throws IOException {
        FileOutputStream out = new FileOutputStream(new File(workbookName + ".xlsx"));
        workbook.write(out);
        workbook.close();
    }

    public void addRow(ModuleRow moduleRow){
        Row row = spreadsheet.createRow(rowIndex);
        Cell cell = row.createCell(moduleRow.getColumnIndex());
        cell.setCellValue(moduleRow.getValue());
        rowIndex++;
    }
}
