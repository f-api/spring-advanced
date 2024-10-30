package org.example.expert.domain.todo.util;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoUtil {

    private final TodoRepository todoRepository;

    public Todo findById(long todoId) {
        return todoRepository.findById(todoId).orElseThrow(() ->
                new InvalidRequestException("Todo not found"));
    }
}
