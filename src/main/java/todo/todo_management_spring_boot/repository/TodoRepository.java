package todo.todo_management_spring_boot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import todo.todo_management_spring_boot.model.Todo;
import todo.todo_management_spring_boot.model.Todo.Priority;


@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
	
	List < Todo > findByUserName(String user);
	
	// Find all todos by completion status
    List<Todo> findByCompleted(boolean completed);

    // Find todos by priority
    List<Todo> findByPriority(Priority priority);

    // Find todos due before a specific date
    List<Todo> findByTargetDateBefore(Date date);

    // Find todos by priority and completion status
    List<Todo> findByPriorityAndCompleted(Priority priority, boolean completed);
    
    List<Todo> findByUserNameAndPriority(String userName, Todo.Priority priority);

}
