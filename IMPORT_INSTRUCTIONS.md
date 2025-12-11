# 🚀 HƯỚNG DẪN IMPORT DỮ LIỆU VÀO DATABASE

## Cách 1: Dùng MySQL Workbench (Khuyến nghị)

1. **Mở MySQL Workbench**
2. **Kết nối đến database `cooking`**
   - Host: localhost:3306
   - Username: root
   - Password: 01112004

3. **Mở file SQL**
   - File > Open SQL Script
   - Chọn file: `C:\Users\taih1\Desktop\laptrinh\phattrienweb\cooking\project\projectCooking\seed_data.sql`

4. **Execute Script**
   - Click biểu tượng sét ⚡ (Execute) hoặc nhấn Ctrl+Shift+Enter
   - Chờ script chạy xong

5. **Kiểm tra kết quả**
   ```sql
   SELECT COUNT(*) FROM users;    -- Phải có 10 users
   SELECT COUNT(*) FROM recipes;  -- Phải có 30+ recipes
   ```

## Cách 2: Dùng Command Line

```bash
# Trong PowerShell
cd C:\Users\taih1\Desktop\laptrinh\phattrienweb\cooking\project\projectCooking

# Import SQL
mysql -u root -p -D cooking
# Nhập password: 01112004
# Sau đó:
source seed_data.sql
```

## Cách 3: Copy-Paste từng đoạn

Nếu gặp lỗi, hãy copy từng đoạn SQL và chạy riêng:

1. **Tạo users trước** (copy đoạn INSERT INTO users...)
2. **Tạo recipes sau** (copy đoạn INSERT INTO recipes...)

## ✅ Sau khi import thành công:

**Thông tin đăng nhập:**
- Username: user1, user2, ..., user10
- Email: user1@cooking.com, user2@cooking.com, ...  
- Password: `password123`

**Dữ liệu có sẵn:**
- 10 users với avatar và bio
- 30 recipes (có thể mở rộng thêm)
- Tất cả recipes đã APPROVED
- Có view_count và like_count ngẫu nhiên

## 🔧 Nếu gặp lỗi:

**Lỗi "Unknown column"**: Kiểm tra tên cột trong database có khác không
```sql
DESCRIBE users;
DESCRIBE recipes;
```

**Lỗi "Duplicate entry"**: Database đã có dữ liệu, uncomment dòng DELETE ở đầu script để xóa dữ liệu cũ

**Lỗi "Foreign key constraint"**: Đảm bảo `category_id` tồn tại trong table categories
```sql
SELECT * FROM categories;
```

---

**Sau khi import xong, chạy backend và kiểm tra!** 🚀
