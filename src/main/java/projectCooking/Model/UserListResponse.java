package projectCooking.Model;

import java.util.List;

public class UserListResponse {
    private List<AdminUserDTO> users;
    private Integer totalUsers;
    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;

    public UserListResponse() {
    }

    public UserListResponse(List<AdminUserDTO> users, Integer totalUsers, Integer currentPage,
            Integer totalPages, Integer pageSize) {
        this.users = users;
        this.totalUsers = totalUsers;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }

    public List<AdminUserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<AdminUserDTO> users) {
        this.users = users;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
