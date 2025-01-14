package com.EmbersTrial.player;

public class Stats {
    private int health;
    private int maxHealth;
    private int exp;
    private int level;


    public Stats(int health, int exp) {
        this.health = health;
        this.maxHealth = health;
        this.exp = exp;
        this.level = 1;

    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    public void gainExp(int amount) {
        exp += amount;
        checkLevelUp();
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
}
