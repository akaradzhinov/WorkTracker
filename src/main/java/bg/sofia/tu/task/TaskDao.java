package bg.sofia.tu.task;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author: Aleksandar Karadzhinov
 * email: aleksandar.karadjinov@gmail.com
 * <p/>
 * created on 15/06/2016 @ 20:35.
 */
public interface TaskDao extends JpaRepository<Task, Long> {
}
