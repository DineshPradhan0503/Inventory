package com.inventory.management.services;

import com.inventory.management.models.Product;
import com.inventory.management.models.Sale;
import com.inventory.management.payload.response.StockReportItem;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExportService {

    public byte[] exportStockExcel(List<StockReportItem> items) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Stock");
            int rowIdx = 0;
            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("Product ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Stock");
            header.createCell(4).setCellValue("Threshold");
            header.createCell(5).setCellValue("Low Stock");

            for (StockReportItem item : items) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getProductId());
                row.createCell(1).setCellValue(item.getName());
                row.createCell(2).setCellValue(item.getCategory());
                row.createCell(3).setCellValue(item.getStockQuantity());
                row.createCell(4).setCellValue(item.getThreshold());
                row.createCell(5).setCellValue(item.isLowStock());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export stock excel", e);
        }
    }

    public byte[] exportStockPdf(List<StockReportItem> items) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Stock Report"));
            PdfPTable table = new PdfPTable(5);
            table.addCell("Product ID");
            table.addCell("Name");
            table.addCell("Category");
            table.addCell("Stock");
            table.addCell("Threshold");
            for (StockReportItem i : items) {
                table.addCell(i.getProductId());
                table.addCell(i.getName());
                table.addCell(i.getCategory());
                table.addCell(String.valueOf(i.getStockQuantity()));
                table.addCell(String.valueOf(i.getThreshold()));
            }
            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export stock pdf", e);
        }
    }

    public byte[] exportSalesExcel(List<Sale> sales) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Sales");
            int rowIdx = 0;
            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("Sale ID");
            header.createCell(1).setCellValue("Product ID");
            header.createCell(2).setCellValue("Qty");
            header.createCell(3).setCellValue("User ID");

            for (Sale s : sales) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(String.valueOf(s.getId()));
                row.createCell(1).setCellValue(s.getProductId());
                row.createCell(2).setCellValue(s.getQuantitySold());
                row.createCell(3).setCellValue(s.getUserId());
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export sales excel", e);
        }
    }
    public byte[] exportSalesPdf(List<Sale> sales) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Sales Report"));
            PdfPTable table = new PdfPTable(4);
            table.addCell("Sale ID");
            table.addCell("Product ID");
            table.addCell("Qty");
            table.addCell("User ID");

            for (Sale s : sales) {
                table.addCell(String.valueOf(s.getId()));
                table.addCell(s.getProductId());
                table.addCell(String.valueOf(s.getQuantitySold()));
                table.addCell(s.getUserId());
            }
            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export sales pdf", e);
        }
    }
}

