package com.passionfactory.user.controller;

import com.passionfactory.user.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void after() {
        userRepository.deleteAll();
    }

    @ParameterizedTest(name = "권한별[{2}] 사용자 생성 : 성공")
    @CsvSource(value = {"admin1:10:ADMIN", "user1:20:USER", "guest1:30:GUEST"}, delimiter = ':')
    void userCreateSuccess(String name, int age, String role) throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("age", age);
        body.put("password", "test1234");
        body.put("role", role);

        ResultActions result = mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("signup"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.age", is(age)))
                .andExpect(jsonPath("$.role", is(role)));
    }

    @ParameterizedTest(name = "[{0}] 사용자 생성 : 실패 - 유효성 검증")
    @CsvSource(value = {"admin1:-1:password1:ADMIN", "user1:151:password2:USER", "guest1:30:pass:GUEST"}, delimiter = ':')
    void userCreateFail_validation(String name, int age, String password, String role) throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("age", age);
        body.put("password", password);
        body.put("role", role);

        ResultActions result = mockMvc.perform(
                post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(
                        r -> assertTrue(Objects.requireNonNull(r.getResolvedException())
                                .getClass()
                                .isAssignableFrom(MethodArgumentNotValidException.class))
                );
    }
}