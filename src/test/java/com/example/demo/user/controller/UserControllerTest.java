package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.MyProfileResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_전달_받을_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("t@t.com")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("00000-000000-000000")
                .lastLoginAt(0L)
                .build());

        // when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("t@t.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jupo13");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when
        // then
        assertThatThrownBy(() -> testContainer.userController.getUserById(888L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {

        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(2L)
                .email("t2@t.com")
                .nickname("jupo14")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("00000-000000-000001")
                .lastLoginAt(0L)
                .build());

        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(2L, "00000-000000-000001");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(2L).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(2L)
                .email("t2@t.com")
                .nickname("jupo14")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("00000-000000-000001")
                .lastLoginAt(0L)
                .build());

        // when
        // then
        assertThatThrownBy(() -> testContainer.userController.verifyEmail(2L, "00000-000000-000003")).isInstanceOf(CertificationCodeNotMatchedException.class);
    }


    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 100L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("t@t.com")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("00000-000000-r000000")
                .lastLoginAt(0L)
                .build());

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("t@t.com");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("t@t.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jupo13");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("t@t.com")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("00000-000000-000000")
                .lastLoginAt(0L)
                .build());

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("t@t.com", UserUpdate.builder()
                .nickname("jupo13-2")
                .address("Daejeon")
                .build());

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("t@t.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jupo13-2");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("Daejeon");
    }
}