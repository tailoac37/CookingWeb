package projectCooking.Service.CloudinaryService.CloudinaryServiceImplements;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import projectCooking.Service.CloudinaryService.CloudinaryService;

@Service
public class CloudinaryServiceImplements implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String extractPublicId(String imageUrl) {
        try {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2)
                return null;

            String path = parts[1]; // v1234567890/abc123.png
            String[] pathParts = path.split("/");

            String lastPart = pathParts[pathParts.length - 1]; // abc123.png
            return lastPart.replaceAll("\\.[^.]+$", ""); // abc123
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteImageByUrl(String imageURL) {
        // Nếu URL không hợp lệ hoặc không phải từ Cloudinary, bỏ qua
        if (imageURL == null || imageURL.isEmpty()) {
            return false;
        }

        // Kiểm tra xem URL có phải từ Cloudinary không
        if (!imageURL.contains("cloudinary.com")) {
            System.out.println("URL không phải từ Cloudinary, bỏ qua xóa: " + imageURL);
            return false;
        }

        String publicId = extractPublicId(imageURL);
        if (publicId == null || publicId.isEmpty()) {
            System.out.println("Không thể lấy publicId từ URL: " + imageURL);
            return false;
        }

        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
            String status = (String) result.get("result");

            // Cloudinary trả về "ok" nếu xóa thành công, "not found" nếu ảnh không tồn tại
            if ("ok".equals(status)) {
                System.out.println("Đã xóa ảnh thành công: " + publicId);
                return true;
            } else if ("not found".equals(status)) {
                System.out.println("Ảnh không tồn tại trên Cloudinary, bỏ qua: " + publicId);
                return false;
            } else {
                System.out.println("Kết quả xóa ảnh không xác định: " + status);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa ảnh từ Cloudinary: " + imageURL);
            System.err.println("Chi tiết lỗi: " + e.getMessage());
            // Không throw exception, chỉ log và return false
            return false;
        }

    }

}
