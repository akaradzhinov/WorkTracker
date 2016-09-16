package bg.sofia.tu.comment;

import bg.sofia.tu.account.Account;
import bg.sofia.tu.task.Task;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 10:42.
 */
@Entity
@Table(name = "comments")
public class Comment {

    private long id;

    private String message;

    private Account user;

    private Timestamp time;

    private Task task;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false, length = 512)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @OneToOne()
    @JoinColumn(name = "user_id", nullable = false)
    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    @Column(nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Comment{");
        sb.append("id=").append(id);
        sb.append(", message='").append(message).append('\'');
        sb.append(", user=").append(user);
        sb.append(", time=").append(time);
        sb.append(", taskId=").append(task.getId());
        sb.append('}');
        return sb.toString();
    }
}
