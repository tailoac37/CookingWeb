package projectCooking.API;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	private AuthenticationService service;
	@Autowired
	private OtpService otpService;

	@PostMapping("/api/auth/register")
	public UserDTO register(@RequestBody UserRequest user) {
		return service.Register(user);
	}

	@PostMapping("/api/auth/login")
	public UserDTO login(@RequestBody UserRequest user) {
		return service.Login(user);
	}

	/**
	 * Gửi OTP đến email để khôi phục mật khẩu
	 */
	@PostMapping("/api/auth/sendOTP")
	public ResponseEntity<?> sendOtp(@RequestParam String email) {
		Map<String, Object> response = new HashMap<>();
		try {
			otpService.sendOtp(email);
			response.put("success", true);
			response.put("message", "Mã OTP đã được gửi đến email " + email);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	/**
	 * Xác thực mã OTP
	 */
	@PostMapping("/api/auth/verifyOTP")
	public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
		Map<String, Object> response = new HashMap<>();
		try {
			boolean valid = otpService.verifyOtp(email, otp);
			if (valid) {
				response.put("success", true);
				response.put("message", "Mã OTP hợp lệ! Bạn có thể đổi mật khẩu.");
				return ResponseEntity.ok(response);
			} else {
				response.put("success", false);
				response.put("message", "Mã OTP không chính xác.");
				return ResponseEntity.badRequest().body(response);
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	/**
	 * Đổi mật khẩu sau khi xác thực OTP
	 */
	@PostMapping("/api/auth/changePassword")
	public ResponseEntity<?> changePassword(@RequestParam("email") String email,
			@RequestParam("newPassword") String password) {
		Map<String, Object> response = new HashMap<>();
		try {
			String result = service.changePassword(email, password);
			response.put("success", true);
			response.put("message", result);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
}
