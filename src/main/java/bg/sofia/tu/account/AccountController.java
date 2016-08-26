package bg.sofia.tu.account;
import java.security.Principal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/08/2016 @ 20:28.
 */
@Controller
@RequestMapping("/accounts")
class AccountController {

    private static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "account/current", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public Account currentAccount(Principal principal) {
        Assert.notNull(principal);
        return accountRepository.findOneByUsername(principal.getName());
    }

    @RequestMapping(value = "account/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Account account(@PathVariable("id") Long id) {
        return accountRepository.findOne(id);
    }

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("request", new CreateAccountRequest());
        return "create_account";
    }

    @RequestMapping(value = "/createUpdate", method = RequestMethod.POST)
    public String createAccount(@ModelAttribute("request") CreateAccountRequest createAccountRequest, final Model model) {
        Account account = new Account(createAccountRequest.getUsername(), createAccountRequest.getPassword(), ROLE_USER);
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            model.addAttribute("globalErrors", Arrays.asList("Username already exists!"));
            return "create_account";
        }

        return "redirect:/accounts";
    }
}
