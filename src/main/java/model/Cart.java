/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class Cart {
    private Map <Integer,CartItem> items = new HashMap<>();
    public void addItem(CartItem item){
     int id = item.getProduct().getId();
     if(items.containsKey(id)){
         CartItem existing = items.get(id);
         existing.setQuantity(existing.getQuantity() + item.getQuantity());
         
     }
     else{
         items.put(id,item);
     }
    }
    public void removeItem(int productID){
        items.remove(productID);
    }
    public void updateItem(String productID , int quantity){
        CartItem item = items.get(productID);
        if (item != null)item.setQuantity(quantity);
        
    }
    public Collection<CartItem> getItems(){
        return items.values();
    }
    public double getTotalAmount(){
        return items.values().stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
    public void clear(){
        items.clear();
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
