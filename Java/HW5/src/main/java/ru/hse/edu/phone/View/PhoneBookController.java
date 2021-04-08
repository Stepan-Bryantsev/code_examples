/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.View;

import java.util.Date;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import ru.hse.edu.phone.Models.Contact;
import ru.hse.edu.phone.ViewModels.PhoneBookViewModel;

/**
 * Окно основного окна приложения.
 */
public class PhoneBookController {

  private final PhoneBookViewModel phoneBookViewModel = new PhoneBookViewModel();

  @FXML private TableView<Contact> phoneBookTableView;
  @FXML private TableColumn<Contact, String> lastNameColumn;
  @FXML private TableColumn<Contact, String> firstNameColumn;
  @FXML private TableColumn<Contact, String> secondNameColumn;
  @FXML private TableColumn<Contact, String> mobilePhoneColumn;
  @FXML private TableColumn<Contact, String> homePhoneColumn;
  @FXML private TableColumn<Contact, String> addressColumn;
  @FXML private TableColumn<Contact, Date> birthdayColumn;
  @FXML private TableColumn<Contact, String> commentColumn;

  @FXML private Button editButton;
  @FXML private Button deleteButton;
  @FXML private TextField searchField;
  @FXML private MenuItem menuEdit;
  @FXML private MenuItem menuDelete;

  @FXML private void initialize() {

    phoneBookTableView.setItems(phoneBookViewModel.getPhoneBookList());
    phoneBookViewModel.selectedContactProperty().bind(phoneBookTableView.getSelectionModel().selectedItemProperty());
    phoneBookViewModel.searchFieldProperty().bind(searchField.textProperty());

    initializeTable();
    initializeElements();
  }

  private void initializeTable() {
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    secondNameColumn.setCellValueFactory(new PropertyValueFactory<>("secondName"));
    mobilePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("mobilePhone"));
    homePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("homePhone"));
    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
    commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
  }

  private void initializeElements() {
    editButton.disableProperty().bind(phoneBookTableView.getSelectionModel().selectedItemProperty().isNull());
    deleteButton.disableProperty().bind(phoneBookTableView.getSelectionModel().selectedItemProperty().isNull());

    menuEdit.disableProperty().bind(phoneBookTableView.getSelectionModel().selectedItemProperty().isNull());
    menuDelete.disableProperty().bind(phoneBookTableView.getSelectionModel().selectedItemProperty().isNull());

    searchField.setOnKeyPressed(key -> {
      if (key.getCode().equals(KeyCode.ENTER)) {
        onSearch();
      }
    });
  }

  @FXML private void onAddClicked() {
    phoneBookViewModel.addNewContact();
  }

  @FXML private void onEditClicked() {
    phoneBookViewModel.editContact();
  }

  @FXML private void onDeleteClicked() {
    phoneBookViewModel.deleteContact();
  }

  @FXML private void onSearch() {
    phoneBookViewModel.refreshContacts();
  }

  @FXML private void onImport() {
    phoneBookViewModel.importData();
  }

  @FXML private void onExport() {
    phoneBookViewModel.exportData();
  }

  @FXML private void onExit() {
    Platform.exit();
  }

  @FXML private void onInfo() {
    phoneBookViewModel.showInfo();
  }
}
