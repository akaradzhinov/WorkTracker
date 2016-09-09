package bg.sofia.tu.task.type;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 14:48.
 */
public interface TypeRepository extends JpaRepository<Type, Integer> {

    Type findOneByValue(String value);
}
