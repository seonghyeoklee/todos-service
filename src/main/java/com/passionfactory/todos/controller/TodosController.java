package com.passionfactory.todos.controller;

import com.passionfactory.config.auth.PrincipalDetails;
import com.passionfactory.todos.dto.*;
import com.passionfactory.todos.service.TodosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class TodosController {

    private final TodosService todosService;

    @GetMapping("/todos")
    public ResponseEntity<Page<FindAllResponseDto>> findAllByUserName(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable) {
        return ResponseEntity
                .status(OK)
                .body(todosService.findAllByUserName(principalDetails.getUser().getName(), pageable));
    }

    @GetMapping("/todos/{todosId}")
    public ResponseEntity<FindByIdResponseDto> findByIdAndUserName(
            @PathVariable("todosId") Long todosId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity
                .status(OK)
                .body(todosService.findByIdAndUserName(todosId, principalDetails.getUser().getName()));
    }

    @PostMapping("/todos")
    public ResponseEntity<CreateTodoResponseDto> createTodo(
            @Valid @RequestBody CreateTodoRequestDto createTodoRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity
                .status(CREATED)
                .body(todosService.createTodo(createTodoRequestDto, principalDetails.getUser().getName()));
    }

    @PutMapping("/todos/{todosId}")
    public ResponseEntity<UpdateTodoResponseDto> updateTodo(
            @PathVariable("todosId") Long todosId,
            @Valid @RequestBody UpdateTodoRequestDto updateTodoRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity
                .status(OK)
                .body(todosService.updateTodo(todosId, principalDetails.getUser().getName(), updateTodoRequestDto));
    }

    @DeleteMapping("/todos/{todosId}")
    public ResponseEntity<Long> deleteTodo(
            @PathVariable("todosId") Long todosId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity
                .status(NO_CONTENT)
                .body(todosService.deleteTodo(todosId, principalDetails.getUser().getName()));
    }
}
