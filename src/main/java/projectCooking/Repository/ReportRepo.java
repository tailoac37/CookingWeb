package projectCooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import projectCooking.Repository.Entity.Report;

public interface ReportRepo extends JpaRepository<Report, Integer> {
	List<Report> findByStatus(Report.ReportStatus status)  ; 
}
