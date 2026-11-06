package org.example.expert.data;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

public class TodoFixture {
	public static Todo createTodo(User user){
		Todo todo = new Todo("title", "contents", "Sunny", user);
		ReflectionTestUtils.setField(todo, "id", 1L);
		return todo;
	}

	public static Todo createTodoWithoutUser() {
		Todo todo = new Todo();
		ReflectionTestUtils.setField(todo, "user", null);
		return todo;
	}
}
