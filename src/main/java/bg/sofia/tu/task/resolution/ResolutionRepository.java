package bg.sofia.tu.task.resolution;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 14:48.
 */
public interface ResolutionRepository extends JpaRepository<Resolution, Integer> {

    Resolution findOneByValue(String value);

    List<Resolution> findAllByEnabled(boolean enabled);
}
