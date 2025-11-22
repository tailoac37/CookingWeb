package projectCooking.Request;

public class RecipeApprovalRequest {
    private String adminNote;

    public RecipeApprovalRequest() {
    }

    public RecipeApprovalRequest(String adminNote) {
        this.adminNote = adminNote;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }
}
