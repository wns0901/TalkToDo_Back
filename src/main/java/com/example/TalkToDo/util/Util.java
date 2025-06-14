package com.example.TalkToDo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.TalkToDo.config.security.PrincipalDetails;
import com.example.TalkToDo.dto.MeetingDataDTO;

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

    // 내용을 줄 단위로 분리
    String[] lines = content.split("\n");

    for (String line : lines) {
      String trimmedLine = line.trim();
      if (trimmedLine.isEmpty())
        continue; // 빈 줄은 추가하지 않음
      XWPFParagraph paragraph = document.createParagraph();
      XWPFRun run = paragraph.createRun();

      // 구분선 처리
      if (trimmedLine.startsWith("=") || trimmedLine.startsWith("-")) {
        run.setBold(true);
        run.setFontSize(12);
      }
      // 섹션 제목 처리
      else if (!trimmedLine.startsWith("•") && !trimmedLine.startsWith("  ")) {
        run.setBold(true);
        run.setFontSize(14);
      }
      // 들여쓰기된 내용 처리
      else if (trimmedLine.startsWith("  ")) {
        paragraph.setIndentationLeft(400); // 들여쓰기 설정
        run.setFontSize(11);
      }
      // 기본 텍스트 처리
      else {
        run.setFontSize(11);
      }

      run.setText(trimmedLine);
      // run.addBreak(); // 줄바꿈 제거
    }

    File file = new File(System.getProperty("java.io.tmpdir"), fileName);
    try (FileOutputStream out = new FileOutputStream(file)) {
      document.write(out);
    }
    document.close();
    return file;
  }

  public String dataToWordString(MeetingDataDTO meetingData) {
    StringBuilder sb = new StringBuilder();

    // 제목 섹션
    sb.append("=".repeat(50));
    sb.append("\n회의 제목\n");
    sb.append("\n").append(meetingData.getMeetingSummary().getSubject()).append("\n");

    // 회의 전문 섹션
    sb.append("\n").append("=".repeat(50));
    sb.append("\n회의 전문\n");
    sb.append("=".repeat(50)).append("\n");

    if (meetingData.getMeetingTranscript() != null) {
      meetingData.getMeetingTranscript().forEach(transcript -> {
        sb.append(transcript.getText()).append("\n");
      });
    }

    // 회의 요약 섹션
    sb.append("\n").append("=".repeat(50));
    sb.append("\n회의 요약\n");
    sb.append("\n").append("=".repeat(50));

    sb.append("\n").append(meetingData.getMeetingSummary().getSummary()).append("\n");

    // 일정 섹션
    sb.append("\n").append("=".repeat(50));
    sb.append("\n일정\n");
    sb.append("\n").append("=".repeat(50));

    if (meetingData.getSchedule() != null) {
      meetingData.getSchedule().forEach(schedule -> {
        String start = schedule.getStart() != null ? schedule.getStart().toString() : "";
        String end = schedule.getEnd() != null ? schedule.getEnd().toString() : "";
        sb.append("• ").append(start)
            .append(" ~ ").append(end)
            .append("\n  ").append(schedule.getText()).append("\n");
      });
    }

    // 할 일 섹션
    sb.append("\n").append("=".repeat(50));
    sb.append("\n할 일\n");
    sb.append("\n").append("=".repeat(50));
    if (meetingData.getTodos() != null) {
      meetingData.getTodos().forEach(todo -> {
        String start = todo.getStart() != null ? todo.getStart().toString() : "";
        String end = todo.getEnd() != null ? todo.getEnd().toString() : "";
        sb.append("• ").append(start)
            .append(" ~ ").append(end)
            .append("\n  ").append(todo.getText()).append("\n");
      });
    }

    return sb.toString();
  }
}
