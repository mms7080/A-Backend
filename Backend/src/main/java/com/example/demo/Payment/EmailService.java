package com.example.demo.Payment;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReservationSuccessEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom("qjatn4343@gmail.com"); // 이름 포함 가능
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML 형식

            mailSender.send(message);
            System.out.println("✅ 예매 이메일 발송 성공");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("❌ 이메일 전송 실패: " + e.getMessage());
        }
    }
}
