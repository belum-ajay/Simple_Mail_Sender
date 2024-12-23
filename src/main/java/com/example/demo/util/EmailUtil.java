package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender sender;

    // New method for sending emails with attachments
    public boolean sendWithAttachments(String to, String[] cc, String[] bcc, String subject, String text, MultipartFile[] attachments) {
        boolean flag = false;
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);  // 'true' to allow multipart

            helper.setTo(to);
            if (cc != null && cc.length > 0) helper.setCc(cc);
            if (bcc != null && bcc.length > 0) helper.setBcc(bcc);
            helper.setSubject(subject);
            helper.setText(text);

            // Debugging: log the number of files to be attached
            System.out.println("Number of attachments: " + (attachments != null ? attachments.length : 0));

            // Add attachments to the email
            if (attachments != null && attachments.length > 0) {
                for (MultipartFile file : attachments) {
                    if (!file.isEmpty()) {
                        System.out.println("Attaching file: " + file.getOriginalFilename());
                        helper.addAttachment(file.getOriginalFilename(), file);
                    } else {
                        System.out.println("Skipped empty file: " + file.getOriginalFilename());
                    }
                }
            }

            // Send the email
            sender.send(message);
            flag = true;
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
}
