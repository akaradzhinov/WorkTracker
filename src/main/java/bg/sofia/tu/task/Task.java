package bg.sofia.tu.task;

import bg.sofia.tu.user.User;
import bg.sofia.tu.enums.Priority;
import bg.sofia.tu.enums.Type;

import java.util.Date;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 15/06/2016 @ 20:28.
 */
public class Task {

    private long id;

    private Type type;

    private String summary;

    private String description;

    private Date createDate;

    private User assignee;

    private User reporter;

    private Priority priority;

    private int points;
}
