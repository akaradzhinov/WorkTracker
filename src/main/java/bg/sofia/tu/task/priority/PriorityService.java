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
            priorityRepository.save(new Priority("Highest", "Should be done ASAP", 5, true));
        }

        if(priorityRepository.findOneByValue("High") == null) {
            priorityRepository.save(new Priority("High", "Should be done soon", 4, true));
        }

        if(priorityRepository.findOneByValue("Medium") == null) {
            priorityRepository.save(new Priority("Medium", "Important, but can wait", 3, true));
        }

        if(priorityRepository.findOneByValue("Low") == null) {
            priorityRepository.save(new Priority("Low", "Not so important", 2, true));
        }

        if(priorityRepository.findOneByValue("Lowest") == null) {
            priorityRepository.save(new Priority("Lowest", "Consider doing when there is available time", 1, true));
        }
    }

}
