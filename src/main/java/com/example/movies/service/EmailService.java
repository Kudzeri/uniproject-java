/*
 * MIT License
 *
 * Copyright (c) 2025 Kudzeri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.movies.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${university.email}")
    private String universityEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Sends a welcome email to the university
    public void sendWelcomeEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(universityEmail);
            message.setSubject("Welcome Message");
            message.setText("Приветствие! Добро пожаловать в наш университет");
            mailSender.send(message);
            logger.info("Welcome email sent to {}", universityEmail);
        } catch (Exception ex) {
            logger.error("Failed to send welcome email: {}", ex.getMessage(), ex);
        }
    }

    // Sends an application confirmation email to the applicant
    public void sendApplicationSubmissionEmail(String name, String recipientEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Подтверждение заявки");
            message.setText("Заявка подана, " + name);
            mailSender.send(message);
            logger.info("Application confirmation email sent to {}", recipientEmail);
        } catch (Exception ex) {
            logger.error("Failed to send application confirmation email: {}", ex.getMessage(), ex);
        }
    }

    public void sendNewsletterEmail(String recipientEmail, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(recipientEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);
            logger.info("Newsletter email sent to {}", recipientEmail);
        } catch (Exception ex) {
            logger.error("Failed to send newsletter email to {}: {}", recipientEmail, ex.getMessage(), ex);
        }
    }
}
