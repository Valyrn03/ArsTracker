package application.models;

import application.models.enums.AbilityCategory;
import application.utils.CharacterUtils;

import java.util.ArrayList;
import java.util.List;

public class Ability implements Comparable<Ability>{
    private AbilityCategory category;
    private String subtype;
    private String speciality;
    private int experience;

    private Ability(AbilityCategory category, String speciality, int experience){
        this.category = category;
        this.speciality = speciality;
        this.experience = experience;
    }

    private Ability(AbilityCategory category, String type, String speciality, int experience){
        this.category = category;
        this.subtype = type;
        this.speciality = speciality;
        this.experience = experience;
    }

    public static Ability createAbility(String abilityCategory, String type, String speciality, int experience){
        try{
            AbilityCategory category = AbilityCategory.valueOf(abilityCategory);

            return new Ability(category, type, speciality, experience);
        }catch (IllegalArgumentException exp){
            return null;
        }
    }

    public int increment(int increment){
        experience += increment;
        return experience;
    }

    public int getExperience(){
        return experience;
    }

    public String getAbility(){
        if(subtype == null){
            return category.name();
        }
        return subtype.toUpperCase();
    }

    @Override
    public int compareTo(Ability o) {
        if(this.getExperience() >= o.getExperience()){
            return 1;
        }else if(this.getExperience() <= o.getExperience()){
            return -1;
        }else{
            return this.getAbility().compareTo(o.getAbility());
        }
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();

        if(subtype == null){
            builder.append(category.name());
        }else{
            builder.append(subtype.toUpperCase()).append(" (").append(category.name()).append(")");
        }

        builder.append(" ").append(CharacterUtils.abilityExperienceToScore(experience)).append(" (").append(experience).append(")");

        return builder.toString();
    }

    @Override
    public boolean equals(Object o){
        if(!o.getClass().equals(this.getClass())){
            return false;
        }

        Ability other = (Ability) o;

        return this.getAbility().equals(other.getAbility());
    }

    public AbilityCategory getCategory() {
        return category;
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
}