package bg.sofia.tu.task;

import org.springframework.data.repository.CrudRepository;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 15/06/2016 @ 20:35.
 */
public interface TaskRepository extends CrudRepository<Task, Long> {
}
