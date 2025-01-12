package com.EmbersTrial.Enemy;
//basic enemy stats now just need to make it as the waves increase
public class Enemy {
    // Base stats for the enemy
    private String name;
    private double hp;
    private double defense;
    private double damage;
    private double attackSpeed;

    // Constructor for setting stats
    public Enemy(String name, double hp, double defense, double damage, double attackSpeed) {
        this.name = name;
        this.hp = hp;
        this.defense = defense;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
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
