package bg.sofia.tu.task;

import bg.sofia.tu.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/06/2016 @ 20:35.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByState(State state);
}
