package application.data;

import application.models.Book;
import application.models.Covenant;
import application.models.CovenantFeature;

import java.util.List;
import java.util.Optional;

public interface ICovenantDataSource {
    Optional<Book> loadBookFromId(String bookID);

    Optional<CovenantFeature> loadCovenantFeatureFromId(String featureID);

    List<CovenantFeature> loadFeaturesFromCovenant(Covenant covenant);

    List<Book> loadBooksFromCovenant(Covenant covenant);

    Optional<Covenant> loadCovenantFromId(String covenantID);

    boolean updateCovenant(Covenant covenant);

    boolean addCovenant(Covenant covenant);
}
