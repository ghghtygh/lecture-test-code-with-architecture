package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceTest {

    @Test
    void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
        // given
        FakeMailSender mailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(mailSender);

        // when
        certificationService.send("ghghtygh@naver.com", 1, "aaaabbbbccccdddd");

        // then
        assertThat(mailSender.email).isEqualTo("ghghtygh@naver.com");
        assertThat(mailSender.title).isEqualTo("Please certify your email address");
        assertThat(mailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaabbbbccccdddd" );
    }
}