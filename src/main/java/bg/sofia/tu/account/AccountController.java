package bg.sofia.tu.account;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import bg.sofia.tu.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Account account = new Account();


    @RequestMapping
    public String index(Model model) {
        model.addAttribute("allAccounts", getAllAccounts());

        return "accounts";
    }

    @RequestMapping(value = "/create")
    public String create(Model model) {
        account = new Account();
        model.addAttribute("account", account);

        return "create_account";
    }

    @RequestMapping(value = "/manage", method = RequestMethod.POST)
    public String manage(@ModelAttribute("account") Account account, final Model model) {
        if(!ValidatorUtils.validateEmail(account.getEmail())) {
            model.addAttribute("globalErrors", Arrays.asList("Email must be valid!"));
            return "create_account";
        }

        if(account.getId() == 0 && account.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }

        try {
            if(account.getId() > 0) {
                accountRepository.updateInfo(account.getUsername(), account.getEmail(), account.getRole(), account.getEnabled(), account.getId());
            } else {
                account.setCreated(new Timestamp(System.currentTimeMillis()));
                accountRepository.save(account);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("globalErrors", Arrays.asList("Username or email already exist!"));
            return "create_account";
        }

        return "redirect:/accounts";
    }

    @RequestMapping("/edit/{username}")
    public String edit(@PathVariable String username, Model model) {
        this.account = accountRepository.findOneByUsername(username);

        if(account == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find account with username=" + username));
            return "accounts";
        }

        model.addAttribute("account", account);

        return "create_account";
    }

    @RequestMapping("/state/{id}/{active}")
    public String changeState(@PathVariable long id, @PathVariable boolean active, Model model) {
        Account selectedAccount = accountRepository.findOneById(id);

        if(selectedAccount == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find account for disabling!"));
            return "accounts";
        }

        selectedAccount.setEnabled(active);
        accountRepository.save(selectedAccount);

        return "redirect:/accounts";
    }

    private List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
