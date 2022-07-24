package com.pterapan.demosql.controller;

import com.pterapan.demosql.dao.CategoryDao;
import com.pterapan.demosql.dao.ItemsDao;
import com.pterapan.demosql.model.Category;
import com.pterapan.demosql.model.Items;
import com.pterapan.demosql.HelloApplication;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ItemsController implements Initializable {
    public MenuItem labelMenu;
    public Label labelId;
    public TextField txtIdItems;
    public Label labelName;
    public TextField txtNameItems;
    public Label labelPrice;
    public TextField txtPrice;
    public Label labelDescription;
    public TextArea txtDescription;
    public ComboBox<Category> cmb1;
    public TableView<Items> tableItems;
    public TableColumn<Items, Integer> colId;
    public TableColumn<Items, String> colName;
    public TableColumn<Items, Double> colPrice;
    public TableColumn<Items, Category> colCategory;
    public Button btnUpdate;

    private ObservableList<Items> ilist;
    private ObservableList<Category> clist;
    private ItemsDao itemsDao;
    private CategoryDao categoryDao;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        itemsDao = new ItemsDao();
        categoryDao = new CategoryDao();
        ilist = FXCollections.observableArrayList();
        clist = FXCollections.observableArrayList();

        ilist.addAll(itemsDao.getData());
        clist.addAll(categoryDao.getData());

        cmb1.setItems(clist);
        tableItems.setItems(ilist);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCategory()));
    }

    public void goToCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader;
        loader = new FXMLLoader(HelloApplication.class.getResource("second_category.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        CategoryController cController = loader.getController();
        cController.setItemsController(this);
        stage.setTitle("Category Management");
        stage.setScene(scene);
        stage.showAndWait();

//        KeyCombination kc = new KeyCodeCombination(KeyCode.F2, KeyCombination.ALT_DOWN);
//        Runnable rn = ()-> System.out.println("Accelerator key worked");
//        scene.getAccelerators().put(kc, rn);
        labelMenu.setAccelerator(new KeyCodeCombination(KeyCode.F2, KeyCombination.ALT_DOWN));
    }

    public void addItems(ActionEvent actionEvent) {
        ItemsDao dao = new ItemsDao();
        String id = txtIdItems.getText();
        String name = txtNameItems.getText();
        String price = txtPrice.getText();
        String description = txtDescription.getText();
        Category category = cmb1.getValue();

        if (id != "" && name != "" && price != "" && description != "") {
            dao.addData(new Items(Integer.valueOf(id), name, Double.valueOf(price), description, category));
            ilist = dao.getData();
            tableItems.setItems(ilist);

            txtIdItems.clear();
            txtNameItems.clear();
            txtDescription.clear();
            txtPrice.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void resetItems(ActionEvent actionEvent) {
        txtIdItems.clear();
        txtNameItems.clear();
        txtPrice.clear();
        txtDescription.clear();
    }

    public ObservableList<Category> getClist() {
        return clist;
    }
}
