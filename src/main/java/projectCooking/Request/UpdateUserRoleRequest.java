package projectCooking.Request;

public class UpdateUserRoleRequest {
    private String role; // USER or ADMIN

    public UpdateUserRoleRequest() {
    }

    public UpdateUserRoleRequest(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
