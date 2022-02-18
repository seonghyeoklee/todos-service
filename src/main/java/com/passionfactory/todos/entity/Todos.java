package com.passionfactory.todos.entity;

import com.passionfactory.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Todos extends BaseLocalDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todos_id")
    private long id;

    @Column(nullable = false)
    private String name;

    private boolean completed;
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateTodo(String name, boolean completed) {
        this.name = name;
        this.completed = completed;
        setCompletedAt(completed);
    }

    private void setCompletedAt(boolean completed) {
        if (completed) {
            this.completedAt = LocalDateTime.now();
        } else {
            this.completedAt = null;
        }
    }
}
