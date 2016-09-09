package bg.sofia.tu.task.priority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 15:48.
 */
@Controller
@RequestMapping("/priorities")
public class PriorityController {

    @Autowired
    private PriorityRepository priorityRepository;

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("allPriorities", priorityRepository.findAll());

        return "priorities";
    }

    @RequestMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("priority", new Priority());

        return "create_priority";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Priority selectedPriority = getSelectedPriority(id);

        if(selectedPriority == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find priority for deletion!"));
            return "priorities";
        }

        model.addAttribute("priority", selectedPriority);
        return "create_priority";
    }

    @RequestMapping(value = "/manage", method = RequestMethod.POST)
    public String manage(@ModelAttribute("priority") Priority priority, final Model model) {
        try {
            priorityRepository.save(priority);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Something went wrong. Please contact support."));
            return "create_priority";
        }

        return "redirect:/priorities";
    }

    @RequestMapping("delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        Priority selectedPriority = getSelectedPriority(id);

        if(selectedPriority == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find priority for deletion!"));
            return "priorities";
        }

        priorityRepository.delete(selectedPriority);

        return "redirect:/priorities";
    }

    private Priority getSelectedPriority(int id) {
        Priority selectedPriority = null;
        final List<Priority> priorities = priorityRepository.findAll();
        for (final Priority priority : priorities) {
            if (priority.getId() == id) {
                selectedPriority = priority;
                break;
            }
        }
        return selectedPriority;
    }
}
