package todo.todo_management_spring_boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import todo.todo_management_spring_boot.model.Todo;
import todo.todo_management_spring_boot.service.ITodoService;

@Controller
public class WelcomeController {
	
    @Autowired
    private ITodoService todoService;

    @GetMapping("/")
    public String showWelcomePage(ModelMap model) {
    	String username = getLoggedInUserName();
        List<Todo> todos = todoService.getTodosByUser(username);

        long total = todos.size();
        long completed = todos.stream().filter(Todo::isCompleted).count();
        long pending = total - completed;

        model.put("name", username);
        model.put("todosCount", total);
        model.put("completedCount", completed);
        model.put("pendingCount", pending);

        return "welcome";
    }

    private String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : principal.toString();
    }
}
