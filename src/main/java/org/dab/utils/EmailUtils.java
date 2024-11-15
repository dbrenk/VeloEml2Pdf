package org.dab.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class EmailUtils {

    private EmailUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Reads in a byte[] of an .eml file and returns a javax MimeMessage object.
     * @param emlBytes
     * @return MimeMessage
     */
    public static MimeMessage readEmlByteArray(byte[] emlBytes){
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(emlBytes);
            Properties properties = new Properties();
            Session mailSession = Session.getDefaultInstance(properties, null);
            return new MimeMessage(mailSession, bais);
        } catch (Exception e) {
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
        return Arrays.stream(mimeMessage.getFrom())
                .filter(InternetAddress.class::isInstance)
                .map(address -> ((InternetAddress) address).toString())
                .collect(Collectors.joining("; "));
    }

    public static String getEmailTo(MimeMessage mimeMessage) throws MessagingException {
        Address[] recipientsTo = mimeMessage.getRecipients(Message.RecipientType.TO);
        return Arrays.stream(recipientsTo)
                .filter(InternetAddress.class::isInstance)
                .map(address -> ((InternetAddress) address).toString())
                .collect(Collectors.joining("; "));
    }

    public static String getEmailCC(MimeMessage mimeMessage) throws MessagingException {
        Address[] recipientsTo = mimeMessage.getRecipients(Message.RecipientType.CC);
        if(null == recipientsTo || recipientsTo.length == 0){
            return "";
        }
        return Arrays.stream(recipientsTo)
                .filter(InternetAddress.class::isInstance)
                .map(address -> ((InternetAddress) address).toString())
                .collect(Collectors.joining("; "));
    }

}

