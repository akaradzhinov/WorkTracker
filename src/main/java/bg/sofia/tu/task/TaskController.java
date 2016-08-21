package bg.sofia.tu.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/06/2016 @ 20:35.
 */
@RestController
@RequestMapping(value = "/task")
public class TaskController {

    @Autowired
    private TaskDao taskDao;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @ResponseBody
    public List<Task> getTasks() {
        return taskDao.findAll();
    }
}
