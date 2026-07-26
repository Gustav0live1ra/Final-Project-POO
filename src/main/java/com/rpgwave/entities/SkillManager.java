package com.rpgwave.entities;

public class SkillManager {

    public static boolean canUseSkill(Character character, Skill skill) {

        if (character == null || skill == null) {
            return false;
        }

        Stats stats = character.getStats();

        return stats.getCurrentMana() >= skill.getManaCost();
    }

    public static boolean useSkill(Character character, Skill skill) {

        if (!canUseSkill(character, skill)) {
            return false;
        }

        Stats stats = character.getStats();

        return stats.useMana(skill.getManaCost());
    }
    public static int useSkillAndCalculateDamage(
            Character character,
            Skill skill,
            int enemyDefense) {

        if (!useSkill(character, skill)) {
            return 0;
        }

        return DamageCalculator.calculateDamage(
                character.getStats().getAttack(),
                skill,
                enemyDefense
        );
    }
}