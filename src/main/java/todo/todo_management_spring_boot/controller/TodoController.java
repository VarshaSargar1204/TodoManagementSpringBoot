package todo.todo_management_spring_boot.controller;

import jakarta.validation.Valid;
import todo.todo_management_spring_boot.model.Todo;
import todo.todo_management_spring_boot.service.ITodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class TodoController {

    @Autowired
    private ITodoService todoService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @GetMapping("/list-todos")
    public String listTodos(ModelMap model) {
        String username = getLoggedInUserName();
        List<Todo> todos = todoService.getTodosByUser(username);
        model.put("todos", todos);
        return "list-todos";
    }

    @GetMapping("/add-todo")
    public String showAddTodoPage(ModelMap model) {
        model.addAttribute("todo", new Todo());
        return "todo";
    }

    @PostMapping("/add-todo")
    public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }
        todo.setUserName(getLoggedInUserName());
        todo.setCreatedAt(java.time.LocalDateTime.now());
        todoService.saveTodo(todo);
        return "redirect:/list-todos";
    }

    @GetMapping("/update-todo")
    public String showUpdateTodoPage(@RequestParam long id, ModelMap model) {
        Todo todo = todoService.getTodoById(id).orElse(null);
        if (todo == null || !todo.getUserName().equals(getLoggedInUserName())) {
            return "redirect:/list-todos";
        }
        model.put("todo", todo);
        return "todo";
    }

    @PostMapping("/update-todo")
    public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }
        todo.setUserName(getLoggedInUserName());
        todoService.updateTodo(todo);
        return "redirect:/list-todos";
    }

    @GetMapping("/delete-todo")
    public String deleteTodo(@RequestParam long id) {
        Todo todo = todoService.getTodoById(id).orElse(null);
        if (todo != null && todo.getUserName().equals(getLoggedInUserName())) {
            todoService.deleteTodo(id);
        }
        return "redirect:/list-todos";
    }

    @GetMapping("/filter-by-priority")
    public String filterByPriority(@RequestParam Todo.Priority priority, ModelMap model) {
        String username = getLoggedInUserName();
        List<Todo> todos = todoService.getTodosByPriority(username, priority);
        model.put("todos", todos);
        return "list-todos";
    }

    private String getLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : principal.toString();
    }
}