package static_section.diagram.struct;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DependencyWorkBook {

    private String workbookName;
    private String spreadsheetName;
    private XSSFWorkbook workbook = null;
    private XSSFSheet spreadsheet = null;
    private int rowIndex = 0;
    private int columnIndex = 0;

    public void createWorkBook(String workbookName){
        this.workbookName = workbookName;
        workbook = new XSSFWorkbook();
    }

    public void createSpreadsheet(String spreadsheetName){
        this.spreadsheetName = spreadsheetName;
        spreadsheet = workbook.createSheet(spreadsheetName);
    }

    public void closeWorkBook() throws IOException {
        FileOutputStream out = new FileOutputStream(workbookName + ".xlsx");
        workbook.write(out);
        workbook.close();
    }

    public void setDependencyChannels(int numberOfChannels, int lenOfChannels){
        IntStream.range(0, lenOfChannels).forEachOrdered( dpRowIndex -> {
            Row row = spreadsheet.createRow(dpRowIndex);
            IntStream.range(0, numberOfChannels).forEachOrdered( dpColumnIndex -> {
                Cell cell = createCell(row, "", dpColumnIndex);
                cell.setCellStyle(getLeftBorderOutlinedStyle());
            });
        });
        columnIndex += numberOfChannels;
    }

    private CellStyle getLeftBorderOutlinedStyle(){
        CellStyle style = workbook.createCellStyle();
        style.setBorderLeft(BorderStyle.MEDIUM);
        return style;
    }

    public void addRow(DependencyRow dependencyRow){
        Row row = spreadsheet.getRow(rowIndex);
        List<String> values = dependencyRow.getRow();
        int startColumn = columnIndex;
        for (int index = 0; index < values.size(); index++){
            Cell cell = createCell(row, values.get(index), startColumn);
            if (isIndexOfDependenceIcons(index)){
                cell.setCellStyle(getRedColoredCellStyle());
            } else if (isIndexOfNameAndIsObject(index, dependencyRow)){
                cell.setCellStyle(getBoldedCellStyle());
            }
            startColumn++;
        }
        rowIndex++;
    }

    private boolean isIndexOfNameAndIsObject(int index, DependencyRow dependencyRow){
        return index == DependencyRow.NAME_INDEX && dependencyRow.isObject();
    }

    private boolean isIndexOfDependenceIcons(int index){
        return index == DependencyRow.IS_INHERITED_FROM_INDEX || index == DependencyRow.IS_USED_BY_ANOTHER_OBJECT_INDEX;
    }

    private Cell createCell(Row row, String value, int column){
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        return cell;
    }

    private CellStyle getBoldedCellStyle(){
        CellStyle style = workbook.createCellStyle();
        style.setFont(getBoldFont());
        return style;
    }

    private CellStyle getRedColoredCellStyle(){
        CellStyle style = workbook.createCellStyle();
        style.setFont(getRedColoredFont());
        return style;
    }

    private Font getBoldFont(){
        XSSFFont font= workbook.createFont();
        font.setBold(true);
        return font;
    }

    private Font getRedColoredFont(){
        XSSFFont font= workbook.createFont();
        font.setColor(XSSFFont.COLOR_RED);
        return font;
    }
}
