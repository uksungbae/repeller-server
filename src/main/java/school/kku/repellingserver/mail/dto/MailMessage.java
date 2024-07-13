package school.kku.repellingserver.mail.dto;

import org.springframework.beans.factory.annotation.Value;

public record MailMessage(
    String to,
    String from,
    String subject,
    String text
) {

  @Value("${smtp.from}")
  private static String basicFrom;

  MailMessage(String to, String subject, String text) {
    this(to, basicFrom, subject, text);
  }

  public static MailMessage of(String to, String from, String subject, String text) {
    return new MailMessage(to, from, subject, text);
  }

  public static MailMessage of(String to, String subject, String text) {
    return new MailMessage(to, subject, text);
  }

  public static MailMessage of(String to) {
    return new MailMessage(to, "", "");
  }

}
