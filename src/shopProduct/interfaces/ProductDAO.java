package shopProduct.interfaces;

import javafx.collections.ObservableList;
import shopProduct.objects.Product;

public interface ProductDAO {

    boolean add(Product product);

    boolean delete(Product product);

    boolean update(Product product);

    ObservableList<Product> findAll();

    ObservableList<Product> find(String text);

}
