package application.data;

import application.models.*;
import application.models.enums.Art;

import java.util.List;
import java.util.Optional;

public interface ICovenantDataSource {
    Optional<Book> loadBookFromId(String bookID);

    Optional<CovenantFeature> loadCovenantFeatureFromId(int featureID);

    List<CovenantFeature> loadFeaturesFromCovenant(Covenant covenant);

    List<Book> loadBooksFromCovenant(Covenant covenant);

    Optional<Covenant> loadCovenantFromId(int covenantID);

    /*
    The id, tribunal, name, and season of establishment of a covenant will not be changeable.

    So realistically, the changes that must be written are:
        Additions or removals from the list of characters <- handled in CharacterDataSource
        Books the covenant contains <- Skipped for now
        Lab texts the covenant contains
        The amount of vis the covenant has
        The features of a covenant
     */
//    boolean updateCovenant(Covenant covenant);

    boolean updateCovenantVisStores(Covenant covenant, Art art);

    boolean addCovenant(Covenant covenant, Campaign campaign);

    boolean saveCovenantFeature(CovenantFeature feature);

    /*
    If the feature that is passed in does not already exist, also call addNewCovenantFeature
     */
    boolean addFeatureToCovenant(Covenant covenant, CovenantFeature feature);

//    boolean addBookToCovenant(Covenant covenant, Book book);

    boolean updateCovenantLabTexts(Covenant covenant);

    /*
    To be precise, loads the basic character requirements (id, name, season, type, and characteristics).

    Anything else will be handled by CharacterDataSource
     */
    List<ArsCharacter> loadCovenantCharacters(Covenant covenant);
}
