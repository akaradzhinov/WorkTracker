package bg.sofia.tu.task.priority;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 15:48.
 */
public interface PriorityRepository extends JpaRepository<Priority, Integer> {

    Priority findOneByValue(String value);

    List<Priority> findAllByEnabled(boolean enabled);
}
