package com.pram;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

public class SimpleReportStep01 implements SimpleReport {

    public SimpleReportStep01() {
        this(true);
    }

    public SimpleReportStep01(boolean show) {
        if (show) {
            build();
        }
    }

    public static void main(String[] args) {
        new SimpleReportStep01();
    }

    private void build() {
        try {
            JasperReportBuilder jasperReportBuilder = generateReport();// set datasource

            jasperReportBuilder.show(); // create and show report
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JasperReportBuilder generateReport() {
        return report()// create new report design
                // add columns
                .columns(
                        col.column("Item", "item", type.stringType()),
                        col.column("Quantity", "quantity", type.integerType()),
                        col.column("Unit price", "unitprice", type.bigDecimalType())
                )
                .title(cmp.text("Getting started"))// shows report title
                .pageFooter(cmp.pageXofY())// shows number of page at page footer
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
}