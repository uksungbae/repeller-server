package school.kku.repellingserver.mail.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import school.kku.repellingserver.mail.dto.MailMessage;


@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

//    @Test
    void mail() {
        //given
        MailMessage mailMessage = MailMessage.of(
                "pica23000@naver.com",
                "테스트",
                "테스트"
        );

        mailService.sendCertificationNumber("pica23000@naver.com");
//        mailService.sendSimpleMail(mailMessage);
        //when

        //then

    }

}