package bg.sofia.tu.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/08/2016 @ 20:28.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findOneByUsername(String username);
}