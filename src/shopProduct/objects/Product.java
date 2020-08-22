package shopProduct.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleIntegerProperty prodid = new SimpleIntegerProperty();
    private SimpleStringProperty title = new SimpleStringProperty("");
    private SimpleIntegerProperty price = new SimpleIntegerProperty();

    public Product() {
    }

    public Product(String title, int price) {
        this.title = new SimpleStringProperty(title);
        this.price.set(price);
    }

    public Product(int prodid, String title, int price) {
        this(title, price);
        this.prodid.set(prodid);
    }

    public Product(int id, int prodid, String title, int price) {
        this(prodid, title, price);
        this.id.set(id);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public int getProdid() {
        return prodid.get();
    }

    public void setProdid(int prodid) {
        this.prodid.set(prodid);
    }

    public SimpleIntegerProperty prodidProperty() {
        return prodid;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public int getPrice() {
        return price.get();
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public SimpleIntegerProperty priceProperty() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", prodid=" + prodid +
                ", title='" + title + '\'' +
                ", cost=" + price +
                '}';
    }
}
