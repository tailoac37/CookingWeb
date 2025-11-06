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

    /**
     * Sinh mã OTP ngẫu nhiên 6 chữ số
     */
    private String generateOtpCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // luôn 6 chữ số
        return String.valueOf(otp);
    }

    /**
     * Gửi OTP qua Gmail API
     */
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
            String subject = "Mã OTP khôi phục mật khẩu - CookingApp 🍳";
            String content = "Xin chào " + user.getFullName() + ",\n\n"
                    + "Mã OTP của bạn là: " + otpCode + "\n"
                    + "Mã có hiệu lực trong 5 phút.\n\n"
                    + "Trân trọng,\nCookingApp Team.";

            GmailSendService.sendEmail(credential, email, subject, content);
            System.out.println(" Đã gửi OTP đến: " + email);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi gửi OTP: " + e.getMessage());
        }
    }


    public boolean verifyOtp(String email, String otpCode) {
        PasswordResetOTP otp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy OTP cho email này"));

    
        if (otp.getIsUsed()) {
            throw new RuntimeException("Mã OTP đã được sử dụng.");
        }

        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new RuntimeException("Mã OTP đã hết hạn.");
        }

        boolean valid = otp.getOtpCode().equals(otpCode);
        if (valid) {
            otp.setIsUsed(true);
            otpRepository.save(otp);
        }
        return valid;
    }
}

