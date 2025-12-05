# 🤖 AI Chatbot Guide - Hướng Dẫn Sử Dụng

## 📌 Tổng Quan
Chatbot AI của ứng dụng nấu ăn giúp bạn tìm kiếm món ăn một cách thông minh và tự nhiên. Chatbot hiểu tiếng Việt (có/không dấu) và tiếng Anh.

---

## ✨ Tính Năng Mới

### 1. **Chào Hỏi Thông Minh** 👋
Chatbot nhận biết thời gian trong ngày và chào hỏi phù hợp.

**Câu hỏi:**
- "Xin chào"
- "Hello"
- "Chào buổi sáng"
- "Good morning"

**Trả lời:**
```
Chào buổi sáng! Tôi là trợ lý AI cho ứng dụng nấu ăn của bạn! 🍳

Tôi có thể giúp bạn:
🔥 Tìm món đang hot và được yêu thích
🥘 Tìm món theo nguyên liệu có sẵn
⏱️ Tìm món theo thời gian nấu
📊 Tìm món theo độ khó
👨‍👩‍👧‍👦 Tìm món theo số người ăn
🔍 Tìm công thức theo tên món

Bạn muốn tìm món gì hôm nay? 😊
```

---

### 2. **Tìm Theo Độ Khó** 📊

**Câu hỏi:**
- "Tìm món dễ làm"
- "Cho tôi món khó"
- "Món nào đơn giản?"
- "Tìm món trung bình"

**Trả lời:**
- Danh sách món ăn theo độ khó: **Dễ**, **Trung bình**, hoặc **Khó**
- Sắp xếp theo độ phổ biến

**Ví dụ:**
```
User: "Tìm món dễ làm"

Bot: 
"Đây là những món ăn mức độ dễ dành cho bạn:"
[Danh sách 10 món ăn dễ làm]
```

---

### 3. **Tìm Theo Thời Gian Nấu** ⏱️

**Câu hỏi:**
- "Tìm món nấu trong 30 phút"
- "Món nào nấu nhanh?"
- "Tìm món nấu dưới 1 giờ"
- "Món gấp" (tự động hiểu là ≤ 30 phút)

**Trả lời:**
- Món ăn có tổng thời gian (prep + cook) ≤ thời gian yêu cầu
- Sắp xếp từ nhanh nhất đến chậm nhất

**Ví dụ:**
```
User: "Tìm món nấu trong 45 phút"

Bot:
"Đây là những món ăn có thể nấu xong trong 45 phút:"
[Danh sách món ≤ 45 phút, sort tăng dần]
```

**Đơn vị hỗ trợ:**
- `phút`, `minute`, `min` → phút
- `giờ`, `hour` → giờ (tự động convert sang phút)

---

### 4. **Tìm Theo Số Người Ăn** 👨‍👩‍👧‍👦

**Câu hỏi:**
- "Món cho 4 người"
- "Tìm món cho 2 người ăn"
- "6 phần ăn"

**Trả lời:**
- Món ăn phù hợp với số người (±2 người tolerance)
- Sắp xếp theo độ chính xác

**Ví dụ:**
```
User: "Món cho 4 người"

Bot:
"Đây là những món ăn cho 4 người:"
[Món cho 2-6 người, ưu tiên 4 người]
```

---

### 5. **Tìm Theo Nguyên Liệu** 🥘

**Câu hỏi:**
- "Tìm món có gà và khoai tây"
- "Món nào dùng tôm?"
- "Làm món từ thịt bò và cà chua"

**Trả lời:**
- Món có chứa các nguyên liệu được mention
- Sắp xếp theo số nguyên liệu khớp

**Nguyên liệu được hỗ trợ:**
- Thịt: gà, thịt bò, bò, heo, thịt heo
- Hải sản: cá, tôm, mực
- Rau củ: khoai tây, cà chua, hành, tỏi, ớt, rau, cà rốt, bắp cải
- Khác: trứng, sữa, bơ, phô mai, nấm, đậu...

---

### 6. **Món Hot & Được Yêu Thích** 🔥❤️

**Câu hỏi:**
- "Món nào đang hot?"
- "Cho tôi xem món được yêu thích nhất"
- "Top món ăn"

**Trả lời:**
```
User: "Món nào đang hot?"

Bot:
"🔥 Đây là Top 10 món ăn đang HOT nhất (nhiều lượt xem):"
[Danh sách sorted by view count]
```

---

### 7. **Tìm Theo Tên Món** 🔍

**Câu hỏi:**
- "Tìm món phở"
- "Cách nấu bún bò"
- "Công thức làm bánh mì"

