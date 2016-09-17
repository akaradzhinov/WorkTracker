package bg.sofia.tu.task.priority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 15:48.
 */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PriorityService {

    @Autowired
    private PriorityRepository priorityRepository;

    @PostConstruct
    protected void initialize() {
        if(priorityRepository.findOneByValue("Highest") == null) {
            priorityRepository.save(new Priority("Highest", "Work with highest priority", 5, true));
        }

        if(priorityRepository.findOneByValue("High") == null) {
            priorityRepository.save(new Priority("High", "Work with high priority", 4, true));
        }

        if(priorityRepository.findOneByValue("Medium") == null) {
            priorityRepository.save(new Priority("Medium", "Work with medium priority", 3, true));
        }

        if(priorityRepository.findOneByValue("Low") == null) {
            priorityRepository.save(new Priority("Low", "Work with low priority", 2, true));
        }

        if(priorityRepository.findOneByValue("Lowest") == null) {
            priorityRepository.save(new Priority("Lowest", "Work with lowest priority", 1, true));
        }
    }

}
