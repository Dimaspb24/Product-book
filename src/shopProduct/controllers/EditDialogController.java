package shopProduct.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shopProduct.objects.Product;
import shopProduct.utils.DialogManager;

public class EditDialogController {

    @FXML
    private Label labelProdid;
    @FXML
    private TextField txtProdid;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelPrice;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtPrice;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnCancel;


    private Product product;

    private boolean saveClicked = false;

    public void setProduct(Product product) {
        if (product == null) {
            return;
        }

        saveClicked = false;
        this.product = product;

        txtProdid.setText(String.valueOf(product.getProdid()));
        txtTitle.setText(product.getTitle());
        txtPrice.setText(String.valueOf(product.getPrice()));
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    public void actionSave(ActionEvent actionEvent) {
        if (!checkValues()) {
            return;
        }

        product.setProdid(Integer.parseInt(txtProdid.getText()));
        product.setTitle(txtTitle.getText());
        product.setPrice(Integer.parseInt(txtPrice.getText()));

        saveClicked = true;
        actionClose(actionEvent);
    }

    private boolean checkValues() {
        String lineProdid = txtProdid.getText().trim();
        String linePrice = txtPrice.getText().trim();
        String lineTitle = txtTitle.getText().trim();

        if (lineTitle.length() == 0 || linePrice.length() == 0 || lineProdid.length() == 0) {
            DialogManager.showInfoDialog("Error", "Fill field");
            return false;
        }

        try {
            Integer.parseInt(lineProdid);
            Integer.parseInt(linePrice);
            return true;
        } catch (NumberFormatException exp) {
            DialogManager.showInfoDialog("Error", "Input Error. Enter the  int numbers.");
            txtProdid.clear();
            txtPrice.clear();
            return false;
        }
    }

}
