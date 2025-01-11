package com.EmbersTrial.player;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<String> items; //list to store inventory items
    private String equippedWeapon;
    private String equippedArmor;

    public Inventory() {
        // Initialize the ArrayList
        items = new ArrayList<>();
        equippedWeapon = "None";
        equippedArmor = "None";
    }

    public void addItem(String item) {
        items.add(item); //add item to the inventory
    }

    public void removeItem(String item) {
        items.remove(item); //remove item from the inventory
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
