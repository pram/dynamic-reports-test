package com.pram;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.PercentageColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.Color;
import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.cnd;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.grid;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.sbt;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

public class SimpleReportStep12 implements SimpleReport {

    /**
     * <p>Constructor for SimpleReport_Step12.</p>
     * @param imageName - path to image to embed in report
     */
    public SimpleReportStep12(String imageName) {
        build(imageName);
    }

    public SimpleReportStep12(boolean show) {
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            new SimpleReportStep12("/images/logo2.png");
        } else {
            new SimpleReportStep12("/images/logo.png");
        }
    }

    private void build(String imageName) {
        try {
            generateReport().show(); // create and show report
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    public JasperReportBuilder generateReport() {
        CurrencyType currencyType = new CurrencyType();

        StyleBuilder boldStyle = stl.style().bold();
        StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);
        StyleBuilder titleStyle = stl.style(boldCenteredStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE).setFontSize(15);

        // title, field name data type
        TextColumnBuilder<String> itemColumn = col.column("Item", "item", type.stringType()).setStyle(boldStyle);
        TextColumnBuilder<Integer> quantityColumn = col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<BigDecimal> unitPriceColumn = col.column("Unit price", "unitprice", currencyType);
        // price = unitPrice * quantity
        TextColumnBuilder<BigDecimal> priceColumn = unitPriceColumn.multiply(quantityColumn).setTitle("Price").setDataType(currencyType);
        PercentageColumnBuilder pricePercColumn = col.percentageColumn("Price %", priceColumn);
        TextColumnBuilder<Integer> rowNumberColumn = col.reportRowNumberColumn("No.")
                // sets the fixed width of a column, width = 2 * character width
                .setFixedColumns(2).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        Bar3DChartBuilder itemChart = cht.bar3DChart().setTitle("Sales by item").setCategory(itemColumn).addSerie(cht.serie(unitPriceColumn), cht.serie(priceColumn));
        Bar3DChartBuilder itemChart2 = cht.bar3DChart().setTitle("Sales by item").setCategory(itemColumn).setUseSeriesAsCategory(true).addSerie(cht.serie(unitPriceColumn), cht.serie(priceColumn));
        ColumnGroupBuilder itemGroup = grp.group(itemColumn);
        itemGroup.setPrintSubtotalsWhenExpression(exp.printWhenGroupHasMoreThanOneRow(itemGroup));

        ConditionalStyleBuilder condition1 = stl.conditionalStyle(cnd.greater(priceColumn, 150)).setBackgroundColor(new Color(210, 255, 210));
        ConditionalStyleBuilder condition2 = stl.conditionalStyle(cnd.smaller(priceColumn, 30)).setBackgroundColor(new Color(255, 210, 210));
        ConditionalStyleBuilder condition3 = stl.conditionalStyle(cnd.greater(priceColumn, 200)).setBackgroundColor(new Color(0, 190, 0)).bold();
        ConditionalStyleBuilder condition4 = stl.conditionalStyle(cnd.smaller(priceColumn, 20)).setBackgroundColor(new Color(190, 0, 0)).bold();
        StyleBuilder priceStyle = stl.style().conditionalStyles(condition3, condition4);
        priceColumn.setStyle(priceStyle);

        return assembleReport("/images/logo.png", boldStyle, boldCenteredStyle, columnTitleStyle, titleStyle, itemColumn, quantityColumn, unitPriceColumn, priceColumn, pricePercColumn, rowNumberColumn, itemChart, itemChart2, itemGroup, condition1, condition2);// set datasource

    }

    private JasperReportBuilder assembleReport(String imageName, StyleBuilder boldStyle, StyleBuilder boldCenteredStyle, StyleBuilder columnTitleStyle, StyleBuilder titleStyle, TextColumnBuilder<String> itemColumn, TextColumnBuilder<Integer> quantityColumn, TextColumnBuilder<BigDecimal> unitPriceColumn, TextColumnBuilder<BigDecimal> priceColumn, PercentageColumnBuilder pricePercColumn, TextColumnBuilder<Integer> rowNumberColumn, Bar3DChartBuilder itemChart, Bar3DChartBuilder itemChart2, ColumnGroupBuilder itemGroup, ConditionalStyleBuilder condition1, ConditionalStyleBuilder condition2) {
        return report()// create new report design
                .setColumnTitleStyle(columnTitleStyle)
                .setSubtotalStyle(boldStyle)
                .highlightDetailEvenRows()
                .columns(// add columns
                         rowNumberColumn, itemColumn, quantityColumn, unitPriceColumn, priceColumn, pricePercColumn)
                .columnGrid(rowNumberColumn, quantityColumn, unitPriceColumn, grid.verticalColumnGridList(priceColumn, pricePercColumn))
                .groupBy(itemGroup)
                .subtotalsAtSummary(sbt.sum(unitPriceColumn), sbt.sum(priceColumn))
                .subtotalsAtFirstGroupFooter(sbt.sum(unitPriceColumn), sbt.sum(priceColumn))
                .detailRowHighlighters(condition1, condition2)
                .title(// shows report title
                       cmp.horizontalList()
                          .add(cmp.image(Templates.class.getResource(imageName)).setFixedDimension(80, 80),
                               cmp.text("DynamicReports").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
                               cmp.text("Getting started").setStyle(titleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
                          .newRow()
                          .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point())).setFixedHeight(10)))
                .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))// shows number of page at page footer
                .summary(cmp.horizontalList(itemChart, itemChart2))
                .setDataSource(createDataSource());
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
        dataSource.add("Notebook", 1, BigDecimal.valueOf(500));
        dataSource.add("DVD", 5, BigDecimal.valueOf(30));
        dataSource.add("DVD", 1, BigDecimal.valueOf(28));
        dataSource.add("DVD", 5, BigDecimal.valueOf(32));
        dataSource.add("Book", 3, BigDecimal.valueOf(11));
        dataSource.add("Book", 1, BigDecimal.valueOf(15));
        dataSource.add("Book", 5, BigDecimal.valueOf(10));
        dataSource.add("Book", 8, BigDecimal.valueOf(9));
        return dataSource;
    }

    private static class CurrencyType extends BigDecimalType {
        private static final long serialVersionUID = 1L;

        @Override
        public String getPattern() {
            return "$ #,###.00";
        }
    }
}