package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository userRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                .userRepository(userRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .uuidHolder(new TestUuidHolder("aaaa"))
                .clockHolder(new TestClockHolder(100L))
                .build();

        userRepository.save(User.builder()
                .id(1L)
                .email("t@t.com")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("00000-000000-000000")
                .lastLoginAt(0L)
                .build());

        userRepository.save(User.builder()
                .id(2L)
                .email("t2@t.com")
                .nickname("jupo14")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("00000-000000-000001")
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        //given
        String email = "t@t.com";

        //when
        User result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("jupo13");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        //given
        String email = "t2@t.com";

        //when
        //then
        assertThatThrownBy(() -> {
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        //given
        long id = 1;

        //when
        User result = userService.getById(id);

        //then
        assertThat(result.getNickname()).isEqualTo("jupo13");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올_수_없다() {
        //given
        long id = 2;

        //when
        //then
        assertThatThrownBy(() -> {
            userService.getById(id);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("t3@t.com")
                .address("Seoul")
                .nickname("jupo15")
                .build();

        //when
        User user = userService.create(userCreate);

        //then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getStatus()).isEqualTo(PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa");
        assertThat(user.getLastLoginAt()).isNull();
    }

    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Incheon")
                .nickname("jupo13-2")
                .build();

        //when
        User user = userService.update(1, userUpdate);

        //then
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getAddress()).isEqualTo("Incheon");
        assertThat(user.getNickname()).isEqualTo("jupo13-2");
        assertThat(user.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void user가_로그인되면_마지막_로그인_시간이_변경된다() {
        //given
        //when
        userService.login(1);

        //then
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        //given
        //when
        userService.verifyEmail(2, "00000-000000-000001");

        //then
        User user = userService.getById(2);
        assertThat(user.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_예외가_발생한다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "00000-000000-000000");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}