package com.EmbersTrial.player;

public class Stats {
    private int health;
    private int maxHealth;
    private int exp;
    private int level;
    private float stamina;
    private float maxStamina;

    public Stats(int health, int exp) {
        this.health = health;
        this.maxHealth = health;
        this.exp = exp;
        this.level = 1;
        this.maxStamina = 100;
        this.stamina = maxStamina;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public void gainExp(int amount) {
        exp += amount;
        checkLevelUp();
    }

    public void reduceStamina(float amount) {
        stamina = Math.max(0, stamina - amount); // Reduce but don't go below 0
    }

    public void rechargeStamina(float amount) {
        stamina = Math.min(maxStamina, stamina + amount); // Recharge but don't exceed max
    }

    private void checkLevelUp() {
        int requiredExp = level * 100; //example EXP formula
        if (exp >= requiredExp) {
            exp -= requiredExp;
            level++;
            maxHealth += 20; //increase max health on level up
            health = maxHealth; //restore health
        }
    }

    public int getHealth() {
        return health;
    }

    public int getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public float getMaxStamina() {
        return maxStamina;
    }

    public float getStamina() {
        return stamina;
    }
}
