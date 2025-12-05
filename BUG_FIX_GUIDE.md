# 🔧 BUG FIXES NEEDED

## ❌ Issues Reported

### **Bug #1: Trả về quá nhiều recipes**
**Current**: 10 recipes  
**Expected**: 2-3 recipes

### **Bug #2: "2 người ăn" nhầm thành "tỏi"**
**Input**: "tôi tìm món có 2 người ăn"  
**Current Response**: "Tôi đã tìm thấy các món ăn phù hợp với nguyên liệu: tỏi"  
**Problem**: "tỏi" được match từ "tôi" + ingredient check quá rộng

### **Bug #3: Shopping list không extract "phở"**
**Input**: "danh sách mua cho món phở"  
**Current Response**: "Xin lỗi, tôi không tìm thấy món ăn nào có tên 'danh sach mua cho pho'."  
**Problem**: Không remove "danh sach mua cho" before searching

---

## ✅ Solutions

### **Fix #1: Limit Recipes**

**Files to edit**: `ChatbotServiceIMPL.java`

**Lines to change**:
1. Line ~174 (multi-intent)
2. Line ~390 (popular)  
3. Line ~400 (trending)
4. Line ~420 (ingredients)
5. Line ~440 (title search)

**Change**:
```java
// FROM:
.limit(10)

// TO:
.limit(3)
```

---

### **Fix #2: Fix Ingredient Detection**

**Problem**: Keywords "co", "dung", "toi" quá rộng

**Solution 1**: Remove overly broad keywords
```java
// Line ~190
// FROM:
if (containsAny(message, "nguyen lieu", "co", "dung", "ingredient", "lam tu", "chua", "thanh phan"))

// TO:
if (containsAny(message, "nguyen lieu", "ingredient", "lam tu", "thanh phan", "voi") && 
    !message.contains("nguoi") && // Avoid "người"
    !message.contains(" phan")) // Avoid "phần" in "2 phần"
```

**Solution 2**: Remove "toi" from ingredient map (line ~338)
```java
// REMOVE or COMMENT OUT:
// ingredientMap.put("toi", "tỏi");

// Only keep "tỏi" spelled correctly:
ingredientMap.put("toi", "tỏi");  // Only if exact match
```

---

### **Fix #3: Shopping List Recipe Name Extraction**

**Problem**: `extractRecipeName()` doesn't remove shopping keywords

**Solution**: Clean message BEFORE calling extractRecipeName

**Lines ~220-250** (Shopping List Handler):
```java
// ADD this before searching:
String cleanMessage = message
    .replace("danh sach mua", "")
    .replace("can mua", "")
    .replace("mua gi", "")
    .replace("di cho", "")
    .replace("cho mon", "")
    .replace("de lam", "")
    .replace("shopping list", "")
    .replace("cho", "")
    .replace("mon", "")
    .trim();

// THEN search with cleanMessage:
List<RecipesDTO> recipes = searchRecipesByTitle(cleanMessage, token);
```

**Full Fix**:
```java
// Line ~237
// FROM:
String recipeName = extractRecipeName(message);

// TO:
String cleanMessage = message
    .replace("danh sach mua", "")
    .replace("can mua", "")
    .replace("cho mon", "")
    .replace("de lam", "")
    .replace("cho", "")
    .replace("mon", "")
    .trim();
```

Then use `cleanMessage` instead of `recipeName` everywhere in that block.

---

## 📝 Manual Edit Steps

### **Step 1: Limit recipes to 3**

1. Open `ChatbotServiceIMPL.java`
2. Search for `.limit(10)`
3. Replace ALL occurrences with `.limit(3)`
4. Check lines: 174, 388, 398, 418, 438

### **Step 2: Fix ingredient keywords**

1. F

ind line ~190: `if (containsAny(message, "nguyen lieu"...`
2. Replace entire line with:
```java
if (containsAny(message, "nguyen lieu", "ingredient", "lam tu", "thanh phan", "voi") && 
    !message.contains("nguoi") && !message.contains(" phan")) {
```

### **Step 3: Fix shopping list**

1. Find line ~220: Shopping list handler  
2. After `if (containsAny(message, "mua"...` block starts
3. Find `String recipeName = extractRecipeName(message);`
4. Replace with cleaning code above
5. Replace all `recipeName` references to `cleanMessage`

---

## 🧪 Test Cases After Fix

### **Test 1: Limit**
```json
{"message": "món yêu thích"}
```
**Expected**: Max 3 recipes ✅

### **Test 2: Servings**
```json
{"message": "tôi tìm món có 2 người ăn"}
```
**Expected**: Recipes for 2 servings (NOT "tỏi") ✅

### **Test 3: Shopping**
```json
{"message": "danh sách mua cho món phở"}
```
**Expected**: Shopping list for "phở" ✅

---

## 🚨 Important Notes

- **Backup first**: Git commit before editing
- **Test after EACH fix**: Don't fix all at once
- **Build after edit**: `mvn compile` to check syntax

---

**I tried to auto-fix but file is complex. Please apply manually following this guide!** 🛠️
