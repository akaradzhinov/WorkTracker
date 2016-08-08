package bg.sofia.tu.user;

import org.springframework.data.repository.CrudRepository;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 15/06/2016 @ 20:35.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
