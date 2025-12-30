package projectCooking.Service.Implements;

import java.io.IOException;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Model.UserDTO;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.UserRequest;
import projectCooking.Service.AuthenticationService;
import projectCooking.Service.JWTService;
import projectCooking.Service.OtpService;

@Service
public class Authentication_Implements implements AuthenticationService {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private JWTService jwtService;
	@Autowired
	private Cloudinary cloudinary;
	@Autowired
	private ModelMapper model;
	@Autowired
	private OtpService otpService;

	private BCryptPasswordEncoder bcyr = new BCryptPasswordEncoder(12);

	@Override
	public UserDTO Register(UserRequest user) {
		User userRegister = userRepo.findByUserName(user.getUserName());
		if (userRegister != null) {
			throw new DulicateUserException("Tai khoan da ton tai");
		}
		userRegister = new User();
		userRegister = model.map(user, User.class);
		userRegister.setPasswordHash(bcyr.encode(user.getPasswordHash()));
		userRegister.setUserName(user.getUserName());
		userRegister.setFullName(user.getFullName());
		userRepo.save(userRegister);
		UserDTO userDTO = model.map(userRegister, UserDTO.class);
		userDTO.setUserName(userRegister.getUserName());
		userDTO.setFullName(userRegister.getFullName());
		userDTO.setToken(jwtService.getToken(userRegister));
		return userDTO;
	}

	@Override
	public UserDTO Login(UserRequest user) {
		User userDataBase = userRepo.findByUserName(user.getUserName());
		if (userDataBase == null) {
			throw new DulicateUserException("Tai khoan khong ton tai");
		}
		boolean check = bcyr.matches(user.getPasswordHash(), userDataBase.getPasswordHash());
		if (!check) {
			throw new DulicateUserException("Mat khau khong dung");
		}
		UserDTO userDTO = model.map(userDataBase, UserDTO.class);
		userDTO.setToken(jwtService.getToken(userDataBase));
		userDTO.setUserName(userDataBase.getUserName());
		userDTO.setFullName(userDataBase.getFullName());
		return userDTO;

	}

	@Override
	public String changePassword(String email, String password) {
		User changeUser = userRepo.findByEmail(email);
		if (changeUser == null) {
			return "Không tìm thấy email này";
		}
		changeUser.setPasswordHash(bcyr.encode(password));
		userRepo.save(changeUser);

		// Gửi email thông báo đổi mật khẩu thành công
		otpService.sendPasswordChangedNotification(email);

		return "Đã thay đổi mật khẩu thành công";
	}
}