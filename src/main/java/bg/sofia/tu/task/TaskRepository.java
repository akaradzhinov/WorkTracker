package bg.sofia.tu.task;

import bg.sofia.tu.enums.State;
import bg.sofia.tu.task.priority.Priority;
import bg.sofia.tu.task.type.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/06/2016 @ 20:35.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByState(State state);

    Task findOneById(Long id);

    @Modifying
    @Transactional
    @Query("update Task set priority = ?1 where id = ?2")
    int updatePriority(Priority priority, long id);

    @Modifying
    @Transactional
    @Query("update Task set state = ?1 where id = ?2")
    int updateState(State state, long id);

    @Modifying
    @Transactional
    @Query("update Task set type = ?1 where id = ?2")
    int updateType(Type type, long id);
}
