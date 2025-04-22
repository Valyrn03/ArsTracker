package application.utils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class Abilities {

    public enum Ability{
        ANIMAL_HANDLING,
        ANIMAL_KEN,
        AREA_LORE,
        ART_OF_MEMORY,
        ARTES_LIBERALES,
        ATHLETICS,
        AWARENESS,
        BARGAIN,
        BRAWL,
        BOWS,
        CAROUSE,
        CHARM,
        CHIRURGY,
        CIVIL_AND_COMMON_LAW,
        CODE_OF_HERMES,
        CONCENTRATION,
        CORPSE_MAGIC,
        COMMON_LAW,
        CRAFT,
        CRAFTERS_HEALING,
        CURSE_THROWING,
        DEAD_LANGUAGE,
        DOMINION_LORE,
        DOWSING,
        EMBITTERMENT,
        ENCHANTING,
        ENTRANCEMENT,
        ENIGMATIC_WISDOM,
        ETIQUETTE,
        FAERIE_LORE,
        FAERIE_MAGIC,
        FINESSE,
        FOLK_KEN,
        FONT_OF_KNOWLEDGE,
        GREAT_WEAPON,
        GUILE,
        HEARTBEAST,
        HEX,
        HUNT,
        INDUCTION,
        INFERNAL_LORE,
        INTRIGUE,
        ISLAMIC_LAW,
        JUDAIC_LORE,
        LEADERSHIP,
        LEGERDEMAIN,
        LIVING_LANGUAGE,
        MAGIC_LORE,
        MAGIC_THEORY,
        MAGIC_SENSITIVITY,
        MEDICINE,
        MUSIC,
        MYSTERY_CULT_LORE,
        ORGANIZATION_LORE,
        PARMA_MAGICA,
        PENETRATION,
        PERSONA,
        PHILOSOPHIAE,
        PREMONITIONS,
        PROFESSION,
        RABBINIC_LAW,
        RIDE,
        SINGLE_WEAPON,
        SECOND_SIGHT,
        SENSE_HOLINESS_AND_UNHOLINESS,
        SENSE_PASSIONS,
        SHAPESHIFTER,
        SUMMON_ANIMALS,
        STEALTH,
        SURVIVAL,
        SWIM,
        TEACHING,
        THROWN_WEAPON,
        THEOLOGY_CHRISTIAN,
        THEOLOGY_ISLAM,
        THEOLOGY_JUDAISM,
        WHISTLE_UP_THE_WIND,
        WILDERNESS_SENSE
    }
    public enum AbilityCategory{
        CATEGORICAL,
        ACADEMIC,
        ARCANE,
        MARTIAL,
        SUPERNATURAL
    }

    public static EnumMap<Ability, ArrayList<AbilityCategory>> abilityCategories;

    public static void initalizeMap(){
        abilityCategories = new EnumMap<Ability, ArrayList<AbilityCategory>>(Ability.class);
        Ability[] categorical = new Ability[]{Ability.AREA_LORE, Ability.CRAFT, Ability.LIVING_LANGUAGE, Ability.MYSTERY_CULT_LORE, Ability.ORGANIZATION_LORE, Ability.PROFESSION, Ability.DEAD_LANGUAGE, Ability.ENCHANTING};
        Ability[] academic = new Ability[]{Ability.ART_OF_MEMORY, Ability.ARTES_LIBERALES, Ability.CIVIL_AND_COMMON_LAW, Ability.COMMON_LAW, Ability.DEAD_LANGUAGE, Ability.ISLAMIC_LAW, Ability.MEDICINE, Ability.PHILOSOPHIAE, Ability.RABBINIC_LAW, Ability.THEOLOGY_ISLAM, Ability.THEOLOGY_CHRISTIAN, Ability.THEOLOGY_JUDAISM};
        Ability[] arcane = new Ability[]{Ability.CODE_OF_HERMES, Ability.DOMINION_LORE, Ability.ENIGMATIC_WISDOM, Ability.FAERIE_LORE, Ability.FAERIE_MAGIC, Ability.FINESSE, Ability.HEARTBEAST, Ability.INFERNAL_LORE, Ability.MAGIC_LORE, Ability.MAGIC_THEORY, Ability.PARMA_MAGICA, Ability.PENETRATION};
        Ability[] martial = new Ability[]{Ability.BOWS, Ability.GREAT_WEAPON, Ability.SINGLE_WEAPON, Ability.THROWN_WEAPON};
        Ability[] supernatural = new Ability[]{Ability.ANIMAL_KEN, Ability.CORPSE_MAGIC, Ability.CRAFTERS_HEALING, Ability.CURSE_THROWING, Ability.DOWSING, Ability.EMBITTERMENT, Ability.ENCHANTING, Ability.ENTRANCEMENT, Ability.FONT_OF_KNOWLEDGE, Ability.HEX, Ability.INDUCTION, Ability.MAGIC_SENSITIVITY, Ability.PERSONA, Ability.PREMONITIONS, Ability.SECOND_SIGHT, Ability.PREMONITIONS, Ability.SENSE_HOLINESS_AND_UNHOLINESS, Ability.SENSE_PASSIONS, Ability.SHAPESHIFTER, Ability.SUMMON_ANIMALS, Ability.WHISTLE_UP_THE_WIND, Ability.WILDERNESS_SENSE};

        for(Ability ability : categorical){
            abilityCategories.put(ability, new ArrayList<>(List.of(new AbilityCategory[]{AbilityCategory.CATEGORICAL})));
        }

        for(Ability ability : academic){
            if(abilityCategories.containsKey(ability)){
                abilityCategories.put(ability, new ArrayList<>());
            }
            abilityCategories.get(ability).add(AbilityCategory.ACADEMIC);
        }

        for(Ability ability : arcane){
            if(abilityCategories.containsKey(ability)){
                abilityCategories.put(ability, new ArrayList<>());
            }
            abilityCategories.get(ability).add(AbilityCategory.ARCANE);
        }

        for(Ability ability : martial){
            if(abilityCategories.containsKey(ability)){
                abilityCategories.put(ability, new ArrayList<>());
            }
            abilityCategories.get(ability).add(AbilityCategory.MARTIAL);
        }

        for(Ability ability : supernatural){
            if(abilityCategories.containsKey(ability)){
                abilityCategories.put(ability, new ArrayList<>());
            }
            abilityCategories.get(ability).add(AbilityCategory.SUPERNATURAL);
        }
    }
}
