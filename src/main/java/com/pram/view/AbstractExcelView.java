package com.pram.view;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public abstract class AbstractExcelView extends AbstractView {

    public AbstractExcelView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        ByteArrayOutputStream baos = createTemporaryOutputStream();

        buildExcelDocument(model, new JasperReportBuilder(), request, response);

        // Flush to HTTP response.
        writeToResponse(response, baos);
    }

    protected abstract void buildExcelDocument(Map<String, Object> model, JasperReportBuilder report,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception;
}

