package application.models;

import lombok.Getter;

/*
Combined class for summae and tractus
 */
public class Book {
    @Getter private int level;
    @Getter private int quality;
    @Getter private BookCategory category;

    public enum BookCategory{
        SUMMAE,
        TRACTUS
    }
}
