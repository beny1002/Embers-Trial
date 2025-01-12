package com.EmbersTrial.Items;
//the base value of each Item is the beginning now just change each item value to be a variable and create new items that just implement this
// Abstract class Item
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
    private static int numWaves = 1; // Number of waves

    // Method to increase wave count
    public static void increaseWaveCount() {
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


