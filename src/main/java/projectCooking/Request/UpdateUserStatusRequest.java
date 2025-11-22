package projectCooking.Request;

public class UpdateUserStatusRequest {
    private Boolean isActive;
    private String reason; // Optional reason for status change

    public UpdateUserStatusRequest() {
    }

    public UpdateUserStatusRequest(Boolean isActive, String reason) {
        this.isActive = isActive;
        this.reason = reason;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
