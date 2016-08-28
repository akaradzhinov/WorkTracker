package bg.sofia.tu.account;

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

        account = new Account();
        model.addAttribute("account", account);

        return "create_account";
    }

    @RequestMapping(value = "/createUpdate", method = RequestMethod.POST)
    public String createAccount(@ModelAttribute("account") Account account, final Model model) {
        if(!ValidatorUtils.validateEmail(account.getEmail())) {
            model.addAttribute("globalErrors", Arrays.asList("Email must be valid!"));
            return "create_account";
        }

        if(account.getId() == null && account.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }

        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            model.addAttribute("globalErrors", Arrays.asList("Username or email already exist!"));
            return "create_account";
        }

        return "redirect:/accounts";
    }

    @RequestMapping("edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        final List<Account> accounts = this.getAllAccounts();
        for (final Account account : accounts) {
            if (account.getId() == id) {
                this.account = account;
                break;
            }
        }

        model.addAttribute("account", account);
        return "create_account";
    }

    @RequestMapping("state/{id}/{active}")
    public String changeAccountState(@PathVariable long id, @PathVariable boolean active, Model model) {
        Account selectedAccount = null;
        final List<Account> accounts = this.getAllAccounts();
        for (final Account account : accounts) {
            if (account.getId() == id) {
                selectedAccount = account;
                break;
            }
        }

        if(selectedAccount == null) {
            model.addAttribute("globalErrors", Arrays.asList("Could not find account for disabling!"));
            return "create_account";
        }

        selectedAccount.setEnabled(active);
        accountRepository.save(selectedAccount);

        return "redirect:/accounts";
    }

    private List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public class CreateAccountRequest {

        private String username;

        private String password;

        private String email;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("CreateAccountRequest [");
            sb.append("username='").append(username).append('\'');
            sb.append(", password='").append(password).append('\'');
            sb.append(", email='").append(email).append('\'');
            sb.append(']');
            return sb.toString();
        }
    }
}
