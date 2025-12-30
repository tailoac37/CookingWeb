package projectCooking.Service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Gửi email OTP với template HTML đẹp
     */
    public void sendOtpEmail(String toEmail, String userName, String otpCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("🔐 Mã OTP khôi phục mật khẩu - Easy Cooking");

        String htmlContent = buildOtpEmailTemplate(userName, otpCode);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("✅ Email OTP đã được gửi đến: " + toEmail);
    }

    /**
     * Gửi email thông báo đổi mật khẩu thành công
     */
    public void sendPasswordChangedEmail(String toEmail, String userName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("✅ Mật khẩu đã được thay đổi thành công - Easy Cooking");

        String htmlContent = buildPasswordChangedTemplate(userName);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("✅ Email thông báo đổi mật khẩu đã được gửi đến: " + toEmail);
    }

    /**
     * Template HTML cho email OTP - Theme Easy Cooking (Navy Blue)
     */
    private String buildOtpEmailTemplate(String userName, String otpCode) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "</head>" +
                "<body style=\"margin: 0; padding: 0; font-family: 'Be Vietnam Pro', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f6f8;\">"
                +
                "    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px;\">" +
                "        <div style=\"background: linear-gradient(135deg, #003459 0%, #00171F 100%); border-radius: 16px 16px 0 0; padding: 40px 30px; text-align: center;\">"
                +
                "            <div style=\"font-size: 50px; margin-bottom: 10px;\">🍳</div>" +
                "            <h1 style=\"color: white; margin: 0; font-size: 28px; font-weight: 700;\">Easy Cooking</h1>"
                +
                "            <p style=\"color: rgba(255,255,255,0.85); margin: 8px 0 0 0; font-size: 14px;\">Khôi phục mật khẩu</p>"
                +
                "        </div>" +
                "        <div style=\"background: white; padding: 40px 30px; border-radius: 0 0 16px 16px; box-shadow: 0 4px 20px rgba(0,52,89,0.1);\">"
                +
                "            <h2 style=\"color: #00171F; margin-top: 0; font-weight: 600;\">Xin chào " + userName
                + "! 👋</h2>" +
                "            <p style=\"color: #667479; line-height: 1.7; font-size: 15px;\">Bạn đã yêu cầu khôi phục mật khẩu cho tài khoản Easy Cooking của mình. Vui lòng sử dụng mã OTP bên dưới để xác nhận:</p>"
                +
                "            <div style=\"background: linear-gradient(135deg, #003459 0%, #00171F 100%); border-radius: 12px; padding: 30px; text-align: center; margin: 30px 0;\">"
                +
                "                <p style=\"color: rgba(255,255,255,0.85); margin: 0 0 12px 0; font-size: 13px; text-transform: uppercase; letter-spacing: 1px;\">Mã xác nhận của bạn</p>"
                +
                "                <div style=\"font-size: 40px; font-weight: 700; color: white; letter-spacing: 10px; font-family: 'Courier New', monospace;\">"
                + otpCode + "</div>" +
                "            </div>" +
                "            <div style=\"background: #FFF8E6; border-left: 4px solid #F5A623; padding: 16px 20px; border-radius: 8px; margin: 25px 0;\">"
                +
                "                <p style=\"color: #8B6914; margin: 0; font-size: 14px; line-height: 1.5;\">⏰ <strong>Lưu ý:</strong> Mã OTP này có hiệu lực trong <strong>5 phút</strong>. Không chia sẻ mã này với bất kỳ ai!</p>"
                +
                "            </div>" +
                "            <p style=\"color: #667479; line-height: 1.7; font-size: 14px;\">Nếu bạn không yêu cầu khôi phục mật khẩu, vui lòng bỏ qua email này. Tài khoản của bạn vẫn an toàn.</p>"
                +
                "            <hr style=\"border: none; border-top: 1px solid #E8EAED; margin: 30px 0;\">" +
                "            <p style=\"color: #99A1A7; font-size: 12px; text-align: center; margin: 0;\">Email này được gửi tự động từ Easy Cooking.<br>Vui lòng không trả lời email này.</p>"
                +
                "        </div>" +
                "        <p style=\"color: #99A1A7; font-size: 11px; text-align: center; margin-top: 20px;\">© 2024 Easy Cooking. All rights reserved.</p>"
                +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Template HTML cho email thông báo đổi mật khẩu thành công - Theme Easy
     * Cooking
     */
    private String buildPasswordChangedTemplate(String userName) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "</head>" +
                "<body style=\"margin: 0; padding: 0; font-family: 'Be Vietnam Pro', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f6f8;\">"
                +
                "    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px;\">" +
                "        <div style=\"background: linear-gradient(135deg, #003459 0%, #00171F 100%); border-radius: 16px 16px 0 0; padding: 40px 30px; text-align: center;\">"
                +
                "            <div style=\"font-size: 50px; margin-bottom: 10px;\">🍳</div>" +
                "            <h1 style=\"color: white; margin: 0; font-size: 28px; font-weight: 700;\">Easy Cooking</h1>"
                +
                "            <p style=\"color: rgba(255,255,255,0.85); margin: 8px 0 0 0; font-size: 14px;\">Thông báo bảo mật</p>"
                +
                "        </div>" +
                "        <div style=\"background: white; padding: 40px 30px; border-radius: 0 0 16px 16px; box-shadow: 0 4px 20px rgba(0,52,89,0.1);\">"
                +
                "            <div style=\"text-align: center; margin-bottom: 25px;\">" +
                "                <div style=\"width: 70px; height: 70px; background: linear-gradient(135deg, #00A86B 0%, #00C853 100%); border-radius: 50%; margin: 0 auto; line-height: 70px;\">"
                +
                "                    <span style=\"font-size: 35px; color: white;\">✓</span>" +
                "                </div>" +
                "            </div>" +
                "            <h2 style=\"color: #00171F; margin-top: 0; text-align: center; font-weight: 600;\">Mật khẩu đã được thay đổi!</h2>"
                +
                "            <p style=\"color: #667479; line-height: 1.7; text-align: center; font-size: 15px;\">Xin chào <strong style=\"color: #003459;\">"
                + userName + "</strong>, mật khẩu tài khoản Easy Cooking của bạn đã được thay đổi thành công.</p>" +
                "            <div style=\"background: #E8F5E9; border-left: 4px solid #00A86B; padding: 16px 20px; border-radius: 8px; margin: 25px 0;\">"
                +
                "                <p style=\"color: #1B5E20; margin: 0; font-size: 14px;\">🔒 Nếu bạn thực hiện thay đổi này, bạn có thể bỏ qua email này.</p>"
                +
                "            </div>" +
                "            <div style=\"background: #FFEBEE; border-left: 4px solid #EF5350; padding: 16px 20px; border-radius: 8px; margin: 25px 0;\">"
                +
                "                <p style=\"color: #C62828; margin: 0; font-size: 14px;\">⚠️ Nếu bạn <strong>không</strong> thực hiện thay đổi này, vui lòng liên hệ với chúng tôi ngay lập tức!</p>"
                +
                "            </div>" +
                "            <hr style=\"border: none; border-top: 1px solid #E8EAED; margin: 30px 0;\">" +
                "            <p style=\"color: #99A1A7; font-size: 12px; text-align: center; margin: 0;\">Email này được gửi tự động từ Easy Cooking.<br>Vui lòng không trả lời email này.</p>"
                +
                "        </div>" +
                "        <p style=\"color: #99A1A7; font-size: 11px; text-align: center; margin-top: 20px;\">© 2024 Easy Cooking. All rights reserved.</p>"
                +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
