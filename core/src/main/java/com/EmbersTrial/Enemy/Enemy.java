package com.EmbersTrial.Enemy;

// Basic enemy stats now scale with waves
public class Enemy {
    // Track the number of waves to dynamically increase stats
    private double numWaves = 1;

    // Base stats for the enemy
    private String name;
    private double hp;
    private double defense;
    private double damage;
    private double attackSpeed;

    // Constructor for setting stats
    public Enemy(String name, double baseHp, double baseDefense, double baseDamage, double baseAttackSpeed) {
        this.name = name;
        this.hp = baseHp + (numWaves * 10); // Increase HP with waves
        this.defense = baseDefense + (numWaves * 2); // Increase Defense with waves
        this.damage = baseDamage + (numWaves * 3); // Increase Damage with waves
        this.attackSpeed = baseAttackSpeed + (numWaves * 0.1); // Slightly increase Attack Speed with waves
    }

    // Increment wave count and update scaling factor
    public double incrementWaves() {
        numWaves++;
        return numWaves;
    }

    // Apply damage to this enemy's HP
    public void takeDamage(double attackDamage, double defense) {
        double effectiveDamage = Math.max(attackDamage - defense, 0); // No negative damage
        hp -= effectiveDamage;
        if (hp < 0) {
            hp = 0; // Ensure HP doesn't go below 0
        }
        System.out.println(name + " took " + effectiveDamage + " damage. Remaining HP: " + hp);
    }


    // Inner classes for specific enemy types with predefined stats
    public static class Goblin extends Enemy {
        public Goblin() {
            super("Goblin", 50, 5, 10, 1.2); // Goblin stats
        }
    }

    public static class Dragon extends Enemy {
        public Dragon() {
            super("Dragon", 500, 20, 50, 0.5); // Dragon stats
        }
    }

    public static class Knight extends Enemy {
        public Knight() {
            super("Knight", 150, 15, 25, 0.9); // Knight stats
        }
    }

    public static class Bandit extends Enemy {
        public Bandit() {
            super("Bandit", 70, 8, 15, 1.1); // Bandit stats
        }
    }

    public static class Archers extends Enemy {
        public Archers() {
            super("Archers", 60, 4, 12, 1.5); // Archers stats
        }
    }

    public static class Slimes extends Enemy {
        public Slimes() {
            super("Slimes", 30, 2, 5, 0.7); // Slimes stats
        }
    }
}
