package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        //given
        String email = "t@t.com";

        //when
        UserEntity result = userService.getByEmail(email);

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
        UserEntity result = userService.getById(id);

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

}