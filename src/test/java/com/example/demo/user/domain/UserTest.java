package com.example.demo.user.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void User는_UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .nickname("jupo13")
                .email("t@t.c")
                .address("Seoul")
                .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaa"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getNickname()).isEqualTo("jupo13");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getEmail()).isEqualTo("t@t.c");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa");
        assertThat(user.getLastLoginAt()).isNull();
    }

    @Test
    void User는_UserUpdate_객체로_데이터를_업데이트할_수_있다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Daejeon")
                .nickname("jupo13_2")
                .build();

        User user = User.builder()
                .id(1L)
                .email("t@t.c")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-bbbb-cccc-dddd")
                .lastLoginAt(100L)
                .build();

        //when
        user = user.update(userUpdate);

        //then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getNickname()).isEqualTo("jupo13_2");
        assertThat(user.getAddress()).isEqualTo("Daejeon");
        assertThat(user.getEmail()).isEqualTo("t@t.c");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa-bbbb-cccc-dddd");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void User는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("t@t.c")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-bbbb-cccc-dddd")
                .lastLoginAt(100L)
                .build();

        //when
        user = user.login(new TestClockHolder(200L));

        //then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getNickname()).isEqualTo("jupo13");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getEmail()).isEqualTo("t@t.c");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa-bbbb-cccc-dddd");
        assertThat(user.getLastLoginAt()).isEqualTo(200L);
    }

    @Test
    void User는_유효한_인증_코드로_계정을_활성화_할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("t@t.c")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-bbbb-cccc-dddd")
                .build();

        //when
        user = user.certificate("aaaa-bbbb-cccc-dddd");

        //then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getNickname()).isEqualTo("jupo13");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getEmail()).isEqualTo("t@t.c");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa-bbbb-cccc-dddd");
        assertThat(user.getLastLoginAt()).isNull();
    }

    @Test
    void User는_잘못된_인증_코드로_계정을_활성화_하려면_에러를_던진다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("t@t.c")
                .nickname("jupo13")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-bbbb-cccc-dddd")
                .lastLoginAt(100L)
                .build();

        //when
        //then
        assertThatThrownBy(() -> user.certificate("bbb")).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}