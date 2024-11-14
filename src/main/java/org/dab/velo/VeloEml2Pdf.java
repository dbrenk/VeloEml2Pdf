package org.dab.velo;

import com.lowagie.text.DocumentException;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.mail.*;
import javax.mail.internet.MimeMessage;

import org.dab.utils.EmailUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;


public class VeloEml2Pdf {

    /**
     *
     * @param velocityTemplateFilePath
     * @param inputFileEmlPath
     * @param outputFilePdfPath
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public static Boolean convertEml2Pdf(String velocityTemplateFilePath, String inputFileEmlPath, String outputFilePdfPath) throws IOException, MessagingException {
        byte[] inputEml = FileUtils.readFileToByteArray(new File(inputFileEmlPath));
        String velocityTemplateString = FileUtils.readFileToString(new File(velocityTemplateFilePath), Charsets.UTF_8);
        byte[] outputPdf = VeloEml2Pdf.convertEml2Pdf(velocityTemplateString, inputEml);
        File outputPdfFile = new File(outputFilePdfPath);
        FileUtils.writeByteArrayToFile(outputPdfFile, outputPdf);
        if(null != outputPdfFile && outputPdfFile.exists()){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param velocityTemplateString
     * @param inputFileEml
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public static byte[] convertEml2Pdf(String velocityTemplateString, byte[] inputFileEml) throws IOException, MessagingException {
        byte[] outputFilePdf = null;
        // checks
        MimeMessage mimeMessage = EmailUtils.readEmlByteArray(inputFileEml);
        if(null == mimeMessage){
            throw new MessagingException("could not read eml");
        }
        // build context with all information necessary for velocity
        VelocityContext context = new VelocityContext();
        context.put("emailFrom", StringEscapeUtils.escapeHtml4(EmailUtils.getEmailFrom(mimeMessage)));
        context.put("emailTo", StringEscapeUtils.escapeHtml4(EmailUtils.getEmailTo(mimeMessage)));
        context.put("emailCC", StringEscapeUtils.escapeHtml4(EmailUtils.getEmailCC(mimeMessage)));
        context.put("emailSubject", StringEscapeUtils.escapeHtml4(mimeMessage.getSubject()));
        context.put("emailText", StringEscapeUtils.escapeHtml4(EmailUtils.getTextFromMimeMessage(mimeMessage)));
        context.put("mimeMessage", mimeMessage);
        // combine the template and the context information into an HTML representation
        String htmlDocument = evaluateVelocityTemplateAndContext(velocityTemplateString, context);
        // render a pdf from the html
        outputFilePdf = printHtmlToPdf(htmlDocument);
        // return
        return outputFilePdf;
    }

    private static String evaluateVelocityTemplateAndContext(String velocityTemplateString, VelocityContext velocityContext) throws IOException, DocumentException {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();
        try (StringWriter writer = new StringWriter()) {
            velocityEngine.evaluate(velocityContext, writer, "pdf template", velocityTemplateString);
            return writer.toString();
        }
    }

    private static byte[] printHtmlToPdf(String htmlTemplate) throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlTemplate);
        renderer.layout();
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            renderer.createPDF(os);
            return os.toByteArray();
        }
    }

}