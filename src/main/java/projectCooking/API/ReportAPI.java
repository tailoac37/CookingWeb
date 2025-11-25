package projectCooking.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projectCooking.Model.ReportDTO;
import projectCooking.Repository.Entity.Report;
import projectCooking.Request.AdminActionRequest;
import projectCooking.Request.ReportRequest;
import projectCooking.Service.ReportService;

@RestController
public class ReportAPI {
	@Autowired
	ReportService service ; 
	@PostMapping("/api/user/report")
	public String sendReport(@RequestHeader("Authorization") String auth , @RequestBody ReportRequest report)
	{ 
		String token = auth.replace("Bearer", "")  ; 
		return service.sendReport(token, report) ; 
	}
	@GetMapping("/api/admin/report")
	public List<ReportDTO> getReportByStatus(@RequestParam("status") Report.ReportStatus status , @RequestHeader("Authorization") String auth) 
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.getListReportByStatus(token, status)  ; 
	}
	@GetMapping("/api/admin/report/{id}")
	public ReportDTO getReportDetails(@PathVariable("id") Integer Id , @RequestHeader("Authorization") String auth) 
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.reportDTODetail(token, Id)  ; 
	}
	@PatchMapping("/api/admin/report/{id}")
	public String resolveReport(@RequestHeader("Authorization") String auth ,@PathVariable("id") Integer Id , @RequestBody AdminActionRequest adminActionRequest )
	{
		String token = auth.replace("Bearer", "")  ; 
		return service.resolveReport(token, Id, adminActionRequest) ;
	}
}
