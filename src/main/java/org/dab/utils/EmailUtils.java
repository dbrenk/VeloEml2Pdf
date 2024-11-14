package org.dab.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EmailUtils {

    public static final Pattern HTML_META_CHARSET_REGEX = Pattern.compile("(<meta(?!\\s*(?:name|value)\\s*=)[^>]*?charset\\s*=[\\s\"']*)([^\\s\"'/>]*)", Pattern.DOTALL);

    /**
     * reads in a byte[] of an .eml file and returns a javax MimeMessage object
     * @param emlBytes
     * @return MimeMessage
     */
    public static MimeMessage readEmlByteArray(byte[] emlBytes){
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(emlBytes);
            Properties properties = new Properties();
            Session mailSession = Session.getDefaultInstance(properties, null);
            MimeMessage message = new MimeMessage(mailSession, bais);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves the body text of an email from a MimeMessage.
     *
     * @param message the MimeMessage to extract the body from
     * @return the body text of the email, or an empty string if no text is found
     * @throws MessagingException if there's an issue accessing the message
     * @throws IOException if there's an issue reading the message content
     */
    public static String getTextFromMimeMessage(MimeMessage message) throws MessagingException, IOException {
        Object content = message.getContent();

        if (content instanceof String) {
            // If the content is simple text, return it directly
            return (String) content;
        } else if (content instanceof Multipart) {
            // If the content is multipart, extract the text
            return getTextFromMultipart((Multipart) content);
        }

        return "";
    }

    /**
     * Recursively retrieves text from a Multipart object.
     *
     * @param multipart the Multipart object to extract text from
     * @return the extracted text
     * @throws MessagingException if there's an issue accessing the multipart content
     * @throws IOException if there's an issue reading the content
     */
    private static String getTextFromMultipart(Multipart multipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);

            if (part.isMimeType("text/plain")) {
                // If the part is plain text, add it to the result
                result.append(part.getContent().toString());
            } else if (part.isMimeType("text/html")) {
                // If the part is HTML, you may prefer to extract it or skip it, depending on your needs
                // Uncomment the line below to prioritize HTML content over plain text
                return part.getContent().toString();
            } else if (part.getContent() instanceof Multipart) {
                // If the part itself is multipart, recurse into it
                result.append(getTextFromMultipart((Multipart) part.getContent()));
            }
        }

        return result.toString();
    }

    public static String getEmailFrom(MimeMessage mimeMessage) throws MessagingException {
        String from = Arrays.stream(mimeMessage.getFrom())
                .filter(address -> address instanceof InternetAddress)
                .map(address -> ((InternetAddress) address).toString())
                .collect(Collectors.joining("; "));
        return from;
    }

    public static String getEmailTo(MimeMessage mimeMessage) throws MessagingException {
        Address[] recipientsTo = mimeMessage.getRecipients(Message.RecipientType.TO);
        String to = Arrays.stream(recipientsTo)
                .filter(address -> address instanceof InternetAddress)
                .map(address -> ((InternetAddress) address).toString())
                .collect(Collectors.joining("; "));
        return to;
    }

    public static String getEmailCC(MimeMessage mimeMessage) throws MessagingException {
        Address[] recipientsTo = mimeMessage.getRecipients(Message.RecipientType.CC);
        if(null == recipientsTo || recipientsTo.length == 0){
            return "";
        }
        String cc = Arrays.stream(recipientsTo)
                .filter(address -> address instanceof InternetAddress)
                .map(address -> ((InternetAddress) address).toString())
                .collect(Collectors.joining("; "));
        return cc;
    }

}

