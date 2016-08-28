package bg.sofia.tu.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private TaskDao taskDao;

//    @RequestMapping(method = RequestMethod.GET, value = "/list")
//    @ResponseBody
//    public List<Task> getTasks() {
//        return taskDao.findAll();
//    }

    @RequestMapping
    public String index(Model model) {
        return "create_task";
    }

}
