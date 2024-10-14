package group7.se1876.kcs_backend.configuration;

import group7.se1876.kcs_backend.service.InvalidTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanupScheduler {

    @Autowired
    private InvalidTokenService invalidTokenService;

    //Clean up logout token
    @Scheduled(cron = "0 */1 * * * *") //Run at every 1 minutes
    public void cleanTokenData(){
        System.out.println("Scheduled task is running...");
        invalidTokenService.deleteExpiredTime();
    }
}
