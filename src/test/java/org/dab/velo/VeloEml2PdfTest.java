package org.dab.velo;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class VeloEml2PdfTest {

    @Test
    void convertEml2Pdf_Paths() throws IOException, MessagingException {
        String velocityTemplateFilePath = "src/main/resources/emlTemplateV2.html";
        String inputFileEmlPath = "src/main/resources/Test_MailMitPDFAnhang_vonDICEI.eml";
        String outputFilePdfPath = FilenameUtils.getBaseName(inputFileEmlPath) + "_" +  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf";
        Boolean ok = VeloEml2Pdf.convertEml2Pdf(velocityTemplateFilePath, inputFileEmlPath, outputFilePdfPath);
        assert ok : "error";
    }

    @Test
    void convertEml2Pdf_ByteArrays() throws IOException, MessagingException {
        String velocityTemplateFilePath = "src/main/resources/emlTemplateV2.html";
        String inputFileEmlPath = "src/main/resources/Test_MailMitPDFAnhang_vonDICEI.eml";
        String outputFilePdfPath = FilenameUtils.getBaseName(inputFileEmlPath) + "_" +  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf";
        byte[] inputEml = FileUtils.readFileToByteArray(new File(inputFileEmlPath));
        String velocityTemplateString = FileUtils.readFileToString(new File(velocityTemplateFilePath), Charsets.UTF_8);
        byte[] outputPdf = VeloEml2Pdf.convertEml2Pdf(velocityTemplateString, inputEml);
        File outputPdfFile = new File(outputFilePdfPath);
        FileUtils.writeByteArrayToFile(outputPdfFile, outputPdf);
        assert (null != outputPdfFile && outputPdfFile.exists()) : "error";
    }

    @Test
    void convertEml2Pdf_Paths_mail_eml() throws IOException, MessagingException {
        String velocityTemplateFilePath = "src/main/resources/emlTemplateV2.html";
        String inputFileEmlPath = "src/main/resources/mail.eml";
        String outputFilePdfPath = FilenameUtils.getBaseName(inputFileEmlPath) + "_" +  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf";
        Boolean ok = VeloEml2Pdf.convertEml2Pdf(velocityTemplateFilePath, inputFileEmlPath, outputFilePdfPath);
        assert ok : "error";
    }
}