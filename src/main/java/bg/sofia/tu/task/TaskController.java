package bg.sofia.tu.task;

import bg.sofia.tu.account.Account;
import bg.sofia.tu.account.AccountRepository;
import bg.sofia.tu.enums.Priority;
import bg.sofia.tu.enums.State;
import bg.sofia.tu.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/06/2016 @ 20:35.
 */
@Controller
@RequestMapping(value = "/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AccountRepository accountRepository;


    @RequestMapping
    public String index(Model model) {
        model.addAttribute("toDoTasks", getToDoTasks());
        model.addAttribute("inProgressTasks", getInProgressTasks());
        model.addAttribute("doneTasks", getDoneTasks());

        return "tasks";
    }

    @RequestMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("allAccountUsernames", getAccountUsernames());

        model.addAttribute("task", new TaskRequest());

        return "create_task";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("task") TaskRequest taskRequest, final Model model) {
        Task task = new Task();
        task.setId(taskRequest.getId());
        task.setState(State.TODO);
        task.setCreateDate(new Timestamp(new Date().getTime()));
        task.setDescription(taskRequest.getDescription());
        task.setSummary(taskRequest.getSummary());
        task.setPoints(taskRequest.getPoints());
        task.setPriority(taskRequest.getPriority());
        task.setType(taskRequest.getType());

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            task.setReporter(findAccount(currentUser.getUsername()));
            task.setAssignee(findAccount(taskRequest.getAssignee()));
        } catch (UsernameNotFoundException ex) {
            model.addAttribute("globalErrors", Arrays.asList(ex.getMessage()));
            return "create_task";
        }

        try {
            taskRepository.save(task);
        } catch (Exception ex) {
            model.addAttribute("globalErrors", Arrays.asList("Something went wrong!"));
            return "create_task";
        }

        return "redirect:/tasks";
    }

    private List<Task> getToDoTasks() {
        return taskRepository.findAllByState(State.TODO);
    }

    private List<Task> getInProgressTasks() {
        return taskRepository.findAllByState(State.IN_PROGRESS);
    }

    private List<Task> getDoneTasks() {
        return taskRepository.findAllByState(State.DONE);
    }


    private List<String> getAccountUsernames() {
        return accountRepository.listAccoutUsernames();
    }

    private Account findAccount(String username) {
        for (Account account : accountRepository.findAllByEnabled(true)) {
            if (account.getUsername().equals(username)) {
                return account;
            }
        }

        throw new UsernameNotFoundException("Could not find user with username=" + username);
    }

    public static class TaskRequest {

        private long id;

        private Type type;

        private String summary;

        private String description;

        private String assignee;

        private Priority priority;

        private int points;


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAssignee() {
            return assignee;
        }

        public void setAssignee(String assignee) {
            this.assignee = assignee;
        }

        public Priority getPriority() {
            return priority;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("TaskRequest{");
            sb.append("id=").append(id);
            sb.append(", type=").append(type);
            sb.append(", summary='").append(summary).append('\'');
            sb.append(", description='").append(description).append('\'');
            sb.append(", assignee='").append(assignee).append('\'');
            sb.append(", priority=").append(priority);
            sb.append(", points=").append(points);
            sb.append('}');
            return sb.toString();
        }
    }
}
