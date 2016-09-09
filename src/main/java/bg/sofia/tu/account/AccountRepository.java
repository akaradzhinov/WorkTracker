package bg.sofia.tu.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/08/2016 @ 20:28.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findOneByUsername(String username);

	Account findOneById(Long id);

	@Modifying
	@Transactional
	@Query("update Account set username = ?1, email = ?2, role = ?3, enabled = ?4 where id = ?5")
	int updateInfo(String username, String email, String role, boolean enabled, long id);

    @Transactional
    @Query("select username from Account where enabled = 1")
	List<String> listAccoutUsernames();

    List<Account> findAllByEnabled(boolean enabled);
}