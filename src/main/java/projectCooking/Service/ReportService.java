package projectCooking.Service;

import java.util.List;

import projectCooking.Model.ReportDTO;
import projectCooking.Repository.Entity.Report;
import projectCooking.Request.AdminActionRequest;
import projectCooking.Request.ReportRequest;

public interface ReportService {
	public String sendReport(String token , ReportRequest report)  ;
	public ReportDTO reportDTODetail(String token , Integer Id)  ; 
	public List<ReportDTO> getListReportByStatus(String token, Report.ReportStatus status )  ;
	public String resolveReport(String token , Integer Id , AdminActionRequest adminActionRequest)  ;
}
