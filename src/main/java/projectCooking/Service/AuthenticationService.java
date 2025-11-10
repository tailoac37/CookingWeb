package projectCooking.Service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import projectCooking.Model.UserDTO;
import projectCooking.Request.UserRequest;

public interface AuthenticationService {
	public UserDTO Register(UserRequest user )    ;
	public UserDTO Login(UserRequest user)  ; 
	public String changePassword(String email , String password)  ; 
}
