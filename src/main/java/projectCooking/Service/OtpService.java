package projectCooking.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Repository.Entity.PasswordResetOTP;
import projectCooking.Repository.Entity.User;
import projectCooking.Service.EmailService.EmailService;
import projectCooking.Exception.DulicateUserException;
import projectCooking.Repository.PasswordResetOTPRepository;
import projectCooking.Repository.UserRepo;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private PasswordResetOTPRepository otpRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Tạo mã OTP ngẫu nhiên 6 số
     */
    private String generateOtpCode() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Gửi OTP đến email của user để khôi phục mật khẩu
     */
    public void sendOtp(String email) {
        // Tìm user theo email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new DulicateUserException("Không tìm thấy tài khoản với email: " + email);
        }

        // Vô hiệu hóa các OTP cũ chưa sử dụng (nếu có)
        otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .ifPresent(oldOtp -> {
                    if (!oldOtp.getIsUsed()) {
                        oldOtp.setIsUsed(true);
                        otpRepository.save(oldOtp);
                    }
                });

        // Tạo mã OTP mới
        String otpCode = generateOtpCode();

        // Lưu OTP vào database
        PasswordResetOTP otpEntity = new PasswordResetOTP();
        otpEntity.setUser(user);
        otpEntity.setEmail(email);
        otpEntity.setOtpCode(otpCode);
        otpEntity.setIsUsed(false);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpEntity);

        // Gửi email chứa OTP
        try {
            String userName = user.getFullName() != null ? user.getFullName() : "Người dùng";
            emailService.sendOtpEmail(email, userName, otpCode);
            System.out.println("✅ OTP đã được gửi đến: " + email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email OTP: " + e.getMessage());
        }
    }

    /**
     * Xác thực mã OTP
     */
    public boolean verifyOtp(String email, String otpCode) {
        PasswordResetOTP otp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mã OTP cho email này"));

        // Kiểm tra OTP đã được sử dụng chưa
        if (otp.getIsUsed()) {
            throw new RuntimeException("Mã OTP này đã được sử dụng");
        }

        // Kiểm tra OTP đã hết hạn chưa
        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new RuntimeException("Mã OTP đã hết hạn");
        }

        // Kiểm tra mã OTP có khớp không
        boolean valid = otp.getOtpCode().equals(otpCode);
        if (valid) {
            otp.setIsUsed(true);
            otpRepository.save(otp);
        }
        return valid;
    }

    /**
     * Gửi thông báo đổi mật khẩu thành công
     */
    public void sendPasswordChangedNotification(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            try {
                String userName = user.getFullName() != null ? user.getFullName() : "Người dùng";
                emailService.sendPasswordChangedEmail(email, userName);
            } catch (Exception e) {
                // Không throw exception, chỉ log lỗi vì đây là thông báo không bắt buộc
                System.err.println("⚠️ Không thể gửi email thông báo đổi mật khẩu: " + e.getMessage());
            }
        }
    }
}
