-- =====================================================
-- SEED DATA - Format đúng với Backend (@-separated)
-- =====================================================

USE cooking;

-- =====================================================  
-- USERS
-- =====================================================

INSERT INTO users (user_name, email, password_hash, full_name, avatar_url, bio, role, is_active, email_verified, created_at, updated_at) VALUES
('user1', 'user1@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Nguyễn Văn An', 'https://i.pravatar.cc/150?img=1', 'Đầu bếp món Việt', 'USER', 1, 1, NOW(), NOW()),
('user2', 'user2@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Trần Thị Bình', 'https://i.pravatar.cc/150?img=2', 'Yêu nấu ăn', 'USER', 1, 1, NOW(), NOW()),
('user3', 'user3@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Lê Hoàng Cường', 'https://i.pravatar.cc/150?img=3', 'Chef Huế', 'USER', 1, 1, NOW(), NOW()),
('user4', 'user4@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Phạm Thu Dung', 'https://i.pravatar.cc/150?img=4', 'Món sáng', 'USER', 1, 1, NOW(), NOW()),
('user5', 'user5@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Vũ Minh Đức', 'https://i.pravatar.cc/150?img=5', 'Food Blogger', 'USER', 1, 1, NOW(), NOW()),
('user6', 'user6@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Hoàng Thu Nga', 'https://i.pravatar.cc/150?img=6', 'Healthy Food', 'USER', 1, 1, NOW(), NOW()),
('user7', 'user7@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Đặng Quốc Hùng', 'https://i.pravatar.cc/150?img=7', 'BBQ Master', 'USER', 1, 1, NOW(), NOW()),
('user8', 'user8@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Bù Thị Lan', 'https://i.pravatar.cc/150?img=8', 'Dessert Queen', 'USER', 1, 1, NOW(), NOW()),
('user9', 'user9@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Ngô Văn Mạnh', 'https://i.pravatar.cc/150?img=9', 'Food Vlogger', 'USER', 1, 1, NOW(), NOW()),
('user10', 'user10@cooking.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkW', 'Phan Thị Oanh', 'https://i.pravatar.cc/150?img=10', 'Vegan Chef', 'USER', 1, 1, NOW(), NOW());

-- =====================================================
-- CATEGORIES
-- =====================================================

INSERT IGNORE INTO categories (name, description, is_active, created_at) VALUES
('Món ăn sáng', 'Các món ăn sáng Việt Nam', 1, NOW()),
('Món chính', 'Các món ăn chính', 1, NOW()),
('Món tráng miệng', 'Tráng miệng và đồ ngọt', 1, NOW()),
('Món súp', 'Súp và canh', 1, NOW()),
('Món xào', 'Món xào nhanh', 1, NOW());

-- =====================================================
-- RECIPES (ingredients và nutrition dùng @ separator)
-- =====================================================

INSERT INTO recipes (user_id, category_id, title, description, ingredients, nutrition, prep_time, cook_time, servings, difficulty_level, image_url, status, view_count, like_count, created_at, updated_at) VALUES
-- PHỞ BÒ
(1, 1, 'Phở Bò Hà Nội', 'Món phở bò truyền thống Hà Nội với nước dùng trong ngọt từ xương hầm', 
'Xương bò: 2kg@Thịt bò nạm: 500g@Bánh phở tươi: 500g@Hành tây: 2 củ@Gừng tươi: 100g@Hoa hồi: 5 cái@Quế: 2 thanh@Thảo quả: 2 quả@Hạt tiêu: 1 muỗng canh@Nước mắm: 3 muỗng canh@Đường phèn: 2 muỗng canh@Hành lá: 100g@Rau thơm: 200g', 
'Calories: 450 kcal@Protein: 25g@Carbs: 55g@Fat: 12g@Fiber: 3g', 
30, 240, 4, 'MEDIUM', 'https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43', 'APPROVED', 523, 89, NOW(), NOW()),

