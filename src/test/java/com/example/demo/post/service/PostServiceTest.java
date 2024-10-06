package com.example.demo.post.service;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostServiceTest {

    private PostServiceImpl postService;

    @BeforeEach
    void setup() {

        FakePostRepository postRepository = new FakePostRepository();
        FakeUserRepository userRepository = new FakeUserRepository();
        postService = PostServiceImpl.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(new TestClockHolder(100L))
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("t@t.com")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("00000-000000-000000")
                .lastLoginAt(0L)
                .build();

        userRepository.save(user1);

        userRepository.save(User.builder()
                .id(2L)
                .email("t2@t.com")
                .nickname("jupo14")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("00000-000000-000001")
                .lastLoginAt(0L)
                .build());

        postRepository.save(Post.builder()
                .id(1L)
                .content("hello world")
                .createdAt(1678530673958L)
                .modifiedAt(1678530673958L)
                .writer(user1).build());
    }

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        //given
        long postId = 1L;

        //when
        Post result = postService.getById(postId);

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
        Post post = postService.create(postCreate);

        //then
        assertThat(post.getId()).isNotNull();
        assertThat(post.getContent()).isEqualTo("hello world2");
        assertThat(post.getCreatedAt()).isEqualTo(100L);
    }

    @Test
    void postUpdate를_이용하여_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        //when
        Post post = postService.update(1, postUpdate);

        //then
        assertThat(post.getId()).isEqualTo(1);
        assertThat(post.getContent()).isEqualTo("hello world :)");
        assertThat(post.getModifiedAt()).isEqualTo(100L);
    }

}