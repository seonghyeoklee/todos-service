package com.passionfactory.todos.controller;

import com.passionfactory.config.auth.PrincipalDetails;
import com.passionfactory.todos.dto.CreateTodoRequestDto;
import com.passionfactory.todos.dto.CreateTodoResponseDto;
import com.passionfactory.todos.dto.FindAllResponseDto;
import com.passionfactory.todos.dto.FindByIdResponseDto;
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
    public ResponseEntity<Page<FindAllResponseDto>> findAllByUserId(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable) {
        return ResponseEntity
                .status(OK)
                .body(todosService.findAllByUserId(principalDetails.getUser().getId(), pageable));
    }

    @GetMapping("/todos/{todosId}")
    public ResponseEntity<FindByIdResponseDto> findByIdAndUserId(
            @PathVariable("todosId") Long todosId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity
                .status(OK)
                .body(todosService.findByIdAndUserId(todosId, principalDetails.getUser().getId()));
    }

    @PostMapping("/todos")
    public ResponseEntity<CreateTodoResponseDto> createTodo(
            @Valid @RequestBody CreateTodoRequestDto createTodoRequestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity
                .status(OK)
                .body(todosService.createTodo(createTodoRequestDto, principalDetails.getUser().getId()));
    }
}
