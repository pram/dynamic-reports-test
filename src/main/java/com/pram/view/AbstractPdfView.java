package com.pram.view;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public abstract class AbstractPdfView extends AbstractView {

    /**
     * This constructor sets the appropriate content type "application/pdf".
     * Note that IE won't take much notice of this, but there's not a lot we
     * can do about this. Generated documents should have a ".pdf" extension.
     */
    public AbstractPdfView() {
        setContentType("application/pdf");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception  {

        ByteArrayOutputStream baos = createTemporaryOutputStream();

        buildPdfDocument(model, new JasperReportBuilder(), request, response);

        // Flush to HTTP response.
        writeToResponse(response, baos);
    }

    protected abstract void buildPdfDocument(Map<String, Object> model, JasperReportBuilder report,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception;
}

