package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/sql/user-repository-test-data.sql")
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        //then
        assertThat(result).isPresent();
    }
    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optinal_empty_를_내려준다() {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.INACTIVE);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void findByIdAndEmail_로_유저_데이터를_찾아올_수_있다() {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("t@t.com", UserStatus.ACTIVE);

        //then
        assertThat(result).isPresent();
    }
    @Test
    void findByIdAndEmail_로_데이터가_없으면_Optinal_empty_를_내려준다() {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("t@t.com", UserStatus.INACTIVE);

        //then
        assertThat(result).isEmpty();
    }
}