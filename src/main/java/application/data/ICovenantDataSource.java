package application.data;

import application.models.Book;
import application.models.Campaign;
import application.models.Covenant;
import application.models.CovenantFeature;

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
        Additions or removals from the list of characters
        Books the covenant contains
        Lab texts the covenant contains
        The amount of vis the covenant has
        The features of a covenant
     */
    boolean updateCovenant(Covenant covenant);

    boolean addCovenant(Covenant covenant, Campaign campaign);

    boolean addNewCovenantFeature(CovenantFeature feature);

    /*
    If the feature that is passed in does not already exist, also call addNewCovenantFeature
     */
    boolean addFeatureToCovenant(Covenant covenant, CovenantFeature feature);
}
