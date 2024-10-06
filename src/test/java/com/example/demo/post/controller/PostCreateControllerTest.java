package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreateControllerTest {

    @Test
    void 사용자는_게시물을_작성할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 100L)
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

        //when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(PostCreate.builder()
                .content("hello world")
                .writerId(1L)
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody().getId()).isGreaterThan(0);
        assertThat(result.getBody().getContent()).isEqualTo("hello world");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jupo13");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("t@t.com");
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
    }
}
