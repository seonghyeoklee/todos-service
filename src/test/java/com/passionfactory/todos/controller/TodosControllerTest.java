package com.passionfactory.todos.controller;

import com.passionfactory.WithMockAuthentication;
import com.passionfactory.exception.NotFoundException;
import com.passionfactory.todos.repository.TodosRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityManager;
import java.util.Objects;

import static com.passionfactory.user.entity.Role.GUEST;
import static com.passionfactory.user.entity.Role.USER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TodosControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TodosRepository todosRepository;

    @Autowired
    EntityManager em;

    @AfterEach
    public void teardown() {
        todosRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE todos ALTER COLUMN `todos_id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @Order(1)
    @DisplayName("todos 생성 : 실패 - 인가(외부사용자는 작성 금지)")
    @WithMockAuthentication(name = "testAccount", role = GUEST)
    void createTodosFail_Authorization() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "testName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    @DisplayName("todos 생성 : 실패 - 유효하지 않은 값 입력")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void createTodosFail_Valid() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "");
        body.put("completed", null);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect((r) -> assertTrue(Objects.requireNonNull(r.getResolvedException())
                        .getClass()
                        .isAssignableFrom(MethodArgumentNotValidException.class)));
    }

    @Test
    @Order(3)
    @DisplayName("todos 생성 : 성공")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void createTodosSuccess() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "testName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect(handler().methodName("createTodo"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("testName")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.completedAt", nullValue()));
    }

    @Test
    @Order(4)
    @DisplayName("todos 전체 조회 : 성공")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void findTodosAllSuccess() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "testName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated());

        result = mockMvc.perform(
                get("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect(handler().methodName("findAllByUserName"))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].name", is("testName")))
                .andExpect(jsonPath("$.content[0].completed", is(false)));
    }

    @Test
    @Order(5)
    @DisplayName("todos 단건 조회 : 실패 - 잘못된 아이디값 입력")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void findTodosByIdFail_NotFound() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "testName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated());

        result = mockMvc.perform(
                get("/todos/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect((r) -> assertTrue(Objects.requireNonNull(r.getResolvedException())
                        .getClass()
                        .isAssignableFrom(NotFoundException.class)));
    }

    @Test
    @Order(6)
    @DisplayName("todos 단건 조회 : 성공")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void findTodosByIdSuccess() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "testName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated());

        result = mockMvc.perform(
                get("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect(handler().methodName("findByIdAndUserName"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("testName")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.completedAt", nullValue()));
    }

    @Test
    @Order(7)
    @DisplayName("todos 수정 : 실패 - 유효하지 않은 값 입력")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void updateTodoFail() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "beforeName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated());

        body.put("name", "");
        body.put("completed", true);
        result = mockMvc.perform(
                put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect((r) -> assertTrue(Objects.requireNonNull(r.getResolvedException())
                        .getClass()
                        .isAssignableFrom(MethodArgumentNotValidException.class)));
    }

    @Test
    @Order(8)
    @DisplayName("todos 수정 : 성공")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void updateTodoSuccess() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "beforeName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated());

        body.put("name", "afterName");
        body.put("completed", true);
        result = mockMvc.perform(
                put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect(handler().methodName("updateTodo"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("afterName")))
                .andExpect(jsonPath("$.completed", is(true)))
                .andExpect(jsonPath("$.completedAt").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    @Order(9)
    @DisplayName("todos 삭제 : 실패 - 잘못된 아이디값 입력")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void deleteTodoFail() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "beforeName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated());

        result = mockMvc.perform(
                delete("/todos/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect((r) -> assertTrue(Objects.requireNonNull(r.getResolvedException())
                        .getClass()
                        .isAssignableFrom(NotFoundException.class)));
    }

    @Test
    @Order(10)
    @DisplayName("todos 삭제 : 성공")
    @WithMockAuthentication(name = "testAccount", role = USER)
    void deleteTodoSuccess() throws Exception {
        JSONObject body = new JSONObject();
        body.put("name", "beforeName");
        body.put("completed", false);

        ResultActions result = mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body.toString())
        );
        result.andDo(print())
                .andExpect(status().isCreated());

        result = mockMvc.perform(
                delete("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(handler().handlerType(TodosController.class))
                .andExpect(handler().methodName("deleteTodo"));
    }
}