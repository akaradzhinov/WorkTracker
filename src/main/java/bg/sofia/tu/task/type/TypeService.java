package bg.sofia.tu.task.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * author: Aleksandar Karadzhinov
 * email: alexandar.karadzhinov@cayetanogaming.com
 * <p/>
 * created on 09/09/2016 @ 14:48.
 */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @PostConstruct
    protected void initialize() {
        if(typeRepository.findOneByValue("Story") == null) {
            typeRepository.save(new Type("Story", "Used for stories", true));
        }

        if(typeRepository.findOneByValue("Task") == null) {
            typeRepository.save(new Type("Task", "Used for tasks", true));
        }

        if(typeRepository.findOneByValue("Bug") == null) {
            typeRepository.save(new Type("Bug", "Used for bugs", true));
        }

        if(typeRepository.findOneByValue("Improvement") == null) {
            typeRepository.save(new Type("Improvement", "Used for improvements", true));
        }

        if(typeRepository.findOneByValue("Test") == null) {
            typeRepository.save(new Type("Test", "Used for tests", true));
        }
    }

}
