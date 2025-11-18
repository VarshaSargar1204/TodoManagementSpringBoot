package todo.todo_management_spring_boot.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import todo.todo_management_spring_boot.model.Todo;

public interface ITodoService {
	
	 List<Todo> getTodosByUser(String userName);

	    Optional<Todo> getTodoById(long id);

	    void saveTodo(Todo todo);

	    void updateTodo(Todo todo);

	    void deleteTodo(long id);

	    void addTodo(String userName, String title, String description, Todo.Priority priority, Date targetDate, boolean completed);

	    //get todos by priority
	    List<Todo> getTodosByPriority(String userName, Todo.Priority priority);
	    
	    List<Todo> getTodosSortedByPriority(String username);

}
