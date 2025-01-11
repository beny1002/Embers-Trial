package com.EmbersTrial.Items;

//should make it so that there is a chance for each item to drop has a certain rareity its like theres a chance each time a item drops
//could make it so that there is no different armors and make it so that they increase a certain set you have depending on upgrades
// Interface for Armor
interface Armor {
    double Defense(double baseDefense); // Calculate defense
    double HpUp(double baseHpUp);       // Calculate HPUp
}

// Armor class that calculates values based on upgrades
public class ArmourValues implements Armor {
    private int upgradeCount; // Number of upgrades applied

    // Constructor to set the number of upgrades
    public ArmourValues(int upgradeCount) {
        this.upgradeCount = upgradeCount;
    }

    // Calculate Defense value
    @Override
    public double Defense(double baseDefense) {
        return baseDefense * (upgradeCount); // Equation as provided
    }

    // Calculate HPUp value
    @Override
    public double HpUp(double baseHpUp) {
        return baseHpUp * (upgradeCount); // Equation as provided
    }

    // Getter for upgrades (optional, for debugging or future use)
    public int getUpgradeCount() {
        return upgradeCount;
    }
}

interface Weapons () {
    double damage();
    double AttackRate();
}

//make it so that in the beginning of the game they decide what weapon they use this is simpler
// make it so that each damage equation is similar to equation values above for each
// Bow class

class Bow extends Item {
    public Bow(int upgradeCount) {
        super("Bow", 40, 3, upgradeCount);
    }

    @Override
    public double damage() {
        return baseDamage + (upgradeCount * 5); // Damage increases by 5 per upgrade
    }

    @Override
    public double attackRate() {
        return baseAttackRate + (upgradeCount * 0.2); // Attack rate increases slightly per upgrade
    }
}


class Crossbow extends Item {
    public Crossbow(int upgradeCount) {
        super("Crossbow", 50, 7, upgradeCount);
    }

    @Override
    public double damage() {
        return baseDamage + (upgradeCount * 7); // Damage increases by 7 per upgrade
    }

    @Override
    public double attackRate() {
        return baseAttackRate - (upgradeCount * 0.1); // Attack rate slightly decreases per upgrade
    }
}


class Dagger extends Item {
    public Dagger(int upgradeCount) {
        super("Dagger", 30, 2, upgradeCount);
    }

    @Override
    public double damage() {
        return baseDamage + (upgradeCount * 3); // Damage increases by 3 per upgrade
    }

    @Override
    public double attackRate() {
        return baseAttackRate + (upgradeCount * 0.5); // Attack rate increases significantly per upgrade
    }
}


class Sword extends Item {
    public Sword(int upgradeCount) {
        super("Sword", 70, 10, upgradeCount);
    }

    @Override
    public double damage() {
        return baseDamage + (upgradeCount * 10); // Damage increases by 10 per upgrade
    }

    @Override
    public double attackRate() {
        return baseAttackRate; // Attack rate remains constant
    }
}


class GreatSword extends Item {
    public GreatSword(int upgradeCount) {
        super("GreatSword", 100, 15, upgradeCount);
    }

    @Override
    public double damage() {
        return baseDamage + (upgradeCount * 15); // Damage increases by 15 per upgrade
    }

    @Override
    public double attackRate() {
        return baseAttackRate - (upgradeCount * 0.3); // Attack rate decreases per upgrade
    }
}

