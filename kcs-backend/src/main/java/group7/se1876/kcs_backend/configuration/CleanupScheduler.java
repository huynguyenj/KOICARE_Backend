package group7.se1876.kcs_backend.configuration;

import group7.se1876.kcs_backend.entity.TrackingUserLogin;
import group7.se1876.kcs_backend.repository.TrackingUserRepository;
import group7.se1876.kcs_backend.service.InvalidTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class CleanupScheduler {

    @Autowired
    private InvalidTokenService invalidTokenService;
    @Autowired
    private TrackingUserRepository trackingUserRepository;

    //Clean up logout token
    @Scheduled(cron = "0 */1 * * * *") //Run at every 1 minutes
    public void cleanTokenData(){
        invalidTokenService.deleteExpiredTime();
    }

    //delete data tracking user depend on token expired time
    @Scheduled(cron = "0 */5 * * * ?")
    @Transactional
    public void deleteExpiredTrackingUsers() {
        Date now = new Date();
        try {
            List<TrackingUserLogin> sessions = trackingUserRepository.findAll();
            for(TrackingUserLogin session: sessions){
               Date loginTime = session.getLoginTime();
               long expiredTime = now.getTime() - loginTime.getTime();
               long timeTokenExpired = 3600000;
               if(expiredTime >= timeTokenExpired){
                   trackingUserRepository.delete(session);
               }
            }
//            Date currentTime = new Date();
//            trackingUserRepository.deleteAllTrackingUsers();
//            System.out.println("Expired tracking users deleted at: " + currentTime);
        } catch (Exception e) {
            System.err.println("Failed to delete expired tracking users: " + e.getMessage());
        }
    }
}
