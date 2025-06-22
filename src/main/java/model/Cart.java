
package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    // Map sử dụng Integer làm key, điều này là đúng
    private Map<Integer, CartItem> items = new HashMap<>();

    public void addItem(CartItem item) {
        // Giả sử product.getId() trả về int
        int productId = item.getProduct().getId(); 
        
        if (items.containsKey(productId)) {
            CartItem existingItem = items.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            items.put(productId, item);
        }
    }

    // Phương thức removeItem cũng phải dùng int
    public void removeItem(int productId) {
        items.remove(productId);
    }

    // ---- ĐÂY LÀ PHƯƠNG THỨC ĐÃ SỬA LỖI ----
    // Đổi tên thành updateItemQuantity cho khớp với Servlet
    // Quan trọng nhất: Đổi tham số từ String -> int
    public void updateItemQuantity(int productId, int newQuantity) {
        // Nếu số lượng mới <= 0, ta xóa sản phẩm
        if (newQuantity <= 0) {
            removeItem(productId);
            return;
        }

        // Bây giờ items.get(productId) sẽ hoạt động vì cả hai đều là kiểu số
        CartItem item = items.get(productId); 
        if (item != null) {
            item.setQuantity(newQuantity);
        }
    }
// Dán phương thức này vào bên trong file Cart.java

public CartItem getItemById(int productId) {
    // items là Map<Integer, CartItem> của bạn
    // Phương thức .get() của Map sẽ lấy ra CartItem tương ứng với productId
    return items.get(productId);
}

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public double getTotalAmount() {
        return items.values().stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}