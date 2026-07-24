package application.models;

import lombok.Getter;
import lombok.ToString;

/*
Combined class for summae and tractus
 */
@ToString
public class Book {
    @Getter private int level;
    @Getter private int quality;
    @Getter private BookCategory category;

    public enum BookCategory{
        SUMMAE,
        TRACTUS
    }
}
