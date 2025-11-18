package todo.todo_management_spring_boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todo.todo_management_spring_boot.model.Todo;
import todo.todo_management_spring_boot.repository.TodoRepository;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService implements ITodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> getTodosByUser(String userName) {
        return todoRepository.findByUserName(userName);
    }
    
    public List<Todo> getTodosSortedByPriority(String username) {
        List<Todo> todos = todoRepository.findByUserName(username);

        todos.sort(Comparator.comparingInt(todo -> {
            switch (todo.getPriority()) {
                case HIGH: return 0;
                case MEDIUM: return 1;
                case LOW: return 2;
                default: return 3;
            }
        }));

        return todos;
    }

    @Override
    public Optional<Todo> getTodoById(long id) {
        return todoRepository.findById(id);
    }

    @Override
    public void saveTodo(Todo todo) {
        todoRepository.save(todo);
    }

    @Override
    public void updateTodo(Todo todo) {
        todoRepository.save(todo); // save() works for both insert and update
    }

    @Override
    public void deleteTodo(long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public void addTodo(String userName, String title, String description, Todo.Priority priority, Date targetDate, boolean completed) {
        Todo todo = new Todo();
        todo.setUserName(userName);
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setPriority(priority);
        todo.setTargetDate(targetDate);
        todo.setCompleted(completed);
        todo.setCreatedAt(java.time.LocalDateTime.now());
        todoRepository.save(todo);
    }

    @Override
    public List<Todo> getTodosByPriority(String userName, Todo.Priority priority) {
        return todoRepository.findByUserNameAndPriority(userName, priority);
    }
}
