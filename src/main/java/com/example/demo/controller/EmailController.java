package com.example.demo.controller;

import com.example.demo.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EmailController {

    @Autowired
    private EmailUtil emailUtil;

    // Display email form
    @GetMapping("/")
    public String showForm() {
        return "index"; // Return index.html
    }

    // Handle form submission with attachments
    @PostMapping("/sendEmail")
    public String sendEmail(
            @RequestParam("to") String to,
            @RequestParam("cc") String[] cc,
            @RequestParam("bcc") String[] bcc,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            @RequestParam(value = "attachment", required = false) MultipartFile[] attachments,  // handle file upload
            Model model) {

        // Debugging: log the uploaded file names (if any)
        if (attachments != null && attachments.length > 0) {
            for (MultipartFile file : attachments) {
                System.out.println("Uploaded file: " + file.getOriginalFilename());
            }
        } else {
            System.out.println("No files uploaded.");
        }

        // Send the email with attachments
        boolean isSuccess = emailUtil.sendWithAttachments(to, cc, bcc, subject, message, attachments);

        // Add success/failure message to the model
        if (isSuccess) {
            model.addAttribute("status", "Email sent successfully!");
        } else {
            model.addAttribute("status", "Failed to send email.");
        }
        return "index";  // Return the same form with status message
    }
}
