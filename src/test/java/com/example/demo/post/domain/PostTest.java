package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void Post는_PostCreate_객체로_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("hello world")
                .build();

        User writer = User.builder()
                .email("t@t.com")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-bbbb-cccc-dddd")
                .build();


        //when
        Post post = Post.from(postCreate, writer, new TestClockHolder(1678530673958L));

        //then
        assertThat(post.getId()).isNull();
        assertThat(post.getContent()).isEqualTo("hello world");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getWriter().getEmail()).isEqualTo("t@t.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("jupo13");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaa-bbbb-cccc-dddd");
    }

    @Test
    void Post는_PostUpdate_객체로_수정할_수_있다() {
        //given
        Post post = Post.builder()
                .id(1L)
                .content("hello world")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .build();

        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        //when
        post = post.update(postUpdate, new TestClockHolder(1678530673979L));

        //then
        assertThat(post.getId()).isEqualTo(1L);
        assertThat(post.getContent()).isEqualTo("hello world :)");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getModifiedAt()).isEqualTo(1678530673979L);
    }
}