package model;

public class product {
    private int id;
    private String name;
    private String description;
    private double price;
    private byte[] image; 
    private String label;
    private Integer quantity;  
   
    private String status; 

   
    public product() {
    }

    
    public product(int id, String name, String description, double price, byte[] image, String label, Integer quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.label = label;
        this.quantity = quantity;
        this.status = (quantity > 0) ? "Còn hàng" : "Hết hàng";
    }

  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

  
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

   
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

  
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        // Cập nhật status mỗi khi quantity thay đổi
        this.status = (quantity > 0) ? "Còn hàng" : "Hết hàng";
    }

    // Getter cho status (status tự động tính toán từ quantity)
    public String getStatus() {
       return (quantity > 0) ? "Còn hàng" : "Hết hàng";
    }

    // Setter cho status (không cần sử dụng, vì status tự động tính từ quantity)
    public void setStatus(String status) {
        this.status = status;
    }
}
