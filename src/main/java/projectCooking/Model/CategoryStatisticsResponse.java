package projectCooking.Model;

import java.util.Map;

public class CategoryStatisticsResponse {
    private Map<String, Long> categoryBreakdown;
    private Map<String, CategoryPerformance> categoryPerformance;
    
    public Map<String, Long> getCategoryBreakdown() { return categoryBreakdown; }
    public void setCategoryBreakdown(Map<String, Long> categoryBreakdown) { 
        this.categoryBreakdown = categoryBreakdown; 
    }
    
    public Map<String, CategoryPerformance> getCategoryPerformance() { return categoryPerformance; }
    public void setCategoryPerformance(Map<String, CategoryPerformance> categoryPerformance) { 
        this.categoryPerformance = categoryPerformance; 
    }
}