**Trả lời:**
```
User: "Tìm món phở"

Bot:
"🔍 Kết quả tìm kiếm cho 'phở' (5 món):"
[Danh sách các món có tên chứa 'phở']
```

---

### 8. **Tương Tác Xã Giao** 💬

#### **Cảm ơn**
```
User: "Cảm ơn bạn"

Bot:
"Không có gì! Rất vui được giúp bạn. 😊
Nếu cần tìm thêm món ăn khác, cứ hỏi tôi nhé!"
```

#### **Tạm biệt**
```
User: "Tạm biệt"

Bot:
"Tạm biệt! Chúc bạn nấu ăn ngon miệng! 👋🍽️
Hẹn gặp lại bạn lần sau nhé!"
```

#### **Hỏi thăm**
```
User: "Bạn khỏe không?"

Bot:
"Tôi rất khỏe, cảm ơn bạn đã hỏi! 😊
Tôi luôn sẵn sàng giúp bạn tìm món ăn ngon.
Còn bạn thì sao? Muốn tìm món gì hôm nay không?"
```

---

### 9. **Hướng Dẫn Sử Dụng** 📖

**Câu hỏi:**
- "Giúp tôi"
- "Hướng dẫn"
- "Bạn có thể làm gì?"

**Trả lời:**
```
📖 Hướng dẫn sử dụng:

🔥 Món hot: "Cho tôi xem món nào đang hot?"
❤️ Món yêu thích: "Món nào được yêu thích nhất?"
🥘 Tìm theo nguyên liệu: "Tìm món có gà và khoai tây"
⏱️ Tìm theo thời gian: "Tìm món nấu trong 30 phút"
📊 Tìm theo độ khó: "Tìm món dễ làm" hoặc "món khó"
👨‍👩‍👧‍👦 Tìm theo số người: "Món cho 4 người"
🔍 Tìm theo tên: "Tìm món phở" hoặc "Cách nấu bún bò"

Hãy thử hỏi tôi bất cứ điều gì về món ăn nhé! 😊
```

---

## 🎯 Câu Hỏi Phức Tạp

Chatbot có thể hiểu và trả lời câu hỏi kết hợp nhiều điều kiện:

### Ví dụ 1: Độ khó + Thời gian
```
User: "Tìm món dễ làm trong 30 phút"

→ Chatbot sẽ ưu tiên tìm theo thời gian (30 phút)
   hoặc có thể xử lý cả 2 điều kiện
```

### Ví dụ 2: Nguyên liệu + Số người
```
User: "Món có gà cho 4 người"

→ Tìm món có gà, sau đó filter theo servings
```

### Ví dụ 3: Không dấu
```
User: "tim mon co ga de lam nhanh"

→ Chatbot tự động chuẩn hóa và hiểu:
   "Tìm món có gà dễ làm nhanh"
```

---

## 🚀 Cải Tiến So Với Phiên Bản Cũ

| Tính năng | Cũ | Mới |
|-----------|-----|-----|
| Chào hỏi | Cố định | Context-aware (theo giờ) |
| Tìm theo độ khó | ❌ | ✅ |
| Tìm theo thời gian | ❌ | ✅ |
| Tìm theo số người | ❌ | ✅ |
| Cảm ơn/Tạm biệt | ❌ | ✅ |
| Hỏi thăm | ❌ | ✅ |
| Emoji | Ít | Nhiều, sinh động hơn |
| Response format | Đơn giản | Có icon, số lượng kết quả |

---

## 💡 Tips Sử Dụng

1. **Không cần dấu**: Chatbot hiểu cả "tim mon" và "tìm món"
2. **Số tự nhiên**: Gõ "30 phút" hoặc "30 phút" đều được
3. **Linh hoạt**: Thử nhiều cách hỏi khác nhau
4. **Kết hợp**: Hỏi nhiều điều kiện trong 1 câu
5. **Feedback**: Nếu không hiểu, chatbot sẽ gợi ý cách hỏi

---

## 🔧 Technical Details

### Supported Languages
- Vietnamese (with/without accents)
- English basic keywords

### NLP Techniques
- Text normalization (remove accents)
- Keyword matching with synonyms
- Number extraction (time, servings)
- Multi-intent detection
- Fuzzy matching for ingredients

### Response Limits
- Maximum 10 recipes per query
- Sorted by relevance/score

---

## 📞 Support

Nếu chatbot không hiểu câu hỏi, hãy thử:
1. Gõ **"giúp"** để xem hướng dẫn
2. Đơn giản hóa câu hỏi
3. Sử dụng ví dụ trong guide này
4. Liên hệ admin nếu có bug

---

**Chúc bạn trải nghiệm chatbot vui vẻ! 🎉**
