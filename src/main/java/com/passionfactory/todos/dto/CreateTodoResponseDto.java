package com.passionfactory.todos.dto;

import com.passionfactory.todos.entity.Todos;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateTodoResponseDto {
    private final Long id;
    private final String name;
    private final boolean completed;
    private final LocalDateTime completedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public CreateTodoResponseDto(Todos todos) {
        this.id = todos.getId();
        this.name = todos.getName();
        this.completed = todos.isCompleted();
        this.completedAt = todos.getCompletedAt();
        this.createdAt = todos.getCreatedAt();
        this.updatedAt = todos.getUpdatedAt();
    }
}
