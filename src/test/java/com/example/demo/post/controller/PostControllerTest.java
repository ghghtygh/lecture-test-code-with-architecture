package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostControllerTest {
    @Test
    void 사용자는_게시물을_단건_조회할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 100L)
                .build();
        User user = User.builder()
                .id(1L)
                .email("t@t.com")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("00000-000000-000000")
                .lastLoginAt(0L)
                .build();
        testContainer.userRepository.save(user);
        testContainer.postRepository.save(Post.builder()
                .writer(user)
                .content("hello world")
                .createdAt(100L)
                .build());

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getId()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("hello world");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getModifiedAt()).isNull();
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("t@t.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jupo13");
    }

    @Test
    void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() {
        // given
        TestContainer testContainer = TestContainer.builder().build();

        // when
        // then
        assertThatThrownBy(() -> testContainer.postController.getPostById(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_게시물을_수정할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 100L)
                .build();
        User user = User.builder()
                .id(1L)
                .email("t@t.com")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("00000-000000-000000")
                .lastLoginAt(0L)
                .build();
        testContainer.userRepository.save(user);
        testContainer.postRepository.save(Post.builder()
                .writer(user)
                .content("hello world")
                .createdAt(100L)
                .build());

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1L, PostUpdate.builder()
                .content("hello world :)")
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getId()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("hello world :)");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(100L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("t@t.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jupo13");
    }
}