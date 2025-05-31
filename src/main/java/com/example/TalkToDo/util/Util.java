package com.example.TalkToDo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.TalkToDo.config.security.PrincipalDetails;
import com.example.TalkToDo.dto.MeetingDataDTO;
import com.example.TalkToDo.dto.MeetingDataDTO.MeetingTranscript;

@Component
public class Util {
  public File convertToMp3(MultipartFile inputFile) throws IOException, InterruptedException {
    // 임시 파일로 저장
    String originalFileName = inputFile.getOriginalFilename();
    String tempInputPath = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + "_" + originalFileName;
    File tempInputFile = new File(tempInputPath);
    try (FileOutputStream fos = new FileOutputStream(tempInputFile)) {
      fos.write(inputFile.getBytes());
    }

    // 변환될 mp3 파일 경로
    String mp3FileName = UUID.randomUUID() + ".mp3";
    String tempOutputPath = System.getProperty("java.io.tmpdir") + "/" + mp3FileName;
    File tempOutputFile = new File(tempOutputPath);

    // ffmpeg 명령어 실행
    ProcessBuilder pb = new ProcessBuilder(
        "ffmpeg", "-y", "-i", tempInputFile.getAbsolutePath(), tempOutputFile.getAbsolutePath());
    pb.redirectErrorStream(true);
    Process process = pb.start();
    int exitCode = process.waitFor();

    // 임시 입력 파일 삭제
    tempInputFile.delete();

    if (exitCode != 0 || !tempOutputFile.exists()) {
      throw new IOException("mp3 변환 실패");
    }

    return tempOutputFile; // 변환된 mp3 파일 반환
  }

  public Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails) {
      PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
      return userDetails.getUser().getId();
    }
    throw new RuntimeException("로그인 정보 없음");
  }

  public File stringToWordFile(String content, String fileName) throws IOException {
    XWPFDocument document = new XWPFDocument();
    XWPFParagraph paragraph = document.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.setText(content);

    File file = new File(System.getProperty("java.io.tmpdir"), fileName);
    try (FileOutputStream out = new FileOutputStream(file)) {
      document.write(out);
    }
    document.close();
    return file;
  }

  public String dataToWordString(MeetingDataDTO meetingData) {
    StringBuilder sb = new StringBuilder();
    sb.append("제목: ");
    sb.append(meetingData.getMeetingSummary().getSubject());
    sb.append("\n");
    sb.append("전문: ");
    sb.append(meetingData.getMeetingTranscript().stream().map(MeetingTranscript::getText).collect(Collectors.joining(", ")));
    sb.append("\n");
    sb.append("요약: ");
    sb.append(meetingData.getMeetingSummary().getSummary());
    sb.append("\n");
    sb.append("일정: ");
    sb.append(meetingData.getSchedule().stream().map((schedule) -> {
      return schedule.getStart() + " - " + schedule.getEnd() + " " + schedule.getText();
    }).collect(Collectors.joining(", ")));
    sb.append("\n");
    sb.append("할 일: ");
    sb.append(meetingData.getTodo().stream().map((todo) -> {
      return todo.getStart() + " - " + todo.getEnd() + " " + todo.getText();
    }).collect(Collectors.joining(", ")));
    sb.append("\n");
    return sb.toString();
  }
}
