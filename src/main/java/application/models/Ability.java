package application.models;

import application.models.enums.AbilityCategory;
import application.utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode
public class Ability implements Comparable<Ability>{
    @Getter private AbilityCategory category;
    @Setter private Optional<String> subtype;
    @Getter private String speciality;
    @Setter @Getter private int experience;

    public Ability(AbilityCategory category, String type, String speciality, int experience){
        this.category = category;
        this.speciality = speciality;
        this.experience = experience;

        if(isAbilityCategorical(category)){
            this.subtype = Optional.of(type);
        }else{
            this.subtype = Optional.empty();
        }
    }

    public int increment(int increment){
        experience += increment;
        return experience;
    }

    public String getAbility(){
        return subtype.orElseGet(() -> category.toString());
    }

    @Override
    public int compareTo(Ability o) {
        if(this.getExperience() > o.getExperience()){
            return 1;
        }else if(this.getExperience() < o.getExperience()){
            return -1;
        }else{
            return this.getAbility().compareTo(o.getAbility());
        }
    }

    @Override
    public String toString(){
        return getAbility() + " (" + getSpeciality() + ") lvl" + utils.abilityExperienceToScore(getExperience());
    }

    public static List<String> generalAbilities(){
        List<String> list = new ArrayList<>();

        list.add("Athletics");
        list.add("Animal Handling");
        list.add("(Area) Lore");
        list.add("Awareness");
        list.add("Bargain");
        list.add("Brawl");
        list.add("Carouse");
        list.add("Charm");
        list.add("Chirurgy");
        list.add("Concentration");
        list.add("Craft (Type)");
        list.add("Etiquette");
        list.add("Folk Ken");
        list.add("Guile");
        list.add("Hunt");
        list.add("Intrigue");
        list.add("Judaic Lore");
        list.add("Leadership");
        list.add("Legerdemain");
        list.add("(Living Language)");
        list.add("Music");
        list.add("(Mystery Cult) Lore");
        list.add("(Organization) Lore");
        list.add("Profession (Type)");
        list.add("Ride");
        list.add("Stealth");
        list.add("Survival");
        list.add("Swim");
        list.add("Teaching");

        return list;
    }

    public static List<String> academicAbilities(){
        List<String> list = new ArrayList<>();

        list.add("Medicine");
        list.add("Art of Memory");
        list.add("Artes Liberales");
        list.add("Civil and Canon Law");
        list.add("Common Law");
        list.add("(Dead Language)");
        list.add("Islamic Law");
        list.add("Philosophiae");
        list.add("Rabbinic Law");
        list.add("Theology: Christian");
        list.add("Theology: Islam");
        list.add("Theology: Judaism");

        return list;
    }

    public static List<String> arcaneAbilities(){
        List<String> list = new ArrayList<>();

        list.add("Penetration");
        list.add("Code of Hermes");
        list.add("Dominion Lore");
        list.add("Enigmatic Wisdom");
        list.add("Faerie Lore");
        list.add("Faerie Magic");
        list.add("Finesse");
        list.add("Heartbeast");
        list.add("Infernal Lore");
        list.add("Magic Lore");
        list.add("Magic Theory");
        list.add("Parma Magica");

        return list;
    }

    public static List<String> supernaturalAbilities(){
        List<String> list = new ArrayList<>();

        list.add("Dowsing");
        list.add("Animal Ken");
        list.add("Corpse Magic");
        list.add("Crafters Healing");
        list.add("Curse-Throwing");
        list.add("Embitterment");
        list.add("Enchanting (Ability)");
        list.add("Entrancement");
        list.add("Font of Knowledge");
        list.add("Hex");
        list.add("Induction");
        list.add("Magic Sensitivity");
        list.add("Persona");
        list.add("Premonitions");
        list.add("Second Sight");
        list.add("Sense Holiness and Unholiness");
        list.add("Sense Passions");
        list.add("Shapeshifter");
        list.add("Summon Animals");
        list.add("Whistle Up The Wind");
        list.add("Wilderness Sense");

        return list;
    }

    public static boolean isAbilityCategorical(AbilityCategory category) {
        switch (category){
            case AREA_LORE -> {return true;}
            case CRAFT -> {return true;}
            case LIVING_LANGUAGE -> {return true;}
            case MYSTERY_CULT_LORE -> {return true;}
            case ORGANIZATION_LORE -> {return true;}
            case PROFESSION -> {return true;}
            case DEAD_LANGUAGE -> {return true;}
            case ENCHANTING -> {return true;}
            default -> {return false;}
        }
    }
}