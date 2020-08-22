package shopProduct.controllers;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shopProduct.interfaces.impls.DBProductDAO;
import shopProduct.objects.Product;
import shopProduct.utils.DialogManager;

import java.io.IOException;

public class MainController {

    public static final String FXML_EDIT = "../fxml/edit.fxml";

    private final DBProductDAO productTableImpl = new DBProductDAO();

    private final FXMLLoader fxmlLoader = new FXMLLoader();

    @FXML
    private Label labelSearchByTitle;
    @FXML
    private Label labelRangeFrom;
    @FXML
    private Label labelRangeTo;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtSearchRangeFrom;
    @FXML
    private Button btnClean;
    @FXML
    private TextField txtSearchRangeTo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField txtSearch;
    @FXML
    private Label labelStatus;
    @FXML
    private TableView<Product> tableProduct;
    @FXML
    private TableColumn<Product, Integer> columnId;
    @FXML
    private TableColumn<Product, Integer> columnProdid;
    @FXML
    private TableColumn<Product, String> columnTitle;
    @FXML
    private TableColumn<Product, Integer> columnPrice;

    private Stage mainStage;

    private Parent fxmlEdit;

    private EditDialogController editDialogController;

    private Stage editDialogStage;


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void actionButtonAddPressed() {
        if (actionAdd()) {
            productTableImpl.findAll();
        }
    }

    public void actionButtonEditPressed() {
        Product selectedPerson = tableProduct.getSelectionModel().getSelectedItem();
        if (!productIsSelected(selectedPerson)) {
            return;
        }

        if (actionEdit(selectedPerson)) {
            productTableImpl.findAll();
        }
    }

    public void actionButtonDeletePressed() {
        Product selectedPerson = tableProduct.getSelectionModel().getSelectedItem();
        if (!productIsSelected(selectedPerson) || !(confirmDelete())) {
            return;
        }

        if (actionDelete(selectedPerson)) {
            productTableImpl.findAll();
        }

    }

    public void actionUniversalSearch() {

        String lineSearch = txtSearch.getText().trim();
        String lineFrom = txtSearchRangeFrom.getText().trim();
        String lineTo = txtSearchRangeTo.getText().trim();

        try {
            if (lineFrom.length() != 0) Integer.parseInt(lineFrom);
            if (lineTo.length() != 0) Integer.parseInt(lineTo);
        } catch (NumberFormatException exp) {
            DialogManager.showInfoDialog("Error", "Input Error. Enter the numbers.");
            txtSearchRangeTo.clear();
            txtSearchRangeFrom.clear();
            return;
        }

        if (lineSearch.length() == 0 && (lineFrom.length() != 0 || lineTo.length() != 0)) {
            actionSearchRange(lineFrom, lineTo);
            return;
        }

        if (lineSearch.length() != 0 && lineFrom.length() == 0 && lineTo.length() == 0) {
            actionSearch(lineSearch);
            return;
        }

        if (lineSearch.length() != 0) {
            actionSearchAllCondition(lineSearch, lineFrom, lineTo);
            return;
        }

        productTableImpl.findAll();
    }

    public void actionClean() {
        txtSearch.clear();
        txtSearchRangeFrom.clear();
        txtSearchRangeTo.clear();
    }

    public void actionKeyEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) actionUniversalSearch();
    }

    private boolean actionAdd() {
        Product prod = new Product();
        editDialogController.setProduct(prod);
        showDialog();

        if (editDialogController.isSaveClicked()) {
            productTableImpl.add(prod);
            return true;
        }
        return false;
    }

    private boolean actionEdit(Product selectedPerson) {
        editDialogController.setProduct(selectedPerson);
        showDialog();

        if (editDialogController.isSaveClicked()) {
            productTableImpl.update(selectedPerson);
            return true;
        }
        return false;
    }

    private boolean actionDelete(Product selectedPerson) {
        return productTableImpl.delete(selectedPerson);
    }

    private void updateCountLabel() {
        labelStatus.setText("Status: " + productTableImpl.getProductList().size());
    }
    // Происходит связывание колонок таблицы с полями объекта Product с помощью строки, которая называет имя поля у объекта
    // Это нельзя делать в конструкторе, так как объект MainController создаётся до создания main.fxml файла
    // Иначе вылезет NPE

    @FXML
    private void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnProdid.setCellValueFactory(new PropertyValueFactory<>("prodid"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<Product, String>("title"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Product, Integer>("price"));

        initListeners();
        fillData();
        initLoader();
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource(FXML_EDIT));
            fxmlEdit = fxmlLoader.load();
            editDialogController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillData() {
        ObservableList<Product> list = productTableImpl.findAll();
        tableProduct.setItems(list);
    }

    private void initListeners() {
        // слушает изменения в коллекции для обновления надписи "Status"
        productTableImpl.getProductList().addListener((ListChangeListener<Product>) c -> updateCountLabel());

        // слушает двойное нажатие для редактирования записи
        tableProduct.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editDialogController.setProduct(tableProduct.getSelectionModel().getSelectedItem());
                showDialog();
//                btnChange.fire();
            }
        });
    }

    private boolean productIsSelected(Product selectedPerson) {
        if (selectedPerson == null) {
            DialogManager.showInfoDialog("Error", "Select person");
            return false;
        }
        return true;
    }

    private boolean confirmDelete() {
        return DialogManager.showConfirmDialog(
                "Confirm", "Are you sure you want to delete the entry?").get() == ButtonType.OK;

    }

    private void showDialog() {

        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setTitle("Editor");
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }

        editDialogStage.showAndWait();
    }

    private void actionSearch(String lineSearch) {
        productTableImpl.find(lineSearch);
    }

    private void actionSearchRange(String lineFrom, String lineTo) {

        lineFrom = (lineFrom.length() == 0) ? "0" : lineFrom;
        lineTo = (lineTo.length() == 0) ? String.valueOf(Integer.MAX_VALUE) : lineTo;

        int from = Integer.parseInt(lineFrom);
        int to = Integer.parseInt(lineTo);

        productTableImpl.findRange(from, to);
    }

    private void actionSearchAllCondition(String lineSearch, String lineFrom, String lineTo) {

        lineFrom = (lineFrom.length() == 0) ? "0" : lineFrom;
        lineTo = (lineTo.length() == 0) ? String.valueOf(Integer.MAX_VALUE) : lineTo;

        int from = Integer.parseInt(lineFrom);
        int to = Integer.parseInt(lineTo);

        productTableImpl.findAllCondition(lineSearch, from, to);
    }
}
