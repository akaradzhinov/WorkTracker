package bg.sofia.tu.account;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/08/2016 @ 20:28.
 */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct	
	protected void initialize() {
		if(accountRepository.findOneByUsername("user") == null) {
			save(new Account("Test User", "user", "demo", "test@abv.bg", "ROLE_USER", true));
		}

		if(accountRepository.findOneByUsername("admin") == null) {
			save(new Account("Grand Admin", "admin", "admin", "test1@abv.bg", "ROLE_ADMIN", true));
		}
	}

	public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
		return accountRepository.save(account);
	}
}
