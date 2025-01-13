package com.EmbersTrial.Items;
import java.util.Random;
//the base value of each Item is the beginning now just change each item value to be a variable and create new items that just implement the methods
// Abstract class Item
public class ItemClass {
    public double hpUP = 100;// this is a placeholder hp thing

    class Drops {
        public double Drop(double Droprate, double hpUp) {//The Drop rate will increase perwave and then when they get that item the droprate will reset
            Random random=new Random();
            double currentHealth = hpUp;

        }
    }

    class potion {
        public potion(double hpUP) {
            double hpUp = 100;//place holder for max hp
            double heal = hpUp * 0.2;
            hpUp = hpUp + heal; //this is just a place holder for heals calc
        }
    }

    abstract class Item implements Weapons {
        protected String name;
        protected double baseDamage;
        protected double baseAttackRate;
        protected int upgradeCount;

        // Constructor
        public Item(String name, double baseDamage, double baseAttackRate, int upgradeCount) {
            this.name = name;
            this.baseDamage = baseDamage;
            this.baseAttackRate = baseAttackRate;
            this.upgradeCount = upgradeCount;
        }

        public String getName() {
            return name;
        }

        public void setUpgradeCount(int upgradeCount) {
            this.upgradeCount = upgradeCount;
        }
    }

    // Interface for Weapons
    interface Weapons {
        double damage();      // Calculate damage

        double attackRate();  // Calculate attack rate
    }

    // Armor Interface
    interface Armor {
        double Defense(double baseDefense); // Calculate defense

        double HpUp(double baseHpUp);       // Calculate HPUp
    }

    // Class for Armor Upgrades
    class ArmourValues implements Armor {
        public int upgradeCount;

        // Constructor
        public ArmourValues(int upgradeCount) {
            this.upgradeCount = upgradeCount;
        }

        @Override
        public double Defense(double baseDefense) {
            return baseDefense * upgradeCount;
        }

        @Override
        public double HpUp(double baseHpUp) {
            return baseHpUp * upgradeCount;
        }

        public int getUpgradeCount() {
            return upgradeCount;
        }
    }

    // Upgrade Class for Dropped Items
    class Upgrade {
        int numWaves = 1; // Number of waves

        // Method to increase wave count
        public void increaseWaveCount() {
            numWaves++;
        }

        // Calculate drop chance
        public double getDropChance() {
            return (1.0 / 100) * numWaves;
        }
    }

    // Bow Class
    class Bow extends Item {
        public Bow(int upgradeCount) {
            super("Bow", 40, 3, upgradeCount);
        }

        @Override
        public double damage() {
            return baseDamage + (upgradeCount * 5);
        }

        @Override
        public double attackRate() {
            return baseAttackRate + (upgradeCount * 1.5);
        }
    }

    class pewpew extends Bow { // new bow weapon
        public pewpew(int upgradeCount) {
            super(upgradeCount);
            this.name = "pewpew";
            this.baseDamage = 60;
            this.baseAttackRate = 2.5;
        }
    }

    class Bigpewpew extends Bow { // new bow weapon will change stats for the weapon later
        public Bigpewpew(int upgradeCount) {
            super(upgradeCount);
            this.name = "Bigpewpew";
            this.baseDamage = 60;
            this.baseAttackRate = 2.5;
        }
    }

    // Crossbow Class
    class Crossbow extends Item {
        public Crossbow(int upgradeCount) {
            super("Crossbow", 50, 7, upgradeCount);
        }

        @Override
        public double damage() {
            return baseDamage + (upgradeCount * 7);
        }

        @Override
        public double attackRate() {
            return baseAttackRate + (upgradeCount * 1.3);
        }
    }

    // Dagger Class
    class Dagger extends Item {
        public Dagger(int upgradeCount) {
            super("Dagger", 30, 2, upgradeCount);
        }

        @Override
        public double damage() {
            return baseDamage + (upgradeCount * 3);
        }

        @Override
        public double attackRate() {
            return baseAttackRate + (upgradeCount * 1.8);
        }
    }

    // Sword Class
    class Sword extends Item {
        public Sword(int upgradeCount) {
            super("Sword", 70, 10, upgradeCount);
        }

        @Override
        public double damage() {
            return baseDamage + (upgradeCount * 10);
        }

        @Override
        public double attackRate() {
            return baseAttackRate;
        }
    }

    // GreatSword Class
    class GreatSword extends Item {
        public GreatSword(int upgradeCount) {
            super("GreatSword", 100, 15, upgradeCount);
        }

        @Override
        public double damage() {
            return baseDamage + (upgradeCount * 15);
        }

        @Override
        public double attackRate() {
            return baseAttackRate + (upgradeCount * 1.1);
        }
    }

}


