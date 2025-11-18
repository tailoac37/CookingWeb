package projectCooking.Request;

import projectCooking.Repository.Entity.AdminAction;
import projectCooking.Repository.Entity.Report;

public class AdminActionRequest {
	Report.ReportStatus status  ; 
	AdminAction.ActionType type ; 
	String adminNote  ;
	public Report.ReportStatus getStatus() {
		return status;
	}
	public void setStatus(Report.ReportStatus status) {
		this.status = status;
	}
	public String getAdminNote() {
		return adminNote;
	}
	public void setAdminNote(String adminNote) {
		this.adminNote = adminNote;
	}
	public AdminAction.ActionType getType() {
		return type;
	}
	public void setType(AdminAction.ActionType type) {
		this.type = type;
	} 
	
}
