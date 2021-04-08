/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.ViewModels;

import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.hse.edu.phone.Models.Contact;
import ru.hse.edu.phone.Models.Contact.Builder;
import ru.hse.edu.phone.Models.ContactsHolder;
import ru.hse.edu.phone.Models.ContactsLoadException;
import ru.hse.edu.phone.Settings;
import ru.hse.edu.phone.View.EditContactController;
import ru.hse.edu.phone.View.LoadDataController;

public class PhoneBookViewModel {

  private final ContactsHolder contactsHolder;

  private final ObservableList<Contact> phoneBookList;
  private final ObjectProperty<Contact> selectedContact = new SimpleObjectProperty<>();;
  private final SimpleStringProperty searchField = new SimpleStringProperty();

  public PhoneBookViewModel() {
    contactsHolder = new ContactsHolder();
    try {
      contactsHolder.loadData();
    }
    catch (ContactsLoadException ex) {
      System.out.println(ex.getMessage());
    }
    phoneBookList = FXCollections.observableArrayList(contactsHolder.getContacts());
    searchField.addListener(observable -> {
      if (searchField.getValue() == null || searchField.getValue().isEmpty()) {
        refreshContacts();
      }
    });
  }

  public ObjectProperty<Contact> selectedContactProperty() {
    return selectedContact;
  }

  public ObservableList<Contact> getPhoneBookList() {
    return phoneBookList;
  }

  public SimpleStringProperty searchFieldProperty() {
    return searchField;
  }

  public void addNewContact() {
    createEditWindow(true);
  }

  public void editContact() {
    if (selectedContact.get() != null) {
      createEditWindow(false);
    }
  }

  public void deleteContact() {
    if (selectedContact.get() != null) {
      contactsHolder.deleteContact(selectedContact.get());
    }
    refreshContacts();
  }

  public void importData() {
    createLoadDataWindow(false);
  }

  public void exportData() {
    createLoadDataWindow(true);
  }

  public void showInfo() {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Справка:");

      alert.setHeaderText(null);
      alert.setContentText(Settings.APP_INFO);
      alert.getDialogPane().getStylesheets().add(getClass().getResource(Settings.GENERAL_STYLE).toExternalForm());

      alert.showAndWait();
  }

  public void refreshContacts() {
    if (searchField.get() == null || searchField.get().isEmpty()) {
      phoneBookList.setAll(contactsHolder.getContacts());
    }
    else {
      phoneBookList.setAll(contactsHolder.getContacts(this::searchPredicate));
    }
  }

  /**
   * Предикат поиска по контактам.
   * @param contact
   * @return
   */
  private boolean searchPredicate(Contact contact) {
    return contact.getFirstName().contains(searchField.get()) ||
        contact.getLastName().contains(searchField.get()) ||
        (contact.getSecondName() != null && contact.getSecondName().contains(searchField.get()));
  }

  /**
   * Создание окна редактирования/добавления контакта.
   * @param createNew редактирование или добавление.
   */
  private void createEditWindow(boolean createNew) {
    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource(Settings.EDIT_WINDOW_FXML));

    try {
      stage.setScene(new Scene(loader.load()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    EditContactController editController = loader.getController();
    if (createNew)
      editController
          .setEditContactViewModel(new EditContactViewModel(contactsHolder, new Builder(), null));
    else {
      editController.setEditContactViewModel(
          new EditContactViewModel(contactsHolder, new Builder(selectedContact.get()),
              selectedContact.get()));
    }

    stage.setOnCloseRequest(event -> refreshContacts());

    stage.setTitle(createNew ? "Создание контакта" : "Редактирование контакта");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.show();
  }

  /**
   * Создание окна загрузки данных.
   * @param isExport
   */
  private void createLoadDataWindow(boolean isExport) {
    Stage stage = new Stage();
    FXMLLoader loader = new FXMLLoader(getClass().getResource(Settings.LOAD_DATA_WINDOW_FXML));

    try {
      stage.setScene(new Scene(loader.load()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    ((LoadDataController)loader.getController()).setLoadDataViewModel(new LoadDataViewModel(contactsHolder, isExport));

    stage.setOnCloseRequest(event -> refreshContacts());

    stage.setTitle(isExport ? "Експорт данных" : "Импорт данных");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.show();
  }
}
