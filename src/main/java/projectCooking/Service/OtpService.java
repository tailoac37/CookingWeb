package projectCooking.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;

import projectCooking.Repository.Entity.PasswordResetOTP;
import projectCooking.Repository.Entity.User;
import projectCooking.Service.EmailService.CredentialLoader;
import projectCooking.Service.EmailService.GmailSendService;
import projectCooking.Exception.DulicateUserException;
import projectCooking.Repository.PasswordResetOTPRepository;
import projectCooking.Repository.UserRepo;


import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private PasswordResetOTPRepository otpRepository;

    @Autowired
    private UserRepo userRepository;

  
    private String generateOtpCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

   
    public void sendOtp(String email) {
        try {
            User user = userRepository.findByEmail(email) ;
            if(user ==null )
            {
            	throw new DulicateUserException("Khong tim thay email ") ; 
            }

           
            String otpCode = generateOtpCode();

           
            PasswordResetOTP otpEntity = new PasswordResetOTP() ; 
            otpEntity.setUser(user);
            otpEntity.setEmail(email);
            otpEntity.setOtpCode(otpCode);
            otpEntity.setIsUsed(false);
            otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5)) ; 
            otpRepository.save(otpEntity);


            Credential credential = CredentialLoader.getCredentials();
            String subject = "ma OTP khoi phuc - CookingApp 🍳";
            String content = "Hello " + user.getFullName() + ",\n\n"
                    + "My OTP: " + otpCode + "\n"
                    + "Ma có hieu luc trong 5 phut.\n\n"
                    + "Tran trong,\nCookingApp Team.";

            GmailSendService.sendEmail(credential, email, subject, content);
            System.out.println(" da gui " + email);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("da gui " + e.getMessage());
        }
    }


    public boolean verifyOtp(String email, String otpCode) {
        PasswordResetOTP otp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException("khong tim thay"));

    
        if (otp.getIsUsed()) {
            throw new RuntimeException("ma nay da duoc su dung");
        }

        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new RuntimeException("OTP het han") ;
        }

        boolean valid = otp.getOtpCode().equals(otpCode);
        if (valid) {
            otp.setIsUsed(true);
            otpRepository.save(otp);
        }
        return valid;
    }
}

