package com.passionfactory.todos.dto;

import com.passionfactory.todos.entity.Todos;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FindAllResponseDto {
    private final Long id;
    private final String name;
    private final boolean completed;

    @Builder
    public FindAllResponseDto(Todos todos) {
        this.id = todos.getId();
        this.name = todos.getName();
        this.completed = todos.isCompleted();
    }
}
