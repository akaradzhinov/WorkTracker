package bg.sofia.tu.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 22/08/2016 @ 14:58.
 */
@Controller
public class LoginContorller {

    @RequestMapping("/login")
    String login(Model model) {
        return "login";
    }
}
