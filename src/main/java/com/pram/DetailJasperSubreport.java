package com.pram;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.stream.IntStream;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

public class DetailJasperSubreport {

    /**
     * <p>Constructor for DetailJasperSubreport.</p>
     */
    public DetailJasperSubreport() {
        build();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        new DetailJasperSubreport();
    }

    private void build() {
        try {
            SubreportBuilder subreport = cmp.subreport(getJasperSubreport()).setDataSource(new SubreportDataSourceExpression());

            report().title(Templates.createTitleComponent("DetailJasperSubreport"))
                    .detail(subreport, cmp.verticalGap(20))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException | JRException e) {
            e.printStackTrace();
        }
    }

    private JRDataSource createDataSource() {
        return new JREmptyDataSource(4);
    }

    private JasperReport getJasperSubreport() throws JRException {
        InputStream is = DetailJasperSubreport.class.getResourceAsStream("/subreport.jrxml");
        return JasperCompileManager.compileReport(is);
    }

    private class SubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {
        private static final long serialVersionUID = 1L;

        @Override
        public JRDataSource evaluate(ReportParameters reportParameters) {
            DRDataSource dataSource = new DRDataSource("item", "quantity", "unitprice");
            IntStream.range(0, 5).forEach(i -> dataSource.add("Book", (int) (Math.random() * 10) + 1, BigDecimal.valueOf(Math.random() * 100 + 1)));
            return dataSource;
        }
    }
}