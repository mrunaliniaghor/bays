/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spam.textextraction;

import com.spam.entity.Mail;
import com.spam.ocr.OCRRecognition;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;

/**
 *
 * @author Girase
 */
public class EmailReaderService {

    List<Mail> spamMailList = new ArrayList<Mail>();
    List<Mail> legitimateMailList = new ArrayList<Mail>();
    List<Mail> allMailList = new ArrayList<Mail>();

    public List<Mail> readInboxMails(String username, String password) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            // Authenticate the Email.
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", username, password);

            // Read the Inbox Folder using READ_ONLY mode.
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Read the Unseen mails only.
            List<Message> messageList = new ArrayList<Message>();
            for (int index = inbox.getMessageCount(); index > (inbox.getMessageCount() - 10); index--) {
                if (index == 0) {
                    break;
                }

                messageList.add(inbox.getMessage(index));
            }

            System.out.println("MessageList Size :" + messageList.size());

            for (Message message : messageList) {
                try {
                    Address[] addressList = message.getFrom();

                    Mail mail = new Mail();
                    mail.setFrom(addressList[0].toString());
                    mail.setSent(message.getSentDate().toString());
                    mail.setSubject(message.getSubject());

                    if (message.getContent() instanceof Multipart) {
                        Multipart mp = (Multipart) message.getContent();
                        String content = "";
                        
                        for (int i = 0; i < mp.getCount(); i++) {
                            String disposition = mp.getBodyPart(i).getDisposition();
                                System.out.println("disposition :~ "+disposition);
                            if (disposition == null) {
                                content = mp.getBodyPart(0).getContent().toString();
                                System.out.println("Body ~ "+content);
                                
                            } else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                                String fileName = saveFile(mp.getBodyPart(i).getFileName(), mp.getBodyPart(i).getInputStream());
                                OCRRecognition ocr = new OCRRecognition();
                                
                                content = content + ocr.scanDocument(fileName);
                                System.out.println(content);
                                
                            } else if (disposition.equalsIgnoreCase(Part.INLINE)) {
                                content = content + mp.getBodyPart(i).getContent().toString();
                            } else {
                                content = content + mp.getBodyPart(i).getContent().toString();
                            }
                        }
                        mail.setContent(content.toLowerCase());
                        // System.out.println("Mail content :"+mail.getContent());
                    } else {
                        // System.out.println("Text Mail :" + message.getSubject());
                        mail.setContent(message.getContent().toString().toLowerCase());
                    }

                    allMailList.add(mail);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

            inbox.close(true);
            store.close();

            System.out.println("AllMailList Size :" + allMailList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allMailList;
    }

    public static String saveFile(String filename, InputStream input) throws IOException {
        if (filename == null) {
            filename = File.createTempFile("MailAttacheFile", ".out").getName();
        }
        System.out.println("downloading attachment...");
        File file = new File(filename);
//        for (int i = 0; file.exists(); i++) {
//            file = new File(filename + i);
//        }
        if(file.exists()) {
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        BufferedInputStream bis = new BufferedInputStream(input);
        int fByte;
        while ((fByte = bis.read()) != -1) {
            bos.write(fByte);
        }
        bos.flush();
        bos.close();
        bis.close();
        System.out.println("done attachment...");

        return file.getAbsolutePath();
    }
}