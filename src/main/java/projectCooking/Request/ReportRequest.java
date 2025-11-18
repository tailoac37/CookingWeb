package projectCooking.Request;



import projectCooking.Repository.Entity.Report.ReportReason;

public class ReportRequest {
    
  
    
    private Integer recipeId; // Optional: for reporting a recipe
    
    private Integer reportedUserId; // Optional: for reporting a user

    private ReportReason reason;
    
    private String description;
    
    // Constructors
    public ReportRequest() {
    }
    
    public ReportRequest(Integer reporterId, Integer recipeId, Integer reportedUserId, 
                         ReportReason reason, String description) {

        this.recipeId = recipeId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.description = description;
    }
    
    // Getters and Setters
   
    
    public Integer getRecipeId() {
        return recipeId;
    }
    
    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }
    
    public Integer getReportedUserId() {
        return reportedUserId;
    }
    
    public void setReportedUserId(Integer reportedUserId) {
        this.reportedUserId = reportedUserId;
    }
    
    public ReportReason getReason() {
        return reason;
    }
    
    public void setReason(ReportReason reason) {
        this.reason = reason;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}