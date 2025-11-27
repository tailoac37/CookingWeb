package projectCooking.Request;

public class InstructionRequest {
    private String instruction;
    private Boolean image;
    private String existingUrl;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Boolean getImage() {
        return image;
    }

    public void setImage(Boolean image) {
        this.image = image;
    }

    public String getExistingUrl() {
        return existingUrl;
    }

    public void setExistingUrl(String existingUrl) {
        this.existingUrl = existingUrl;
    }
}
