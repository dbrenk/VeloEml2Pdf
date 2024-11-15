package org.dab.velo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class VeloEml2PdfTest {

    @Test
    void convertEml2Pdf_Paths() throws IOException, MessagingException {
        String velocityTemplateFilePath = "src/main/resources/emlTemplateV2.html";
        String inputFileEmlPath = "src/main/resources/example-eml-files/[ENTWICKLUNG] Frist-Benachrichtigung [Vertrag  Dienstwagen] [V-2020-00015] [].eml";
        String outputFilePdfPath = FilenameUtils.getBaseName(inputFileEmlPath) + "_" +  new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date()) + ".pdf";
        Boolean ok = VeloEml2Pdf.convertEml2Pdf(velocityTemplateFilePath, inputFileEmlPath, outputFilePdfPath);
        assertTrue(ok);
        File outputPdfFile = new File(outputFilePdfPath);
        assertNotNull(outputPdfFile);
        assertTrue(outputPdfFile.exists());
    }

    @Test
    void convertEml2Pdf_ByteArrays() throws IOException, MessagingException {
        String velocityTemplateFilePath = "src/main/resources/emlTemplateV2.html";
        String inputFileEmlPath = "src/main/resources/example-eml-files/[ENTWICKLUNG] Frist-Benachrichtigung [Vertrag  Dienstwagen] [V-2020-00015] [].eml";
        String outputFilePdfPath = FilenameUtils.getBaseName(inputFileEmlPath) + "_" +  new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date()) + ".pdf";
        byte[] inputEml = FileUtils.readFileToByteArray(new File(inputFileEmlPath));
        String velocityTemplateString = FileUtils.readFileToString(new File(velocityTemplateFilePath), StandardCharsets.UTF_8);
        byte[] outputPdf = VeloEml2Pdf.convertEml2Pdf(velocityTemplateString, inputEml);
        File outputPdfFile = new File(outputFilePdfPath);
        FileUtils.writeByteArrayToFile(outputPdfFile, outputPdf);
        assertNotNull(outputPdfFile);
        assertTrue(outputPdfFile.exists());
    }

    @Test
    void convertEml2Pdf_Paths_mail_eml() throws IOException, MessagingException {
        String velocityTemplateFilePath = "src/main/resources/emlTemplateV2.html";
        String inputFileEmlPath = "src/main/resources/example-eml-files/example_mail_200520013502983.eml";
        String outputFilePdfPath = FilenameUtils.getBaseName(inputFileEmlPath) + "_" +  new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date()) + ".pdf";
        Boolean ok = VeloEml2Pdf.convertEml2Pdf(velocityTemplateFilePath, inputFileEmlPath, outputFilePdfPath);
        assertTrue(ok);
        File outputPdfFile = new File(outputFilePdfPath);
        assertNotNull(outputPdfFile);
        assertTrue(outputPdfFile.exists());
    }
}