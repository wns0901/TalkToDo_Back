package com.example.TalkToDo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.TalkToDo.dto.EmailDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  public void sendEmail(EmailDTO emailDTO) throws MessagingException, IOException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setFrom(new InternetAddress("crewdock0@gmail.com", emailDTO.getFrom()));
    helper.setTo(emailDTO.getTo());
    helper.setSubject(emailDTO.getSubject());
    helper.setText(emailDTO.getText());

    // 첨부파일 추가
    if (emailDTO.getAttachments() != null) {
      for (MultipartFile file : emailDTO.getAttachments()) {
        if (file != null && !file.isEmpty()) {
          helper.addAttachment(file.getOriginalFilename(), file);
        }
      }
    }

    mailSender.send(message);
  }
}
