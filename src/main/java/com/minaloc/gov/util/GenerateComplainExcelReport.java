package com.minaloc.gov.util;

import com.minaloc.gov.domain.Complain;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GenerateComplainExcelReport {

    private List<Complain> complains;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public GenerateComplainExcelReport(List<Complain> complains) {
        this.complains = complains;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Complain");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Id", style);
        createCell(row, 1, "Ikibazo", style);
        createCell(row, 2, "Icyakozwe", style);
        createCell(row, 3, "Icyakorwa", style);
        createCell(row, 4, "Umwanzuro", style);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Complain complain : complains) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, complain.getId(), style);
            createCell(row, columnCount++, complain.getIkibazo(), style);
            createCell(row, columnCount++, complain.getIcyakorwa(), style);
            createCell(row, columnCount++, complain.getIcyakorwa(), style);
            createCell(row, columnCount++, complain.getUmwanzuro(), style);
        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
