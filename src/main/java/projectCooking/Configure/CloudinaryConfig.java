package projectCooking.Configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
    
    @Value("${cloudinary.cloud_name}")
    private String cloudName;
    
    @Value("${cloudinary.api_key}")
    private String apiKey;
    
    @Value("${cloudinary.api_secret}")
    private String apiSecret;
    
    @Bean
    public Cloudinary cloudinary() {
        System.out.println("========== CREATING CLOUDINARY BEAN ==========");
        System.out.println("Cloud Name: " + cloudName);
        System.out.println("API Key: " + apiKey);
        System.out.println("API Secret: " + (apiSecret != null ? "EXISTS" : "NULL"));
        System.out.println("==============================================");
        
        if(cloudName == null || apiKey == null || apiSecret == null) {
            throw new RuntimeException("Cloudinary credentials are not configured properly!");
        }
        
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret,
            "secure", true
        ));
        
        System.out.println("Cloudinary Bean created successfully!");
        
        return cloudinary;
    }
}