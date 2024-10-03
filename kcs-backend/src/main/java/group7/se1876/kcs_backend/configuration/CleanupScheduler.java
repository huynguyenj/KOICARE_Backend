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
    @Scheduled(cron = "0 0 0 * * *") //Run at 00:00 every day
    public void cleanTokenData(){
        invalidTokenService.deleteExpiredTime();
    }
}
