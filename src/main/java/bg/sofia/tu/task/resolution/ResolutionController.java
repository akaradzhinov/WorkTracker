package bg.sofia.tu.task.resolution;

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
@RequestMapping("/resolutions")
public class ResolutionController {

    @Autowired
    private ResolutionRepository resolutionRepository;

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("allResolutions", resolutionRepository.findAll());

        return "resolutions";
    }

    @RequestMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("resolution", new Resolution());

        return "create_resolution";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Resolution selectedResolution = getSelectedResolution(id);

        if(selectedResolution == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find resolution for deletion!"));
            return "resolution";
        }

        model.addAttribute("resolution", selectedResolution);
        return "create_resolution";
    }

    @RequestMapping(value = "/manage", method = RequestMethod.POST)
    public String manage(@ModelAttribute("resolution") Resolution resolution, final Model model) {
        try {
            resolutionRepository.save(resolution);
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Something went wrong. Please contact support."));
            return "create_resolution";
        }

        return "redirect:/resolutions";
    }

    @RequestMapping("/state/{id}/{active}")
    public String changeState(@PathVariable int id, @PathVariable boolean active, Model model) {
        Resolution selectedResolution = getSelectedResolution(id);

        if(selectedResolution == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find resolution for disabling!"));
            return "resolutions";
        }

        selectedResolution.setEnabled(active);
        resolutionRepository.save(selectedResolution);

        return "redirect:/resolutions";
    }

    @RequestMapping(value = "/getForSelect", method = RequestMethod.GET)
    @ResponseBody
    public String getResolutions() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(Resolution resolution : getAllResolutions()) {
            builder.append("{\"id\":\"").append(resolution.getValue()).append("\", \"text\":\"").append(resolution.getValue()).append("\"},");
        }

        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");

        return builder.toString();
    }

    private Resolution getSelectedResolution(int id) {
        Resolution selectedResolution = null;
        final List<Resolution> resolutions = resolutionRepository.findAll();
        for (final Resolution resolution : resolutions) {
            if (resolution.getId() == id) {
                selectedResolution = resolution;
                break;
            }
        }
        return selectedResolution;
    }

    private List<Resolution> getAllResolutions() {
        return resolutionRepository.findAllByEnabled(true);
    }
}
