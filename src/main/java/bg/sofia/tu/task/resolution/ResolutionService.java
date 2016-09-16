package bg.sofia.tu.task.resolution;

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
public class ResolutionService {

    @Autowired
    private ResolutionRepository resolutionRepository;

    @PostConstruct
    protected void initialize() {
        if(resolutionRepository.findOneByValue("Unresolved") == null) {
            resolutionRepository.save(new Resolution("Unresolved", "Task has no resolution"));
        }

        if(resolutionRepository.findOneByValue("Done") == null) {
            resolutionRepository.save(new Resolution("Done", "Task is done"));
        }

        if(resolutionRepository.findOneByValue("Won't fix") == null) {
            resolutionRepository.save(new Resolution("Won't fix", "Task won't be done"));
        }

        if(resolutionRepository.findOneByValue("Duplicate") == null) {
            resolutionRepository.save(new Resolution("Duplicate", "Task is duplicate to another"));
        }
    }

}
