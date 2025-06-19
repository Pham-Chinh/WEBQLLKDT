/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class CartItem {
    private product Product;
    private int quantity;

    public CartItem(product Product, int quantity) {
        this.Product = Product;
        this.quantity = quantity;
    }

    public product getProduct() {
        return Product;
    }

    public void setProduct(product Product) {
        this.Product = Product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice(){
        return Product.getPrice()*quantity;
    }
    
}