-- BÚN CHẢ
(1, 2, 'Bún Chả Hà Nội', 'Bún chả thơm ngon với chả nướng than hoa, thịt nướng thơm phức', 
'Thịt ba chỉ: 500g@Thịt nạc vai: 300g@Bún tươi: 400g@Tỏi: 50g@Hành khô: 50g@Đường: 3 muỗng canh@Nước mắm: 100ml@Tiêu: 1 muỗng cà phê@Ớt: 3 quả@Giấm: 2 muỗng canh@Rau sống: 300g', 
'Calories: 520 kcal@Protein: 28g@Carbs: 48g@Fat: 22g@Fiber: 4g', 
30, 25, 3, 'EASY', 'https://images.unsplash.com/photo-1559314809-0d155014e29e', 'APPROVED', 412, 76, NOW(), NOW()),

-- CƠM TẤM
(2, 2, 'Cơm Tấm Sườn Nướng', 'Cơm tấm sườn nướng thơm lừng mùi than hoa', 
'Sườn heo: 500g@Cơm tấm: 400g@Trứng: 4 quả@Tỏi: 50g@Sả: 2 cây@Đường: 3 muỗng canh@Nước mắm: 50ml@Dầu ăn: 2 muỗng canh@Hành phi: 50g@Đồ chua: 200g@Dưa leo: 1 trái', 
'Calories: 620 kcal@Protein: 32g@Carbs: 68g@Fat: 24g@Fiber: 3g', 
20, 25, 3, 'MEDIUM', 'https://images.unsplash.com/photo-1512058564366-18510be2db19', 'APPROVED', 544, 98, NOW(), NOW()),

