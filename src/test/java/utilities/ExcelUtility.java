package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility implements AutoCloseable { //

    public FileInputStream fi; //
    public FileOutputStream fo; //
    public XSSFWorkbook workbook; //
    public XSSFSheet sheet; //
    public XSSFRow row; //
    public XSSFCell cell; //
    public CellStyle cellstyle; //
    String path; //

    public ExcelUtility(String path) { //
        this.path = path;
    }

    public int getRowCount(String sheetName) throws IOException { //
        fi = new FileInputStream(path); //
        workbook = new XSSFWorkbook(fi); //
        sheet = workbook.getSheet(sheetName); //
        int rowCount = sheet.getLastRowNum(); //
        workbook.close(); //
        fi.close(); //
        return rowCount; //
    }

    public int getCellCount(String sheetName, int rownum) throws IOException { //
        fi = new FileInputStream(path);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        int cellCount = row.getLastCellNum(); // Inferred
        workbook.close();
        fi.close();
        return cellCount; // Inferred
    }

    public String getCellData(String sheetName, int rownum, int colnum) throws IOException {
        // Snippet showing core logic
        String data = ""; 
        try {
            DataFormatter formatter = new DataFormatter();
            data = formatter.formatCellValue(cell); //
        }
        catch(Exception e) {
            // catch(Exception e) {}
        }
        // workbook.close(); fi.close(); return data; // Inferred closing from context
        return data;
    }

    public void setCellData(String sheetName, int rownum, int colnum, String data) throws IOException { //
        File xlfile = new File(path); //

        // If file not exists then create new file
        if (!xlfile.exists()) { //
            workbook = new XSSFWorkbook(); //
            fo = new FileOutputStream(path); //
            workbook.write(fo); //
        }

        fi = new FileInputStream(path); //
        workbook = new XSSFWorkbook(fi); //

        // If sheet not exists then create new Sheet
        if (workbook.getSheetIndex(sheetName) == -1) { //
            sheet = workbook.createSheet(sheetName); //
        } else {
            sheet = workbook.getSheet(sheetName); //
        }

        // If row not exists then create new Row
        if (sheet.getRow(rownum) == null) { //
            row = sheet.createRow(rownum); //
        } else {
            row = sheet.getRow(rownum); //
        }

        cell = row.createCell(colnum); //
        cell.setCellValue(data); //

        fo = new FileOutputStream(path);
        workbook.write(fo);
        // workbook.close(); fi.close(); // Inferred
        fo.close(); //
    }

    public void fillRedColor(String sheetName, int rownum, int colnum) throws IOException { //
        fi = new FileInputStream(path); //
        // workbook = new XSSFWorkbook(fi); sheet = workbook.getSheet(sheetName); row = sheet.getRow(rownum); // Inferred
        cell = row.getCell(colnum); //

        cellstyle = workbook.createCellStyle(); //
        cellstyle.setFillForegroundColor(IndexedColors.RED.getIndex()); //
        cellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); //

        cell.setCellStyle(cellstyle); //

        // fo = new FileOutputStream(path); workbook.write(fo); workbook.close(); fi.close(); fo.close(); // Inferred
        workbook.write(fo); //
        fo.close(); //
    }

    @Override
    public void close() throws IOException {
        // Implementation for AutoCloseable is not fully visible
    }
}