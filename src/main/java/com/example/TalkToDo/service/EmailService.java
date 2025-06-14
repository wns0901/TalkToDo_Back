package com.example.TalkToDo.service;

import java.io.IOException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.TalkToDo.dto.EmailDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final MeetingService meetingService;
  private final S3Service s3Service;
  private final JavaMailSender mailSender;

  public void sendEmail(EmailDTO emailDTO) throws MessagingException, IOException {
    String docxUrl = meetingService.getDocx(emailDTO.getMeetingId());

    byte[] docxBytes = s3Service.downloadFile(docxUrl);

    // 각 수신자에게 개별적으로 이메일 전송
    for (String recipient : emailDTO.getToList()) {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(new InternetAddress(emailDTO.getFrom(), emailDTO.getFrom()));
      helper.setTo(recipient);
      helper.setSubject(emailDTO.getSubject());
      helper.setText(emailDTO.getText());

      // S3에서 다운로드한 docx 파일 첨부
      if (docxBytes != null && docxBytes.length > 0) {
        org.springframework.core.io.ByteArrayResource docxResource = new org.springframework.core.io.ByteArrayResource(
            docxBytes) {
          @Override
          public String getFilename() {
            return "회의록.docx";
          }
        };
        helper.addAttachment(docxResource.getFilename(), docxResource);
      }
      System.out.println("메일 전송");
      mailSender.send(message);
      System.out.println("메일 전송 완료");
    }
  }
}
