package com.passionfactory.todos.dto;

import com.passionfactory.todos.entity.Todos;
import com.passionfactory.user.entity.User;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class CreateTodoRequestDto {

    @NotNull(message = "name cannot be null")
    @Size(min = 1, message = "name not be less than 1 characters")
    private String name;

    private boolean completed;

    public Todos toEntity(User user) {
        return Todos.builder()
                .name(name)
                .completed(completed)
                .user(user)
                .build();
    }
}
