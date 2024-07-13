package school.kku.repellingserver.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import school.kku.repellingserver.mail.constant.MailCode;
import school.kku.repellingserver.mail.dto.MailMessage;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailService {

  private final JavaMailSender javaMailSender;

  private SimpleMailMessage msg = new SimpleMailMessage();

  public MailCode sendSimpleMail(MailMessage mailMessage) {
    setMailMessage(mailMessage, mailMessage.subject(), mailMessage.text());
    return sendEmail();
  }

  public String sendCertificationNumber(String toEmail) {
    int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);
    String certificationNumberSubject = "조수퇴치 인증번호 발급입니다.";
    String certificationNumberText = "인증번호는 " + randomNumber + " 입니다.";

    MailMessage mailMessage = MailMessage.of(toEmail, certificationNumberSubject, certificationNumberText);
    setMailMessage(mailMessage, mailMessage.subject(), mailMessage.text());
    sendEmail();

    return String.valueOf(randomNumber);
  }


  private void setMailMessage(MailMessage mailMessage, String subject, String text) {
    msg.setFrom(mailMessage.from());
    msg.setTo(mailMessage.to());
    msg.setSubject(subject);
    msg.setText(text);
  }


  private MailCode sendEmail() {
    try {
      javaMailSender.send(msg);
      return MailCode.SUCCESS;
    } catch (MailException ex) {
      log.error(ex.getMessage());
      return MailCode.FAIL;
    }
  }

  private static String makeTemporaryPassword() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString().substring(0, 8);
  }
}
