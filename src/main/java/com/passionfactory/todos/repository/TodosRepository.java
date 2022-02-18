package com.passionfactory.todos.repository;

import com.passionfactory.todos.entity.Todos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodosRepository extends JpaRepository<Todos, Long> {

    Page<Todos> findAllByUserName(String name, Pageable pageable);

    Optional<Todos> findByIdAndUserName(Long todosId, String name);
}
