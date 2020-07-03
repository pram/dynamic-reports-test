package com.pram;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

public class HtmlReport implements SimpleReport {

    public HtmlReport() {
        this(true);
    }

    public HtmlReport(boolean show) {
        if (show) {
            build();
        }
    }

    public static void main(String[] args) {
        new HtmlReport();
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

        HorizontalListBuilder title = cmp.horizontalList().add(cmp.text("HTML Test").setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                .newRow()
                .add(cmp.filler().setStyle(stl.style().setTopBorder(stl.pen2Point()))).setFixedHeight(10);

        FieldBuilder<String> narrativeField = field("narrative", type.stringType());

        JasperReportBuilder report = report();

        report.setDataSource(createDataSource());
        report.title(
                title,
                cmp.verticalGap(20),
                cmp.text(narrativeField).setMarkup(Markup.HTML));

        return report;
//                .title(tit)// shows report title
//                .pageFooter(cmp.pageXofY())// shows number of page at page footer
//                .setDataSource(createDataSource());
    }

    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("narrative");
        dataSource.add("<table><tr><td><b>John</b></td><td>Doe</td></tr><tr><td>Jane</td><td>Doe</td></tr></table>");
        return dataSource;
    }
}