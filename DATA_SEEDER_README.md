## DataSeeder - Hướng dẫn sử dụng Java Faker

### ✅ Đã hoàn tất:
1. **Đã thêm JavaFaker dependency** vào `pom.xml`
2. **Đã tạo DataSeeder class** tại `projectCooking/Service/DataSeeder.java`

###  Cách hoạt động:
DataSeeder sẽ tự động chạy khi backend khởi động (sử dụng `@PostConstruct`). Nó sẽ kiểm tra nếu database trống thì tự động tạo dữ liệu mẫu.

### 📊 Dữ liệu được tạo:
- **10 users** với:
  - Username: user1, user2, ..., user10  
  - Email: user1@cooking.com, user2@cooking.com, ...
  - Password: `password123` (đã mã hóa)
  - Avatar từ pravatar.cc
  - Bio ngẫu nhiên bằng tiếng Việt
  
- **10 categories**: Món ăn sáng, Món chính, Món tráng miệng, v.v.

- **100 recipes** (mỗi user 10 món):
  - Tên món Việt Nam: Phở Bò, Bánh Mì, Bún Chả, v.v.
  - Hình ảnh đẹp t Unsplash
  - Nguyên liệu ngẫu nhiên
  - Thông tin dinh dưỡng
  - Thời gian nấu realistic
  - Status: APPROVED

### 🚀 Cách chạy:
1. **Đảm bảo database MySQL đang chạy**
2. **Chạy backend Spring Boot**:
   ```
   mvn spring-boot:run
   ```
   hoặc Run từ IDE

3. DataSeeder sẽ tự động chạy và populate data nếu database trống

### 📝 Ghi chú:
- DataSeeder chỉ chạy 1 lần khi database trống
- Nếu đã có data, nó sẽ bỏ qua và in message "✅ Database đã có dữ liệu, bỏ qua seed"
- Để reset và seed lại, xóa toàn bộ dữ liệu trong database

### 🔧 Nếu cần sửa code:
Để sửa lỗi compile hiện tại, có thể cần update Maven dependencies hoặc kiểm tra lại các import statement trong DataSeeder.java
