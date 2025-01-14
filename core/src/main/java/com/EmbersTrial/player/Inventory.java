package com.EmbersTrial.player;

import com.EmbersTrial.Items.ItemClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Inventory {
    private List<String> items; //list to store inventory items
    private String equippedWeapon;
    private String equippedArmor;
    private int size = 4;
    private ItemClass.Item[] Ainventory;// place holder for array
    private Vector Winventory = new Vector(size);
    private int upgradecount = 1;// place holder for upgrade count
    public Inventory() {
        // Initialize the ArrayList
        items = new ArrayList<>();
        equippedWeapon = "None";
        equippedArmor = "None";
        Ainventory = new ItemClass.Item[size];
        Winventory = new Vector(size);
    }


    public boolean addItem(ItemClass.Item Ainventory[], int size,ItemClass.Item item) {// for armor inventory
        if (size >= 0 && size < Ainventory.length) {
            Ainventory[size] = item;
            return true;//add item to the inventory
        }
        return false;
    }
    public void addVectorItem(ItemClass.Item item){
        Winventory.add(item);
    }
    public void removeVectorItem ( int index){
        if (index >= 0 && index < Winventory.size()){
            Winventory.remove(index);
        }
    }
    public void upgradePoint(){
        Winventory.setSize(Winventory.size()+upgradecount);
    }

    public boolean removeItem(ItemClass.Item inventory[], int size) {//for amro inventory
        if (size >= 0 && size < inventory.length) {
            inventory[size] = null;
            return true;//remove item from the inventory
        }
        return false;
    }

    public void equipWeapon(String weapon) {
        if (items.contains(weapon)) {
            equippedWeapon = weapon;
        } else {
            System.out.println("Weapon not in inventory!");
        }
    }

    public void equipArmor(String armor) {
        if (items.contains(armor)) {
            equippedArmor = armor;
        } else {
            System.out.println("Armor not in inventory!");
        }
    }

    public String getEquippedWeapon() {
        return equippedWeapon;
    }

    public String getEquippedArmor() {
        return equippedArmor;
    }

    public List<String> getItems() {
        return items; //return the current list of items
    }
}