-- BÁNH MÌ
(2, 1, 'Bánh Mì Thịt Pate', 'Bánh mì Việt Nam giòn rụm với nhân thịt nguội, pate gan', 
'Bánh mì: 4 ổ@Pate gan: 100g@Thịt nguội: 200g@Dưa leo: 1 trái@Rau mùi: 50g@Ngò gai: 30g@Nước tương: 30ml@Ớt: 2 quả', 
'Calories: 380 kcal@Protein: 18g@Carbs: 45g@Fat: 14g@Fiber: 3g', 
10, 5, 4, 'EASY', 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea', 'APPROVED', 671, 132, NOW(), NOW()),

-- GỎI CUỐN
(3, 1, 'Gỏi Cuốn Tôm Thịt', 'Gỏi cuốn tươi mát với tôm, thịt luộc, rau sống cuốn bánh tráng', 
'Bánh tráng: 20 tờ@Tôm tươi: 300g@Thịt ba chỉ: 200g@Bún tươi: 200g@Xà lách: 150g@Húng quế: 50g@Rau mùi: 50g@Dưa leo: 1 trái@Nước mắm pha: 150ml', 
'Calories: 180 kcal@Protein: 15g@Carbs: 22g@Fat: 4g@Fiber: 2g', 
30, 15, 4, 'EASY', 'https://images.unsplash.com/photo-1617093727343-374698b1b08d', 'APPROVED', 389, 67, NOW(), NOW()),

-- BÁNH XÈO
(3, 2, 'Bánh Xèo Miền Tây', 'Bánh xèo giòn rụm nhân tôm thịt, giá đỗ', 
'Bột bánh xèo: 300g@Nước cốt dừa: 200ml@Tôm tươi: 250g@Thịt ba chỉ: 200g@Giá đỗ: 300g@Hành lá: 100g@Nghệ bột: 1 muỗng@Dầu ăn: 100ml@Rau sống: 300g@Nước mắm: 100ml', 
'Calories: 410 kcal@Protein: 22g@Carbs: 42g@Fat: 18g@Fiber: 4g', 
25, 20, 4, 'MEDIUM', 'https://images.unsplash.com/photo-1626804475297-41608ea09aeb', 'APPROVED', 456, 84, NOW(), NOW()),

-- CÁ KHO
(4, 2, 'Cá Kho Tộ', 'Cá kho tộ đậm đà theo kiểu miền Nam, cá thấm gia vị', 
'Cá basa: 600g@Thịt ba chỉ: 100g@Nước dừa: 200ml@Đường: 50g@Nước mắm: 80ml@Tỏi: 50g@Ớt: 3 quả@Hành khô: 50g@Tiêu: 1 muỗng', 
'Calories: 340 kcal@Protein: 28g@Carbs: 18g@Fat: 16g@Fiber: 1g', 
15, 35, 3, 'EASY', 'https://images.unsplash.com/photo-1563245372-f21724e3856d', 'APPROVED', 367, 71, NOW(), NOW()),

-- BÚN BÒ HUẾ
(4, 4, 'Bún Bò Huế', 'Bún bò Huế cay nồng đặc trưng xứ Huế với nước dùng đỏ cam', 
'Xương bò: 1kg@Thịt bò: 400g@Giò heo: 300g@Chả lụa: 200g@Bún: 500g@Sả: 100g@Mắm ruốc: 50g@Ớt bột: 2 muỗng@Nước mắm: 50ml@Hành lá: 50g@Rau sống: 300g', 
'Calories: 480 kcal@Protein: 26g@Carbs: 52g@Fat: 18g@Fiber: 4g', 
30, 180, 4, 'HARD', 'https://images.unsplash.com/photo-1585032226651-759b368d7246', 'APPROVED', 598, 115, NOW(), NOW()),

-- BÁNH FLAN
(5, 3, 'Bánh Flan Caramel', 'Bánh flan mịn màng béo ngậy với lớp caramel đắng ngọt', 
'Trứng gà: 6 quả@Sữa đặc: 200ml@Sữa tươi: 300ml@Đường trắng: 150g@Vanilla: 5ml@Muối: 1 nhúm', 
'Calories: 220 kcal@Protein: 8g@Carbs: 32g@Fat: 8g@Sugar: 28g', 
20, 45, 8, 'MEDIUM', 'https://images.unsplash.com/photo-1551024506-0bccd828d307', 'APPROVED', 423, 88, NOW(), NOW()),

-- CHẢ CÁ
(5, 2, 'Chả Cá Lã Vọng', 'Chả cá Hà Nội thơm ngon với tinh bột nghệ, ăn kèm bún', 
'Cá lăng: 500g@Bún tươi: 400g@Rau thì là: 200g@Nghệ tươi: 30g@Mắm tôm: 100ml@Tỏi: 50g@Hành lá: 100g@Dầu ăn: 100ml@Đậu phộng rang: 50g@Nước mắm: 50ml', 
'Calories: 390 kcal@Protein: 30g@Carbs: 35g@Fat: 14g@Fiber: 3g', 
25, 20, 3, 'MEDIUM', 'https://images.unsplash.com/photo-1534422298391-e4f8c172dddb', 'APPROVED', 501, 92, NOW(), NOW());

-- =====================================================
-- RECIPE IMAGES với Instructions
-- =====================================================

-- Phở Bò (ID 1)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(1, 'https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43', 1, 'Chần xương bò qua nước sôi, vớt ra rửa sạch để loại bỏ tạp chất.', NOW()),
(1, 'https://images.unsplash.com/photo-1569562211093-4ed0d0758f12', 0, 'Nướng hành tây và gừng trên bếp ga cho thơm, cạo sạch phần cháy.', NOW()),
(1, 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1', 0, 'Hầm xương với gia vị trong 3-4 tiếng, vớt bọt thường xuyên.', NOW()),
(1, 'https://images.unsplash.com/photo-1565299507177-b0ac66763828', 0, 'Trình bày: Cho phở vào tô, xếp thịt, chan nước dùng nóng hổi.', NOW());

-- Bún Chả (ID 2)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(2, 'https://images.unsplash.com/photo-1559314809-0d155014e29e', 1, 'Ướp thịt với tỏi, hành, đường, nước mắm, tiêu trong 30 phút.', NOW()),
(2, 'https://images.unsplash.com/photo-1562565652-a0d8f0c59eb4', 0, 'Nướng chả và thịt trên than hoa đến vàng đều, thơm phức.', NOW()),
(2, 'https://images.unsplash.com/photo-1504674900247-0877df9cc836', 0, 'Pha nước mắm chua ngọt, cho chả thịt vào tô, ăn kèm bún rau.', NOW());

-- Cơm Tấm (ID 3)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(3, 'https://images.unsplash.com/photo-1512058564366-18510be2db19', 1, 'Ướp sườn với tỏi sả đường nước mắm, ướp qua đêm cho thấm.', NOW()),
(3, 'https://images.unsplash.com/photo-1585032226651-759b368d7246', 0, 'Nướng sườn trên than hoa khoảng 20 phút cho chín vàng đều.', NOW());

-- Bánh Mì (ID 4)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(4, 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea', 1, 'Nướng bánh mì cho giòn, rạch dọc và phết pate đều lên.', NOW()),
(4, 'https://images.unsplash.com/photo-1559847844-5315695dadae', 0, 'Xếp thịt nguội, dưa leo, rau thơm vào bánh và thưởng thức.', NOW());

-- Gỏi Cuốn (ID 5)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(5, 'https://images.unsplash.com/photo-1617093727343-374698b1b08d', 1, 'Luộc tôm và thịt, chuẩn bị rau sống rửa sạch.', NOW()),
(5, 'https://images.unsplash.com/photo-1582169296194-e4d644c48063', 0, 'Nhúng bánh tráng vào nước, đặt nhân vào giữa và cuốn chặt.', NOW());

-- Bánh Xèo (ID 6)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(6, 'https://images.unsplash.com/photo-1626804475297-41608ea09aeb', 1, 'Pha bột với nước cốt dừa và nghệ, để nghỉ 30 phút.', NOW()),
(6, 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1', 0, 'Đổ bánh mỏng, thêm nhân tôm thịt giá, gấp đôi và chiên giòn.', NOW());

-- Cá Kho (ID 7)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(7, 'https://images.unsplash.com/photo-1563245372-f21724e3856d', 1, 'Làm nước màu caramel từ đường, cẩn thận không để cháy.', NOW()),
(7, 'https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8', 0, 'Kho cá với nước dừa và gia vị đến khi thấm, nước sệt lại.', NOW());

-- Bún Bò Huế (ID 8)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(8, 'https://images.unsplash.com/photo-1585032226651-759b368d7246', 1, 'Hầm xương bò, rang sả mắm ruốc với ớt bột cho thơm.', NOW()),
(8, 'https://images.unsplash.com/photo-1565299507177-b0ac66763828', 0, 'Trình bày bún với thịt bò, giò, chan nước dùng đỏ cam cay nồng.', NOW());

-- Bánh Flan (ID 9)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(9, 'https://images.unsplash.com/photo-1551024506-0bccd828d307', 1, 'Làm caramel vàng nâu, đổ vào khuôn và xoay đều.', NOW()),
(9, 'https://images.unsplash.com/photo-1504674900247-0877df9cc836', 0, 'Trộn trứng sữa vanilla, lọc qua rây và hấp 45 phút lửa nhỏ.', NOW());

-- Chả Cá (ID 10)
INSERT INTO RecipeImage (recipe_id, image_url, is_primary, instructions, created_at) VALUES
(10, 'https://images.unsplash.com/photo-1534422298391-e4f8c172dddb', 1, 'Ướp cá với nghệ tươi giã nhuyễn, chiên vàng đều hai mặt.', NOW()),
(10, 'https://images.unsplash.com/photo-1565958011703-44f9829ba187', 0, 'Xào cá với hành lá thì là, ăn kèm bún và mắm tôm pha.', NOW());

-- =====================================================
-- KẾT QUẢ
-- =====================================================

SELECT 'Seed completed!' AS message;
SELECT COUNT(*) AS users FROM users;
SELECT COUNT(*) AS recipes FROM recipes;
SELECT COUNT(*) AS recipe_images FROM RecipeImage;
