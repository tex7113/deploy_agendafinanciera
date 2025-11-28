package com.dalficc_technologies.agendafinanciera.infrastructure.excel;

import com.dalficc_technologies.agendafinanciera.domain.model.TransactionItem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionExcelGenerator {

    public byte[] generate(List<TransactionItem> transactions) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Transacciones");

        // ======== Estilos ========
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle amountStyle = workbook.createCellStyle();
        amountStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

        CellStyle incomeStyle = workbook.createCellStyle();
        incomeStyle.cloneStyleFrom(amountStyle);
        incomeStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        incomeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle expenseStyle = workbook.createCellStyle();
        expenseStyle.cloneStyleFrom(amountStyle);
        expenseStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        expenseStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // ======== Encabezados ========
        String[] columns = {"ID", "Descripción", "Monto", "Categoría", "Fecha", "Tipo"};

        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int rowNum = 1;
        double totalIncome = 0;
        double totalExpense = 0;

        for (TransactionItem t : transactions) {
            Row row = sheet.createRow(rowNum++);

            boolean isIncome = t.getIsIncome();

            String date = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(t.getDate()),
                    ZoneId.systemDefault()
            ).format(formatter);

            row.createCell(0).setCellValue(t.getId());
            row.createCell(1).setCellValue(t.getDescription());

            // monto
            Cell amountCell = row.createCell(2);
            amountCell.setCellValue(t.getAmount());
            amountCell.setCellStyle(isIncome ? incomeStyle : expenseStyle);

            if (isIncome) totalIncome += t.getAmount();
            else totalExpense += t.getAmount();

            row.createCell(3).setCellValue(t.getCategory());
            row.createCell(4).setCellValue(date);
            row.createCell(5).setCellValue(isIncome ? "Ingreso" : "Gasto");
        }

        // Autosize
        for (int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);

        // ======== Totales ========
        int totalRow = rowNum + 1;

        Row r1 = sheet.createRow(totalRow);
        r1.createCell(1).setCellValue("Total Ingresos:");
        r1.createCell(2).setCellValue(totalIncome);

        Row r2 = sheet.createRow(totalRow + 1);
        r2.createCell(1).setCellValue("Total Gastos:");
        r2.createCell(2).setCellValue(totalExpense);

        Row r3 = sheet.createRow(totalRow + 2);
        r3.createCell(1).setCellValue("Balance Total:");
        r3.createCell(2).setCellValue(totalIncome - totalExpense);

        // ============================================================
        // HOJA 2: GRÁFICOS
        // ============================================================

        XSSFSheet chartSheet = workbook.createSheet("Gráficos");

        // --- Datos para gráfico de barras ---
        Row h = chartSheet.createRow(0);
        h.createCell(0).setCellValue("Tipo");
        h.createCell(1).setCellValue("Monto");

        Row i1 = chartSheet.createRow(1);
        i1.createCell(0).setCellValue("Ingresos");
        i1.createCell(1).setCellValue(totalIncome);

        Row i2 = chartSheet.createRow(2);
        i2.createCell(0).setCellValue("Gastos");
        i2.createCell(1).setCellValue(totalExpense);

        XSSFDrawing drawing = chartSheet.createDrawingPatriarch();
        XSSFClientAnchor anchorBar = drawing.createAnchor(0, 0, 0, 0, 0, 4, 8, 20);

        XSSFChart barChart = drawing.createChart(anchorBar);
        barChart.setTitleText("Ingresos vs Gastos");

        // 🔥 Ahora SÍ funciona con POI 5.3.x
        XDDFChartLegend legendBar = barChart.getOrAddLegend();
        legendBar.setPosition(LegendPosition.TOP_RIGHT);

        XDDFDataSource<String> cat = XDDFDataSourcesFactory.fromStringCellRange(
                chartSheet, new CellRangeAddress(1, 2, 0, 0)
        );

        XDDFNumericalDataSource<Double> vals = XDDFDataSourcesFactory.fromNumericCellRange(
                chartSheet, new CellRangeAddress(1, 2, 1, 1)
        );

        XDDFCategoryAxis xAxis = barChart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis yAxis = barChart.createValueAxis(AxisPosition.LEFT);

        XDDFBarChartData barData = (XDDFBarChartData)
                barChart.createData(ChartTypes.BAR, xAxis, yAxis);

        barData.addSeries(cat, vals);
        barChart.plot(barData);

        // --- Gráfico de pastel por categoría ---
        Map<String, Double> categoryTotals = transactions.stream()
                .collect(Collectors.groupingBy(TransactionItem::getCategory,
                        Collectors.summingDouble(TransactionItem::getAmount)));

        int startRow = 25;
        Row hc = chartSheet.createRow(startRow);
        hc.createCell(0).setCellValue("Categoría");
        hc.createCell(1).setCellValue("Monto");

        int r = startRow + 1;
        for (var e : categoryTotals.entrySet()) {
            Row rr = chartSheet.createRow(r++);
            rr.createCell(0).setCellValue(e.getKey());
            rr.createCell(1).setCellValue(e.getValue());
        }

        XSSFClientAnchor anchorPie = drawing.createAnchor(0, 0, 0, 0, 9, 4, 20, 20);

        XSSFChart pieChart = drawing.createChart(anchorPie);
        pieChart.setTitleText("Distribución por Categoría");

        XDDFChartLegend pieLegend = pieChart.getOrAddLegend();
        pieLegend.setPosition(LegendPosition.RIGHT);

        XDDFPieChartData pieData = (XDDFPieChartData)
                pieChart.createData(ChartTypes.PIE, null, null);

        XDDFDataSource<String> pieCat = XDDFDataSourcesFactory.fromStringCellRange(
                chartSheet, new CellRangeAddress(startRow + 1, r - 1, 0, 0)
        );

        XDDFNumericalDataSource<Double> pieVals = XDDFDataSourcesFactory.fromNumericCellRange(
                chartSheet, new CellRangeAddress(startRow + 1, r - 1, 1, 1)
        );

        pieData.addSeries(pieCat, pieVals);
        pieChart.plot(pieData);

        // ============================================================

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        workbook.write(output);
        workbook.close();
        return output.toByteArray();
    }
}
