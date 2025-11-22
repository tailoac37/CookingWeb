package projectCooking.Model;

public class UserStatisticsDTO {
    private Integer totalUsers;
    private Integer activeUsers;
    private Integer inactiveUsers;
    private Integer adminUsers;
    private Integer regularUsers;
    private Integer verifiedUsers;
    private Integer unverifiedUsers;
    private Integer newUsersThisMonth;
    private Integer newUsersToday;

    public UserStatisticsDTO() {
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Integer getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Integer activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Integer getInactiveUsers() {
        return inactiveUsers;
    }

    public void setInactiveUsers(Integer inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }

    public Integer getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(Integer adminUsers) {
        this.adminUsers = adminUsers;
    }

    public Integer getRegularUsers() {
        return regularUsers;
    }

    public void setRegularUsers(Integer regularUsers) {
        this.regularUsers = regularUsers;
    }

    public Integer getVerifiedUsers() {
        return verifiedUsers;
    }

    public void setVerifiedUsers(Integer verifiedUsers) {
        this.verifiedUsers = verifiedUsers;
    }

    public Integer getUnverifiedUsers() {
        return unverifiedUsers;
    }

    public void setUnverifiedUsers(Integer unverifiedUsers) {
        this.unverifiedUsers = unverifiedUsers;
    }

    public Integer getNewUsersThisMonth() {
        return newUsersThisMonth;
    }

    public void setNewUsersThisMonth(Integer newUsersThisMonth) {
        this.newUsersThisMonth = newUsersThisMonth;
    }

    public Integer getNewUsersToday() {
        return newUsersToday;
    }

    public void setNewUsersToday(Integer newUsersToday) {
        this.newUsersToday = newUsersToday;
    }
}
