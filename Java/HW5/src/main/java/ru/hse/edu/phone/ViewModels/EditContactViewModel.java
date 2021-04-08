/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.ViewModels;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ru.hse.edu.phone.Models.Contact;
import ru.hse.edu.phone.Models.Contact.Builder;
import ru.hse.edu.phone.Models.ContactsHolder;
import ru.hse.edu.phone.Settings;

/**
 * ViewModel окна редактирования/добавления.
 */
public class EditContactViewModel {

  private final ContactsHolder contactsHolder;
  private final BooleanProperty disableAdding = new SimpleBooleanProperty(true);;
  private final Contact.Builder contactBuilder;

  private final SimpleStringProperty lastName = new SimpleStringProperty();
  private final SimpleStringProperty firstName = new SimpleStringProperty();
  private final SimpleStringProperty secondName = new SimpleStringProperty();
  private final SimpleStringProperty mobilePhone = new SimpleStringProperty();
  private final SimpleStringProperty homePhone = new SimpleStringProperty();
  private final SimpleStringProperty address = new SimpleStringProperty();
  private final SimpleObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();
  private final SimpleStringProperty comment = new SimpleStringProperty();

  private final Contact oldContact;

  public EditContactViewModel(ContactsHolder contactsHolder, Builder contactBuilder, Contact oldContact) {
    this.contactsHolder = contactsHolder;
    this.contactBuilder = contactBuilder;
    this.oldContact = oldContact;

    lastName.addListener((observable, oldValue, newValue) -> { this.contactBuilder.setLastName(newValue); checkValidation();});
    firstName.addListener((observable, oldValue, newValue) -> { this.contactBuilder.setFirstName(newValue); checkValidation();});
    secondName.addListener((observable, oldValue, newValue) -> { this.contactBuilder.setSecondName(newValue); checkValidation();});
    mobilePhone.addListener((observable, oldValue, newValue) -> {this.contactBuilder.setMobilePhone(newValue); checkValidation();});
    homePhone.addListener((observable, oldValue, newValue) -> {this.contactBuilder.setHomePhone(newValue); checkValidation();});
    address.addListener((observable, oldValue, newValue) -> {this.contactBuilder.setAddress(newValue); checkValidation();});
    birthday.addListener((observable, oldValue, newValue) -> {this.contactBuilder.setBirthday(newValue); checkValidation();});
    comment.addListener((observable, oldValue, newValue) -> {this.contactBuilder.setComment(newValue); checkValidation();});
  }

  public void fillValues() {
    if (oldContact != null) {
      lastName.set(oldContact.getLastName());
      firstName.set(oldContact.getFirstName());
      secondName.set(oldContact.getSecondName());
      mobilePhone.set(oldContact.getMobilePhone());
      homePhone.set(oldContact.getHomePhone());
      address.set(oldContact.getAddress());
      birthday.set(oldContact.getBirthday());
      comment.set(oldContact.getComment());
    }
  }

  private void checkValidation() {
    disableAdding.setValue(!contactBuilder.validate());
  }

  public BooleanProperty disableAddingProperty() {
    return disableAdding;
  }

  /**
   * Метод добавления или редактирования контакта в модели.
   * @return успешность редактирования/удаления.
   */
  public boolean confirmEdit() {
    try {
      if (oldContact == null)
        contactsHolder.addContact(contactBuilder);
      else
        contactsHolder.editContact(oldContact, contactBuilder);
    }
    catch (IllegalArgumentException ex) {
      messageAlert(ex.getMessage());
      return false;
    }
    return true;
  }

  public SimpleStringProperty lastNameProperty() {
    return lastName;
  }

  public SimpleStringProperty firstNameProperty() {
    return firstName;
  }

  public SimpleStringProperty secondNameProperty() {
    return secondName;
  }

  public SimpleStringProperty mobilePhoneProperty() {
    return mobilePhone;
  }

  public SimpleStringProperty homePhoneProperty() {
    return homePhone;
  }

  public SimpleStringProperty addressProperty() {
    return address;
  }

  public SimpleObjectProperty<LocalDate> birthdayProperty() {
    return birthday;
  }

  public SimpleStringProperty commentProperty() {
    return comment;
  }

  public Builder getContactBuilder() {
    return contactBuilder;
  }

  private void messageAlert(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Ошибка!");

    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.getDialogPane().getStylesheets().add(getClass().getResource(Settings.GENERAL_STYLE).toExternalForm());

    alert.showAndWait();
  }
}
