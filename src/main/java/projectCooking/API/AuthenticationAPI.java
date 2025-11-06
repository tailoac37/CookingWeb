package projectCooking.API;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.UserDTO;
import projectCooking.Request.UserRequest;
import projectCooking.Service.AuthenticationService;
import projectCooking.Service.OtpService;

@RestController
public class AuthenticationAPI {
	@Autowired
	private AuthenticationService service ; 
	@Autowired
	private OtpService otpService;
	@PostMapping("/api/auth/register")
	public UserDTO register(@RequestPart("register") UserRequest user , @RequestPart("avatar_Url") MultipartFile file) throws IOException
	{
		return service.Register(user, file)  ; 
	}
	@PostMapping("/api/auth/login")
	public UserDTO login(@RequestBody UserRequest user) 
	{
		return service.Login(user) ; 
	}
	 @PostMapping("/api/auth/sendOTP")
	    public ResponseEntity<?> sendOtp(@RequestParam String email) {
	        otpService.sendOtp(email);
	        return ResponseEntity.ok("OTP đã được gửi đến email " + email);
	    }

    @PostMapping("/api/auth/verifyOTP")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean valid = otpService.verifyOtp(email, otp);
        if (valid) {
            return ResponseEntity.ok("OTP hợp lệ!");
        } else {
            return ResponseEntity.badRequest().body("OTP không chính xác.");
        }
    }
    @PostMapping("/api/auth/changePassword")
    public String changePassword(@RequestParam("email") String email , @RequestParam("newPassword") String password )
    {
    	return service.changePassword(email, password) ; 
    }
	
	 
}
