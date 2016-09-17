package bg.sofia.tu.task.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 14:48.
 */
@Controller
@RequestMapping("/types")
public class TypeController {

    @Autowired
    private TypeRepository typeRepository;

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("allTypes", typeRepository.findAll());

        return "types";
    }

    @RequestMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("type", new Type());

        return "create_type";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Type selectedType = getSelectedType(id);

        if(selectedType == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find type for deletion!"));
            return "types";
        }

        model.addAttribute("type", selectedType);
        return "create_type";
    }

    @RequestMapping(value = "/manage", method = RequestMethod.POST)
    public String manage(@ModelAttribute("type") Type type, final Model model) {
        try {
            typeRepository.save(type);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Something went wrong. Please contact support."));
            return "create_type";
        }

        return "redirect:/types";
    }

    @RequestMapping("/state/{id}/{active}")
    public String changeState(@PathVariable int id, @PathVariable boolean active, Model model) {
        Type selectedType = getSelectedType(id);

        if(selectedType == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find type for disabling!"));
            return "types";
        }

        selectedType.setEnabled(active);
        typeRepository.save(selectedType);

        return "redirect:/types";
    }

    @RequestMapping(value = "/getForSelect", method = RequestMethod.GET)
    @ResponseBody
    public String getTypes() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(Type type : getAllTypes()) {
            builder.append("{\"id\":\"").append(type.getValue()).append("\", \"text\":\"").append(type.getValue()).append("\"},");
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");

        return builder.toString();
    }

    private Type getSelectedType(int id) {
        Type selectedType = null;
        final List<Type> types = typeRepository.findAll();
        for (final Type type : types) {
            if (type.getId() == id) {
                selectedType = type;
                break;
            }
        }
        return selectedType;
    }

    private List<Type> getAllTypes() {
        return typeRepository.findAllByEnabled(true);
    }
}
