package application.models;

import lombok.Getter;

import java.util.Optional;

public class LabText {
    private Spell spell;
    private EnchantedItem item;
    @Getter private TextType type;

    public enum TextType{
        SPELL,
        ITEM
    }

    public LabText(Spell spell){
        this.spell = spell;
        this.item = null;
        this.type = TextType.SPELL;
    }

    public LabText(EnchantedItem item){
        this.item = item;
        this.spell = null;
        this.type = TextType.ITEM;
    }

    public void getText(){

    }
}
