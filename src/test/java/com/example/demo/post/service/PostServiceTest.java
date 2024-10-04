package com.example.demo.post.service;

import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {

    @Autowired
    PostService postService;

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        //given
        long postId = 1L;

        //when
        PostEntity result = postService.getById(postId);

        //then
        assertThat(result.getContent()).isEqualTo("hello world");
        assertThat(result.getWriter().getId()).isEqualTo(1);
        assertThat(result.getWriter().getEmail()).isEqualTo("t@t.com");
    }

    @Test
    void postCreate를_이용하여_게시물을_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1L)
                .content("hello world2")
                .build();

        //when
        PostEntity postEntity = postService.create(postCreate);

        //then
        assertThat(postEntity.getId()).isNotNull();
        assertThat(postEntity.getContent()).isEqualTo("hello world2");
        assertThat(postEntity.getCreatedAt()).isGreaterThan(0);
    }

    @Test
    void postUpdate를_이용하여_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        //when
        PostEntity postEntity = postService.update(1, postUpdate);

        //then
        assertThat(postEntity.getId()).isEqualTo(1);
        assertThat(postEntity.getContent()).isEqualTo("hello world :)");
        assertThat(postEntity.getModifiedAt()).isGreaterThan(0);
    }

}