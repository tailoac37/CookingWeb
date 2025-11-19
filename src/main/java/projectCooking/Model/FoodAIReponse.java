package projectCooking.Model;

import lombok.Data;

public class FoodAIReponse {
    private boolean success;
    private DataBlock data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBlock getData() {
        return data;
    }

    public void setData(DataBlock data) {
        this.data = data;
    }

    public static class DataBlock {
        private String ingredients_vi;
        private String ingredients_en;
        private String description_vi;
        private String description_en;

        public String getIngredients_vi() { return ingredients_vi; }
        public void setIngredients_vi(String v) { this.ingredients_vi = v; }

        public String getIngredients_en() { return ingredients_en; }
        public void setIngredients_en(String v) { this.ingredients_en = v; }

        public String getDescription_vi() { return description_vi; }
        public void setDescription_vi(String v) { this.description_vi = v; }

        public String getDescription_en() { return description_en; }
        public void setDescription_en(String v) { this.description_en = v; }
    }
}

