package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {

    @Test
    void 사용자는_회원_가입을_할_수_있고_회원_가입된_사용자는_PENDING_상태이다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(() -> "00000-000000-000000")
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(UserCreate.builder()
                .nickname("jupo13")
                .email("t@t.com")
                .address("Seoul")
                .build());

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("t@t.com");
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(result.getBody().getNickname()).isEqualTo("jupo13");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
    }
}