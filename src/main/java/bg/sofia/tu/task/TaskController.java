package bg.sofia.tu.task;

import bg.sofia.tu.account.Account;
import bg.sofia.tu.account.AccountRepository;
import bg.sofia.tu.enums.State;
import bg.sofia.tu.task.priority.Priority;
import bg.sofia.tu.task.priority.PriorityRepository;
import bg.sofia.tu.task.type.Type;
import bg.sofia.tu.task.type.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.util.Arrays;
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

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private PriorityRepository priorityRepository;


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
        model.addAttribute("allTypes", typeRepository.findAll());
        model.addAttribute("allPriorities", priorityRepository.findAll());

        model.addAttribute("task", new TaskRequest());

        return "create_task";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("task") TaskRequest taskRequest, final Model model) {
        Task task = new Task();
        task.setId(taskRequest.getId());
        task.setState(State.TODO);
        task.setCreated(new Timestamp(System.currentTimeMillis()));
        task.setDescription(taskRequest.getDescription());
        task.setSummary(taskRequest.getSummary());
        task.setPoints(taskRequest.getPoints());
        task.setPriority(priorityRepository.findOneByValue(taskRequest.getPriority()));
        task.setType(typeRepository.findOneByValue(taskRequest.getType()));

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

    @RequestMapping(value = "/update/priority/{id}/{value}/")
    public String updatePriority(@PathVariable long id, @PathVariable String value, Model model) {
        Priority prior = priorityRepository.findOneByValue(value);
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setPriority(prior);

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task priority!"));
        }

        return "create_task";
    }

    @RequestMapping(value = "/update/type/{id}/{value}/")
    public String updateType(@PathVariable long id, @PathVariable String value, Model model) {
        Type type = typeRepository.findOneByValue(value);
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setType(type);

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task type!"));
        }

        return "create_task";
    }

    @RequestMapping(value = "/update/state/{id}/{value}/")
    public String updateState(@PathVariable long id, @PathVariable State state, Model model) {
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setState(state);

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task state!"));
        }

        return "create_task";
    }

    @RequestMapping("/open/{id}")
    public String view(@PathVariable long id, Model model) {
        Task currentTask = taskRepository.findOneById(id);

        model.addAttribute("task", currentTask);
        return "manage_task";
    }


    private List<Task> getToDoTasks() {
        return taskRepository.findAllByStateOrderByPriorityPowerDesc(State.TODO);
    }

    private List<Task> getInProgressTasks() {
        return taskRepository.findAllByStateOrderByPriorityPowerDesc(State.IN_PROGRESS);
    }

    private List<Task> getDoneTasks() {
        return taskRepository.findAllByStateOrderByPriorityPowerDesc(State.DONE);
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

        private String type;

        private String summary;

        private String description;

        private String assignee;

        private String priority;

        private int points;


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
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

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
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
