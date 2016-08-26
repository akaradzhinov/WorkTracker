package bg.sofia.tu.layout;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LayoutController {

    @RequestMapping("/layout")
    String index(Model model) {
        return "layout";
    }
}
