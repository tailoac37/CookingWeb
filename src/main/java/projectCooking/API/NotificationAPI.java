package projectCooking.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.NotificationDTO;
import projectCooking.Service.NotificationService;

@RestController
public class NotificationAPI {
	@Autowired
	NotificationService service ; 
	@GetMapping("/api/user/notifications")
	public List<NotificationDTO> getListNotification(@RequestHeader("Authorization") String auth)
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.getListNotification(token)  ;
 	}
	@GetMapping("/api/user/notifications/unread")
	public List<NotificationDTO> getListNotificationUnread(@RequestHeader("Authorization") String auth)
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.getListNotificationUnread(token)  ;
 	}
	@PatchMapping("/api/user/notifications/{id}/read")
	public String changeStatusNotification(@RequestHeader("Authorization") String auth , @PathVariable("id") Integer Id)
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.changeStatusNotification(token, Id) ;
	}
	@DeleteMapping("/api/user/notifications/{id}")
	public String delNotificationById(@RequestHeader("Authorization") String auth , @PathVariable("id") Integer Id)
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.delNotificationById(token, Id) ; 
	}
	@DeleteMapping("/api/user/notifications")
	public String delAllNotification(@RequestHeader("Authorization") String auth )
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.delAllNotification(token) ; 
	}
}
