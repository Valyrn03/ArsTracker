package application.characters;

import application.utils.CharacterUtils;

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
}