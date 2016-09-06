package bg.sofia.tu.task;

import bg.sofia.tu.account.Account;
import bg.sofia.tu.enums.Priority;
import bg.sofia.tu.enums.State;
import bg.sofia.tu.enums.Type;

import javax.persistence.*;
import java.util.Date;

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

    private Date createDate;

    private Account assignee;

    private Account reporter;

    private Priority priority;

    private int points;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false)
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

    @Column(nullable = false)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Column(nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "create_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @OneToOne()
    @JoinColumn(name = "assignee_id", nullable = false)
    public Account getAssignee() {
        return assignee;
    }

    public void setAssignee(Account assignee) {
        this.assignee = assignee;
    }

    @OneToOne()
    @JoinColumn(name = "reporter_id", nullable = false)
    public Account getReporter() {
        return reporter;
    }

    public void setReporter(Account reporter) {
        this.reporter = reporter;
    }

    @Column(nullable = false)
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Column(nullable = false)
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", state=").append(state);
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", assignee=").append(assignee);
        sb.append(", reporter=").append(reporter);
        sb.append(", priority=").append(priority);
        sb.append(", points=").append(points);
        sb.append('}');
        return sb.toString();
    }
}
