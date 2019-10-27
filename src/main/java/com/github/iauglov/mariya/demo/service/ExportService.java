package com.github.iauglov.mariya.demo.service;

import com.github.iauglov.mariya.demo.models.Purchase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExportService {

    public File exportToExcel(Set<Purchase> purchases, String periodInfo) {

        String[] columns = {"Товар", "Количество", "Цена за шт.", "Всего"};
        try {
            try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                Sheet sheet = workbook.createSheet("Покупки");

                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setColor(IndexedColors.BLUE.getIndex());

                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);

                Row headerRow = sheet.createRow(0);

                for (int col = 0; col < columns.length; col++) {
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(columns[col]);
                    cell.setCellStyle(headerCellStyle);
                }


                int rowIdx = 1;
                for (Purchase purchase : purchases) {
                    Row row = sheet.createRow(rowIdx++);

                    row.createCell(0).setCellValue(purchase.getName());
                    row.createCell(1).setCellValue(purchase.getCount());
                    row.createCell(2).setCellValue(purchase.getCost().toPlainString());
                    row.createCell(3).setCellValue(purchase.getCost().multiply(new BigDecimal(purchase.getCount())).toPlainString());
                }
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(periodInfo);

                for (int i = 0; i < 5; i++) {
                    sheet.autoSizeColumn(i);
                }

                File outputFile = new File("export.xlsx");

                workbook.write(out);
                try (FileOutputStream stream = new FileOutputStream(outputFile)) {
                    stream.write(out.toByteArray());
                }
                return outputFile;
            }
        } catch (IOException e) {
            log.error("Exception on export", e);
            return null;
        }
    }


}
