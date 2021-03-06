package bg.sofia.tu.task;

import bg.sofia.tu.account.Account;
import bg.sofia.tu.account.AccountRepository;
import bg.sofia.tu.comment.Comment;
import bg.sofia.tu.enums.State;
import bg.sofia.tu.task.priority.Priority;
import bg.sofia.tu.task.priority.PriorityRepository;
import bg.sofia.tu.task.resolution.Resolution;
import bg.sofia.tu.task.resolution.ResolutionRepository;
import bg.sofia.tu.task.type.Type;
import bg.sofia.tu.task.type.TypeRepository;
import bg.sofia.tu.utils.TimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    private ArrayList<String> globalErrors = new ArrayList<>();


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
        model.addAttribute("allTypes", typeRepository.findAllByEnabled(true));
        model.addAttribute("allPriorities", priorityRepository.findAllByEnabled(true));
        model.addAttribute("allResolution", resolutionRepository.findAllByEnabled(true));

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
        task.setPriority(priorityRepository.findOneByValue(taskRequest.getPriority()));
        task.setType(typeRepository.findOneByValue(taskRequest.getType()));
        task.setResolution(resolutionRepository.findOneByValue(taskRequest.getResolution()));
        task.setEstimatedWork(TimeConverter.convertTimeToString(taskRequest.getWeeks(), taskRequest.getDays(), taskRequest.getHours()));
        task.setLoggedWork(TimeConverter.convertHoursToString(0));

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

    @RequestMapping("/view/{id}")
    public String view(@PathVariable long id, Model model) {
        Task currentTask = taskRepository.findOneById(id);

        setManageTaskAttributes(model, currentTask);

        return "manage_task";
    }

    @RequestMapping("/log")
    public String logWork(@ModelAttribute("work") Work work, Model model) {
        Task currentTask = taskRepository.findOneById(work.getId());

        if(work.getHours() == 0 && work.getDays() == 0 && work.getWeeks() == 0) {
            return showError("Invalid logged time: 0 week, 0 days, 0 hours.", currentTask, model);
        }

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(currentUser.getUsername().compareTo(currentTask.getAssignee().getUsername()) != 0) {
            String error = "Only the owner of task can log work!";
            return showError(error, currentTask, model);
        }

        int worked = TimeConverter.convertStringToHours(currentTask.getLoggedWork());
        int loggedWork = (work.getWeeks() * 168) + (work.getDays() * 24) + work.getHours();
        currentTask.setLoggedWork(TimeConverter.convertHoursToString(worked + loggedWork));
        currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            return showError("Could not update logged time!", currentTask, model);
        }

        return "redirect:/tasks/view/" + currentTask.getId();
    }

    @RequestMapping(value = "/createComment", method = RequestMethod.POST)
    public String createComment(@ModelAttribute("commentRequest") CommentRequest commentRequest, final Model model) {
        if(commentRequest.getMessage().trim().length() == 0) {
            return showError("Comment should not be empty!", taskRepository.findOneById(commentRequest.getTaskId()), model);
        }

        User creator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task currentTask = taskRepository.findOneById(commentRequest.getTaskId());

        Comment comment = new Comment();
        comment.setUser(findAccount(creator.getUsername()));
        comment.setTime(new Timestamp(System.currentTimeMillis()));
        comment.setTask(currentTask);
        comment.setMessage(cut(commentRequest.getMessage(), 512));

        currentTask.getComments().add(comment);

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            return showError("Could not update task comments!", currentTask, model);
        }

        return "redirect:/tasks/view/" + currentTask.getId();
    }

    @RequestMapping("delete/{taskId}/comment/{commentId}")
    public String delete(@PathVariable long taskId, @PathVariable long commentId, final Model model) {
        Task currentTask = taskRepository.findOneById(taskId);

        for(Comment comment : currentTask.getComments()) {
            if(comment.getId() == commentId) {
                User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if(currentUser.getUsername().compareTo(comment.getUser().getUsername()) != 0) {
                    return showError("Only the owner of the comment can delete it!", currentTask, model);
                }

                currentTask.getComments().remove(comment);
                break;
            }
        }

        try {
            taskRepository.save(currentTask);
        } catch (Exception ex) {
            ex.printStackTrace();
            return showError("Could not delete task comments!", currentTask, model);
        }

        return "redirect:/tasks/view/" + currentTask.getId();
    }

    @RequestMapping(value = "/update/priority/{id}")
    @ResponseBody
    public String updatePriorityById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        if(value != null && value.trim().length() > 0) {
            Task currentTask = taskRepository.findOneById(id);

            if (currentTask.getPriority().getValue().toLowerCase().compareTo(value.toLowerCase()) != 0) {
                Priority prior = priorityRepository.findOneByValue(value);
                currentTask.setPriority(prior);
                currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

                try {
                    taskRepository.save(currentTask);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    model.addAttribute("globalErrors", Arrays.asList("Could not update task priority!"));
                    return "error";
                }
            }
        }
        return "success";
    }

    @RequestMapping(value = "/update/type/{id}")
    @ResponseBody
    public String updateTypeById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        if(value != null && value.trim().length() > 0) {
            Task currentTask = taskRepository.findOneById(id);

            if (currentTask.getType().getValue().toLowerCase().compareTo(value.toLowerCase()) != 0) {
                Type type = typeRepository.findOneByValue(value);
                currentTask.setType(type);
                currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

                try {
                    taskRepository.save(currentTask);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    model.addAttribute("globalErrors", Arrays.asList("Could not update task type!"));
                    return "error";
                }
            }
        }
        return "success";
    }

    @RequestMapping(value = "/update/state/{id}")
    @ResponseBody
    public String updateStateById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        if(value != null && value.trim().length() > 0) {
            State state = State.valueOf(value.trim().replace(" ", "_"));

            if (!updateTaskState(model, state, id)) return "error";
        }
        return "success";
    }

    @RequestMapping(value = "/update/assignee/{id}")
    @ResponseBody
    public String updateAssigneeById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        if(value != null && value.trim().length() > 0) {
            Task currentTask = taskRepository.findOneById(id);

            if (currentTask.getAssignee().getUsername().toLowerCase().compareTo(value.toLowerCase()) != 0) {
                Account account = accountRepository.findOneByUsername(value);
                currentTask.setAssignee(account);
                currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

                try {
                    taskRepository.save(currentTask);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    model.addAttribute("globalErrors", Arrays.asList("Could not update task assignee!"));
                    return "error";
                }
            }
        }
        return "success";
    }

    @RequestMapping(value = "/update/resolution/{id}")
    @ResponseBody
    public String updateResolutionById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        if(value != null && value.trim().length() > 0) {
            Task currentTask = taskRepository.findOneById(id);

            if (currentTask.getResolution().getValue().toLowerCase().compareTo(value.toLowerCase()) != 0) {
                Resolution resolution = resolutionRepository.findOneByValue(value);
                currentTask.setResolution(resolution);
                currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

                try {
                    taskRepository.save(currentTask);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    model.addAttribute("globalErrors", Arrays.asList("Could not update task resolution!"));
                    return "error";
                }
            }
        }
        return "success";
    }

    @RequestMapping(value = "/update/description/{id}")
    @ResponseBody
    public String updateDescriptionById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        if(value != null && value.trim().length() > 0) {
            Task currentTask = taskRepository.findOneById(id);

            if (currentTask.getDescription().compareTo(value) != 0) {
                currentTask.setDescription(cut(value, 2000));
                currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

                try {
                    taskRepository.save(currentTask);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    model.addAttribute("globalErrors", Arrays.asList("Could not update task description!"));
                    return "error";
                }
            }
        }
        return "success";
    }

    @RequestMapping(value = "/update/summary/{id}")
    @ResponseBody
    public String updateSummaryById(@PathVariable long id, @RequestParam(value = "value") String value, Model model) {
        if(value != null && value.trim().length() > 0) {
            Task currentTask = taskRepository.findOneById(id);

            if (currentTask.getSummary().compareTo(value) != 0) {
                currentTask.setSummary(cut(value, 255));
                currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

                try {
                    taskRepository.save(currentTask);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    model.addAttribute("globalErrors", Arrays.asList("Could not update task summary!"));
                    return "error";
                }
            }
        }
        return "success";
    }


    @RequestMapping(value = "/update/state")
    @ResponseBody
    public String updateState(@RequestBody UpdateStateRequest updateStateRequest, Model model) {
        State state = State.valueOf(updateStateRequest.getState());
        long id = updateStateRequest.getId();

        if (!updateTaskState(model, state, id)) return "error";

        return "tasks";
    }

    private boolean updateTaskState(Model model, State state, long id) {
        Task currentTask = taskRepository.findOneById(id);

        if(currentTask.getState() != state) {
            if (currentTask.getState() == State.DONE) {
                currentTask.setReopened(new Timestamp(System.currentTimeMillis()));
            }
            currentTask.setState(state);
            currentTask.setUpdated(new Timestamp(System.currentTimeMillis()));

            if (currentTask.getState() == State.DONE) {
                currentTask.setResolved(new Timestamp(System.currentTimeMillis()));
            }

            try {
                taskRepository.save(currentTask);
            } catch (Exception ex) {
                ex.printStackTrace();
                model.addAttribute("globalErrors", Arrays.asList("Could not update task state!"));
                return false;
            }
        }
        return true;
    }

    private void setManageTaskAttributes(Model model, Task currentTask) {
        model.addAttribute("task", currentTask);
        model.addAttribute("allTypes", typeRepository.findAll());
        model.addAttribute("allPriorities", priorityRepository.findAll());
        model.addAttribute("allResolutions", resolutionRepository.findAll());
        model.addAttribute("allStates", Arrays.asList(State.values()));
        model.addAttribute("commentRequest", new CommentRequest());
        model.addAttribute("work", new Work());

        if(!globalErrors.isEmpty()) {
            model.addAttribute("globalErrors", new ArrayList<>(globalErrors));
            globalErrors.clear();
        }
    }

    private String showError(String error, Task currentTask, Model model) {
        globalErrors.add(error);
        return "redirect:/tasks/view/" + currentTask.getId();
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

    private String cut(String value, int size) {
        if(value.length() <= size) {
            return value;
        }

        return value.substring(0, size);
    }

    public static class TaskRequest {

        private long id;

        private String type;

        private String summary;

        private String description;

        private String assignee;

        private String priority;

        private String resolution;

        private int weeks;

        private int days;

        private int hours;


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

        public int getWeeks() {
            return weeks;
        }

        public void setWeeks(int weeks) {
            this.weeks = weeks;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
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
            sb.append(", weeks=").append(weeks);
            sb.append(", days=").append(days);
            sb.append(", hours=").append(hours);
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

    public static class CommentRequest {
        private long taskId;

        private String message;

        public long getTaskId() {
            return taskId;
        }

        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("CommentRequest{");
            sb.append("taskId=").append(taskId);
            sb.append(", message='").append(message).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    private static class Work {
        private long id;

        private int weeks;

        private int days;

        private int hours;


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getWeeks() {
            return weeks;
        }

        public void setWeeks(int weeks) {
            this.weeks = weeks;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Work{");
            sb.append("id=").append(id);
            sb.append(", weeks=").append(weeks);
            sb.append(", days=").append(days);
            sb.append(", hours=").append(hours);
            sb.append('}');
            return sb.toString();
        }
    }
}
