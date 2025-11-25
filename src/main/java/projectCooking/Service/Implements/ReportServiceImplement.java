package projectCooking.Service.Implements;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projectCooking.Exception.DulicateUserException;
import projectCooking.Mapper.ReportMapper;
import projectCooking.Model.ReportDTO;
import projectCooking.Repository.AdminActionRepo;
import projectCooking.Repository.NotificationRepo;
import projectCooking.Repository.RecipesRepo;
import projectCooking.Repository.ReportRepo;
import projectCooking.Repository.UserRepo;
import projectCooking.Repository.Entity.AdminAction;
import projectCooking.Repository.Entity.AdminAction.ActionType;
import projectCooking.Repository.Entity.Recipe;
import projectCooking.Repository.Entity.Report;
import projectCooking.Repository.Entity.Report.ReportStatus;
import projectCooking.Repository.Entity.User;
import projectCooking.Request.AdminActionRequest;
import projectCooking.Request.ReportRequest;
import projectCooking.Service.JWTService;
import projectCooking.Service.NotificationService;
import projectCooking.Service.ReportService;

@Service
public class ReportServiceImplement implements ReportService {
	@Autowired
	private ReportRepo reportRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private JWTService jwt;
	@Autowired
	private RecipesRepo recipeRepo;
	@Autowired
	private ReportMapper mapper;
	@Autowired
	private NotificationService notifService;
	@Autowired
	private NotificationRepo notifRepo;
	@Autowired
	private AdminActionRepo adminActionRepo;

	@Override
	public String sendReport(String token, ReportRequest request) {
		Recipe recipe = null;
		if (request.getRecipeId() != null) {
			recipe = recipeRepo.findById(request.getRecipeId()).orElse(null);
		}

		String userName = jwt.extractUserName(token);
		User reporter = userRepo.findByUserName(userName);
		User reportedUser = null;
		if (request.getReportedUserId() != null) {
			reportedUser = userRepo.findById(request.getReportedUserId()).orElse(null);
		}

		Report report = mapper.toEntity(request, reporter, recipe, reportedUser);
		reportRepo.save(report);
		notifService.sendReportNotificationToAdmins(reporter, recipe, reportedUser, report.getReason().toString());
		return "done";
	}

	@Override
	public List<ReportDTO> getListReportByStatus(String token, ReportStatus status) {
		String userName = jwt.extractUserName(token);
		User user = userRepo.findByUserName(userName);
		if (user == null) {
			throw new DulicateUserException("Nguoi dung nay khong ton tai , hoac da bi xoa trong database");
		}
		List<Report> report = reportRepo.findByStatus(status);
		List<ReportDTO> reportDTOList = new ArrayList<>();
		for (Report item : report) {
			ReportDTO reportDTO = mapper.toDTO(item);
			reportDTOList.add(reportDTO);
		}
		return reportDTOList;
	}

	@Override
	public String resolveReport(String token, Integer Id, AdminActionRequest adminActionRequest) {
		String userName = jwt.extractUserName(token);
		User admin = userRepo.findByUserName(userName);
		if (admin == null) {
			throw new DulicateUserException("Nguoi dung nay khong ton tai , hoac da bi xoa trong database");
		}
		Report report = reportRepo.findById(Id).orElse(null);

		if (report == null) {
			throw new DulicateUserException("bao cao khong ton tai");
		}
		report.setAdminNote(adminActionRequest.getAdminNote());
		report.setStatus(adminActionRequest.getStatus());
		reportRepo.save(report);
		AdminAction adminAction = new AdminAction();

		if (adminActionRequest.getStatus() == ReportStatus.RESOLVED) {
			String reporterMessage = "";

			if (adminActionRequest.getType() == ActionType.WARN) {
				adminAction.setActionType(ActionType.WARN);

				// Gửi thông báo cho người bị báo cáo
				notifService.AdminSendReportedNotification(admin, report.getRecipe(), adminActionRequest.getAdminNote(),
						report.getReportedUser(), report.getReason().toString());

				// Tạo message cho người báo cáo
				if (report.getRecipe() != null) {
					reporterMessage = "Cảm ơn bạn đã báo cáo. Admin đã cảnh cáo tác giả của bài viết '" +
							report.getRecipe().getTitle() + "' về vi phạm: " + report.getReason().toString();
				} else if (report.getReportedUser() != null) {
					reporterMessage = "Cảm ơn bạn đã báo cáo. Admin đã cảnh cáo người dùng '" +
							report.getReportedUser().getFullName() + "' về vi phạm: " + report.getReason().toString();
				}

			} else if (adminActionRequest.getType() == ActionType.DELETE) {
				adminAction.setActionType(ActionType.DELETE);

				if (report.getRecipe() != null) {
					Recipe recipe = report.getRecipe();
					recipe.setAdminNote(adminActionRequest.getAdminNote());
					recipe.setStatus(Recipe.RecipeStatus.REJECTED);
					recipeRepo.save(recipe);

					// Message cho người báo cáo khi xóa bài viết
					reporterMessage = "Cảm ơn bạn đã báo cáo. Admin đã xóa bài viết '" +
							recipe.getTitle() + "' do vi phạm: " + report.getReason().toString();
				} else if (report.getReportedUser() != null) {
					User user = report.getReportedUser();
					user.setIsActive(false);
					userRepo.save(user);

					// Message cho người báo cáo khi khóa tài khoản
					reporterMessage = "Cảm ơn bạn đã báo cáo. Admin đã khóa tài khoản '" +
							user.getFullName() + "' do vi phạm: " + report.getReason().toString();
				}

				// Gửi thông báo cho người bị báo cáo
				notifService.AdminSendReportedNotification(admin, report.getRecipe(), adminActionRequest.getAdminNote(),
						report.getReportedUser(), report.getReason().toString());
			}

			// Gửi thông báo cho người báo cáo với message cụ thể
			notifService.AdminSendReporterNotification(admin, report.getReporter(), report.getRecipe(),
					reporterMessage, report.getReportedUser(), report.getReason().toString());

		} else {
			if (adminActionRequest.getStatus() == ReportStatus.REJECTED) {
				adminAction.setActionType(ActionType.REJECT);
			} else {
				adminAction.setActionType(ActionType.REVIEW);
			}
			notifService.AdminSendReporterNotification(admin, report.getReporter(), report.getRecipe(),
					adminActionRequest.getAdminNote(), report.getReportedUser(), report.getReason().toString());
		}
		adminAction.setAdmin(admin);
		adminAction.setReason(report.getReason().toString());
		adminAction.setAdminNote(report.getAdminNote());
		adminAction.setRecipe(report.getRecipe());
		adminAction.setRelatedReport(report);
		adminAction.setTargetUser(report.getReportedUser());
		adminActionRepo.save(adminAction);

		notifService.sendResolveNotificationToAdmins(admin, report.getRecipe(), report.getReportedUser(),
				report.getReason().toString(), report);

		return "done";
	}

	@Override
	public ReportDTO reportDTODetail(String token, Integer Id) {
		String userName = jwt.extractUserName(token);
		User user = userRepo.findByUserName(userName);
		if (user == null) {
			throw new DulicateUserException("Nguoi dung nay khong ton tai , hoac da bi xoa trong database");
		}
		Report report = reportRepo.findById(Id).orElse(null);
		if (report == null) {
			throw new DulicateUserException("bao cao khong ton tai");
		}
		ReportDTO reportDTO = mapper.toDTO(report);
		return reportDTO;
	}

}
