package com.passionfactory.todos.service;

import com.passionfactory.exception.UnAuthorizationException;
import com.passionfactory.exception.NotFoundException;
import com.passionfactory.todos.dto.*;
import com.passionfactory.todos.entity.Todos;
import com.passionfactory.todos.repository.TodosRepository;
import com.passionfactory.user.entity.User;
import com.passionfactory.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodosService {

    private final UserRepository userRepository;
    private final TodosRepository todosRepository;

    public Page<FindAllResponseDto> findAllByUserName(String name, Pageable pageable) {
        return new PageImpl<>(todosRepository.findAllByUserName(name, pageable)
                .stream()
                .map(FindAllResponseDto::new)
                .collect(Collectors.toList()));
    }

    public FindByIdResponseDto findByIdAndUserName(Long todosId, String name) {
        return todosRepository.findByIdAndUserName(todosId, name)
                .map(FindByIdResponseDto::new)
                .orElseThrow(NotFoundException::new);
    }

    public CreateTodoResponseDto createTodo(CreateTodoRequestDto createTodoRequestDto, String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(NotFoundException::new);

        Todos todo = createTodoRequestDto.toEntity(user);
        Todos saveTodo = todosRepository.save(todo);

        return new CreateTodoResponseDto(saveTodo);
    }

    public UpdateTodoResponseDto updateTodo(Long todosId, String name, UpdateTodoRequestDto updateTodoRequestDto) {
        Todos todos = findTodoOrElseThrow(todosId);
        User user = findUserOrElseThrow(name);

        checkTodosByUserId(todos, user);

        todos.updateTodo(updateTodoRequestDto.getName(), updateTodoRequestDto.isCompleted());
        

        return new UpdateTodoResponseDto(todos);
    }

    private void checkTodosByUserId(Todos todos, User user) {
        if (todos.getUser().getId() != user.getId()) {
            throw new UnAuthorizationException();
        }
    }

    private User findUserOrElseThrow(String name) {
        Optional<User> findUser = userRepository.findByName(name);
        if (findUser.isEmpty()) {
            throw new NotFoundException("사용자가 존재하지 않습니다");
        }
        return findUser.get();
    }

    private Todos findTodoOrElseThrow(Long todosId) {
        Optional<Todos> findTodos = todosRepository.findById(todosId);
        if (findTodos.isEmpty()) {
            throw new NotFoundException("일정이 존재하지 않습니다");
        }
        return findTodos.get();
    }

    public Long deleteTodo(Long todosId, String name) {
        if (isDeletedPossible(todosId, name)) {
            todosRepository.deleteById(todosId);
        }
        return todosId;
    }

    private boolean isDeletedPossible(Long todosId, String name) {
        Optional<Todos> findTodos = todosRepository.findById(todosId);
        if (findTodos.isPresent()) {
            Todos todos = findTodos.get();
            if (!Objects.equals(todos.getUser().getName(), name)) {
                throw new UnAuthorizationException();
            }
        } else {
            throw new NotFoundException();
        }
        return true;
    }
}
