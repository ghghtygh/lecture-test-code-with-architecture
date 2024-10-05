package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {

    @Test
    void Post_객체로_응답을_생성할_수_있다() {
        //given
        User writer = User.builder()
                .email("t@t.c")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-bbbb-cccc-dddd")
                .build();

        Post post = Post.builder()
                .id(1L)
                .content("hello world")
                .writer(writer)
                .build();

        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        assertThat(postResponse.getId()).isEqualTo(1L);
        assertThat(postResponse.getContent()).isEqualTo("hello world");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("t@t.c");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("jupo13");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}