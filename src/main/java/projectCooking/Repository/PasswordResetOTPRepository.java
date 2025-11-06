package projectCooking.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import projectCooking.Repository.Entity.PasswordResetOTP;

public interface PasswordResetOTPRepository extends JpaRepository<PasswordResetOTP, Long> {
	Optional<PasswordResetOTP> findTopByEmailOrderByCreatedAtDesc(String email);
}
