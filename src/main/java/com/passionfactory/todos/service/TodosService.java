package com.passionfactory.todos.service;

import com.passionfactory.exception.NotFoundException;
import com.passionfactory.todos.dto.CreateTodoRequestDto;
import com.passionfactory.todos.dto.CreateTodoResponseDto;
import com.passionfactory.todos.dto.FindByIdResponseDto;
import com.passionfactory.todos.dto.FindAllResponseDto;
import com.passionfactory.todos.entity.Todos;
import com.passionfactory.todos.repository.TodosRepository;
import com.passionfactory.user.entity.User;
import com.passionfactory.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodosService {

    private final UserRepository userRepository;
    private final TodosRepository todosRepository;

    public Page<FindAllResponseDto> findAllByUserId(Long userId, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of((int) pageable.getOffset(), pageable.getPageSize(), Sort.by("id").ascending());
        List<FindAllResponseDto> collect = todosRepository.findAllByUserId(userId, pageRequest)
                .stream()
                .map(FindAllResponseDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(collect);
    }

    public FindByIdResponseDto findByIdAndUserId(Long todosId, Long userId) {
        return todosRepository.findByIdAndUserId(todosId, userId)
                .map(FindByIdResponseDto::new)
                .orElseThrow(NotFoundException::new);
    }

    public CreateTodoResponseDto createTodo(CreateTodoRequestDto createTodoRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);

        Todos todo = createTodoRequestDto.toEntity(user);
        Todos saveTodo = todosRepository.save(todo);

        return new CreateTodoResponseDto(saveTodo);
    }
}
