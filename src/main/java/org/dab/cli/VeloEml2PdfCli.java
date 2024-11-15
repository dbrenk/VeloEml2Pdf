package org.dab.cli;
import org.apache.commons.cli.*;
import org.dab.velo.VeloEml2Pdf;
import java.io.File;

public class VeloEml2PdfCli {

    /**
     * java -jar veloEml2Pdf-1.0-SNAPSHOT.jar -velocityTemplateFilePath "src/main/resources/emlTemplateV2.html" -inputFileEmlPath "src/main/resources/example-eml-files/mail.eml" -outputFilePdfPath "Test_1.pdf"
     * @param args
     */
    public static void main(String[] args) {
        // Create the Options object to define required options
        Options options = new Options();

        // Define three required options (a, b, c)
        options.addOption(Option.builder("t")
                .longOpt("velocityTemplateFilePath")
                .desc("specify path to the velocity-template file")
                .hasArg()
                .required()
                .build());

        options.addOption(Option.builder("i")
                .longOpt("inputFileEmlPath")
                .desc("specify path to the input-eml file")
                .hasArg()
                .required()
                .build());

        options.addOption(Option.builder("o")
                .longOpt("outputFilePdfPath")
                .desc("specify output-path for the generated PDF")
                .hasArg()
                .required()
                .build());

        // Create a parser
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            // Parse the command-line arguments
            CommandLine cmd = parser.parse(options, args);

            // Retrieve the values of the required options
            String velocityTemplateFilePath = cmd.getOptionValue("t");
            String inputFileEmlPath = cmd.getOptionValue("i");
            String outputFilePdfPath = cmd.getOptionValue("o");

            // Use the parameters (for demonstration, just printing them)
            System.out.println("Parameter 1 velocityTemplateFilePath: " + velocityTemplateFilePath);
            System.out.println("Parameter 2 inputFileEmlPath: " + inputFileEmlPath);
            System.out.println("Parameter 3 outputFilePdfPath: " + outputFilePdfPath);

            Boolean ok = VeloEml2Pdf.convertEml2Pdf(velocityTemplateFilePath, inputFileEmlPath, outputFilePdfPath);
            System.out.println("conversion finished: " + ok);

            System.out.println("finished successfully");
            File out = new File(outputFilePdfPath);
            System.out.println("output.exists: " + out.exists() + " path: " + out.getAbsolutePath());

        } catch (Exception e) {
            // Print an error message if parsing fails
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            formatter.printHelp("VeloEml2PdfCli", options);
            System.exit(1);
        }
    }
}
