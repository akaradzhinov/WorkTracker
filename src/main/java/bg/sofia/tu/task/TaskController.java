package bg.sofia.tu.task;

import bg.sofia.tu.account.Account;
import bg.sofia.tu.account.AccountRepository;
import bg.sofia.tu.enums.State;
import bg.sofia.tu.task.priority.Priority;
import bg.sofia.tu.task.priority.PriorityRepository;
import bg.sofia.tu.task.resolution.Resolution;
import bg.sofia.tu.task.resolution.ResolutionRepository;
import bg.sofia.tu.task.type.Type;
import bg.sofia.tu.task.type.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private ResolutionRepository resolutionRepository;


    @RequestMapping
    public String index(Model model) {
        model.addAttribute("toDoTasks", getToDoTasks());
        model.addAttribute("inProgressTasks", getInProgressTasks());
        model.addAttribute("doneTasks", getDoneTasks());
        model.addAttribute("canceledTasks", getCanceledTasks());

        return "tasks";
    }

    @RequestMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("allAccountUsernames", getAccountUsernames());
        model.addAttribute("allTypes", typeRepository.findAll());
        model.addAttribute("allPriorities", priorityRepository.findAll());
        model.addAttribute("allResolution", resolutionRepository.findAll());

        model.addAttribute("task", new TaskRequest());

        return "create_task";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("task") TaskRequest taskRequest, final Model model) {
        Task task = new Task();
        task.setId(taskRequest.getId());
        task.setState(State.TO_DO);
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

    @RequestMapping(value = "/update/priority/{id}")
    @ResponseBody
    public String updatePriorityById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        Priority prior = priorityRepository.findOneByValue(value);
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setPriority(prior);
        currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task priority!"));
        }

        return "success";
    }

    @RequestMapping(value = "/update/type/{id}")
    @ResponseBody
    public String updateTypeById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        Type type = typeRepository.findOneByValue(value);
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setType(type);
        currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task type!"));
        }

        return "success";
    }

    @RequestMapping(value = "/update/state/{id}")
    @ResponseBody
    public String updateStateById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setState(State.valueOf(value.trim().replace(" ", "_")));
        currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task state!"));
        }

        return "success";
    }

    @RequestMapping(value = "/update/assignee/{id}")
    @ResponseBody
    public String updateAssigneeById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        Account account = accountRepository.findOneByUsername(value);
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setAssignee(account);
        currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task assignee!"));
        }

        return "success";
    }

    @RequestMapping(value = "/update/resolution/{id}")
    @ResponseBody
    public String updateResolutionById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        Resolution resolution = resolutionRepository.findOneByValue(value);
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setResolution(resolution);
        currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task resolution!"));
        }

        return "success";
    }

    @RequestMapping(value = "/update/description/{id}")
    @ResponseBody
    public String updateDescriptionById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        Task currentTask = taskRepository.findOneById(id);
        currentTask.setDescription(value);
        currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task resolution!"));
        }

        return "success";
    }

    @RequestMapping(value = "/update/state")
    @ResponseBody
    public String updateState(@RequestBody UpdateStateRequest updateStateRequest, Model model) {
        Task currentTask = taskRepository.findOneById(updateStateRequest.getId());
        currentTask.setState(State.valueOf(updateStateRequest.getState()));
        currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Could not update task state!"));
        }

        return "tasks";
    }

    @RequestMapping("/view/{id}")
    public String view(@PathVariable long id, Model model) {
        Task currentTask = taskRepository.findOneById(id);

        model.addAttribute("task", currentTask);
        model.addAttribute("allTypes", typeRepository.findAll());
        model.addAttribute("allPriorities", priorityRepository.findAll());
        model.addAttribute("allStates", Arrays.asList(State.values()));

        return "manage_task";
    }


    private List<Task> getToDoTasks() {
        return taskRepository.findAllByStateOrderByPriorityPowerDesc(State.TO_DO);
    }

    private List<Task> getInProgressTasks() {
        return taskRepository.findAllByStateOrderByPriorityPowerDesc(State.IN_PROGRESS);
    }

    private List<Task> getDoneTasks() {
        return taskRepository.findAllByStateOrderByPriorityPowerDesc(State.DONE);
    }

    private List<Task> getCanceledTasks() {
        return taskRepository.findAllByStateOrderByPriorityPowerDesc(State.CANCELED);
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

        private String resolution;

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

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
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
            sb.append(", type='").append(type).append('\'');
            sb.append(", summary='").append(summary).append('\'');
            sb.append(", description='").append(description).append('\'');
            sb.append(", assignee='").append(assignee).append('\'');
            sb.append(", priority='").append(priority).append('\'');
            sb.append(", resolution='").append(resolution).append('\'');
            sb.append(", points=").append(points);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class UpdateStateRequest {
        private long id;

        private String state;


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("UpdateStateRequest{");
            sb.append("id=").append(id);
            sb.append(", state='").append(state).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
