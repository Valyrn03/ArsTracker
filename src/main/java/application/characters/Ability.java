package application.characters;

import application.utils.CharacterUtils;

public class Ability implements Comparable<Ability>{
    private int category;
    private String subtype;
    private String speciality;
    private int experience;

    private Ability(int category, String speciality, int experience){
        this.category = category;
        this.speciality = speciality;
        this.experience = experience;
    }

    public Ability(int category, String type, String speciality, int experience){
        this.category = category;
        this.subtype = type;
        this.speciality = speciality;
        this.experience = experience;
    }

    public int increment(int increment){
        experience += increment;
        return experience;
    }

    public int getExperience(){
        return experience;
    }

    //TODO
    public String getAbility(){
        return "";
    }

    public int getCategory() {
        return category;
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
            builder.append(getCategory());
        }else{
            builder.append(subtype.toUpperCase()).append(" (").append(getCategory()).append(")");
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