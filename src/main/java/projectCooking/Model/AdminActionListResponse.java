package projectCooking.Model;

import java.util.List;

public class AdminActionListResponse {
    private List<AdminActionDTO> actions;
    private int totalActions;
    private int currentPage;
    private int totalPages;
    private int pageSize;

    public AdminActionListResponse() {
    }

    public AdminActionListResponse(List<AdminActionDTO> actions, int totalActions,
            int currentPage, int totalPages, int pageSize) {
        this.actions = actions;
        this.totalActions = totalActions;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }

    // Getters and Setters
    public List<AdminActionDTO> getActions() {
        return actions;
    }

    public void setActions(List<AdminActionDTO> actions) {
        this.actions = actions;
    }

    public int getTotalActions() {
        return totalActions;
    }

    public void setTotalActions(int totalActions) {
        this.totalActions = totalActions;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
