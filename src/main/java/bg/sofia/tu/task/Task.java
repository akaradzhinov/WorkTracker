package bg.sofia.tu.task;

import bg.sofia.tu.comment.Comment;
import bg.sofia.tu.account.Account;
import bg.sofia.tu.enums.State;
import bg.sofia.tu.task.priority.Priority;
import bg.sofia.tu.task.resolution.Resolution;
import bg.sofia.tu.task.type.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/06/2016 @ 20:28.
 */
@Entity
@Table(name = "tasks")
public class Task {

    private long id;

    private Type type;

    private State state;

    private String summary;

    private String description;

    private Timestamp created;

    private Timestamp updated;

    private Timestamp resolved;

    private Timestamp reopened;

    private Account assignee;

    private Account reporter;

    private Priority priority;

    private List<Comment> comments;

    private Resolution resolution;

    private String estimatedWork;

    private String loggedWork;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Column(nullable = false)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Column(nullable = false, length = 255)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Column(nullable = false, length = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "create_date", nullable = false)
    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Column(name = "update_date")
    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @Column(name = "resolve_date")
    public Timestamp getResolved() {
        return resolved;
    }

    public void setResolved(Timestamp resolved) {
        this.resolved = resolved;
    }

    @Column(name = "reopened_date")
    public Timestamp getReopened() {
        return reopened;
    }

    public void setReopened(Timestamp reopened) {
        this.reopened = reopened;
    }

    @ManyToOne
    public Account getAssignee() {
        return assignee;
    }

    public void setAssignee(Account assignee) {
        this.assignee = assignee;
    }

    @ManyToOne
    public Account getReporter() {
        return reporter;
    }

    public void setReporter(Account reporter) {
        this.reporter = reporter;
    }

    @ManyToOne
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @ManyToOne
    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    @Column(name = "estimated_work", nullable = false)
    public String getEstimatedWork() {
        return estimatedWork;
    }

    public void setEstimatedWork(String estimatedWork) {
        this.estimatedWork = estimatedWork;
    }

    @Column(name = "logged_work", nullable = false)
    public String getLoggedWork() {
        return loggedWork;
    }

    public void setLoggedWork(String loggedWork) {
        this.loggedWork = loggedWork;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", state=").append(state);
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", created=").append(created);
        sb.append(", updated=").append(updated);
        sb.append(", resolved=").append(resolved);
        sb.append(", reopened=").append(reopened);
        sb.append(", assignee=").append(assignee);
        sb.append(", reporter=").append(reporter);
        sb.append(", priority=").append(priority);
        sb.append(", comments=").append(comments);
        sb.append(", resolution=").append(resolution);
        sb.append('}');
        return sb.toString();
    }
}
