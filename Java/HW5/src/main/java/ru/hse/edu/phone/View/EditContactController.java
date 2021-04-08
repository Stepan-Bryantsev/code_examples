/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.View;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.hse.edu.phone.Models.Contact.Builder;
import ru.hse.edu.phone.Models.ValidationEvent;
import ru.hse.edu.phone.Models.ValidationEvent.ValidationResult;
import ru.hse.edu.phone.ViewModels.EditContactViewModel;

/**
 * Класс котроллера для окна добавления или редактиования контакта.
 */
public class EditContactController {

  private EditContactViewModel editContactViewModel;

  @FXML private TextField lastNameField;
  @FXML private TextField firstNameField;
  @FXML private TextField secondNameField;
  @FXML private TextField mobilePhoneField;
  @FXML private TextField homePhoneField;
  @FXML private TextField addressField;
  @FXML private DatePicker birthdayField;
  @FXML private TextField commentField;

  @FXML private Label lastNameMessageField;
  @FXML private Label firstNameMessageField;
  @FXML private Label secondNameMessageField;
  @FXML private Label mobilePhoneMessageField;
  @FXML private Label homePhoneMessageField;
  @FXML private Label addressMessageField;
  @FXML private Label birthdayMessageField;
  @FXML private Label commentMessageField;

  @FXML private Button cancelButton;
  @FXML private Button okButton;

  @FXML private Button cleanBirthday;

  @FXML
  private void initialize() {
  }

  /**
   * Инициализация ViewModel.
   * @param editContactViewModel ViewModel для данного окна.
   */
  public void setEditContactViewModel(EditContactViewModel editContactViewModel) {
    this.editContactViewModel = editContactViewModel;
    initializeElements();
    initializeValidations();
    editContactViewModel.fillValues();
  }

  private void initializeElements() {
    okButton.disableProperty().bind(editContactViewModel.disableAddingProperty());
    cleanBirthday.setOnMouseClicked(event -> birthdayField.setValue(null));
    okButton.setOnMouseClicked(event -> {
      if (editContactViewModel.confirmEdit()) {
        closeWindow(event);
      }
    });

    cancelButton.setOnMouseClicked(this::closeWindow);

    editContactViewModel.lastNameProperty().bindBidirectional(lastNameField.textProperty());
    editContactViewModel.firstNameProperty().bindBidirectional(firstNameField.textProperty());
    editContactViewModel.secondNameProperty().bindBidirectional(secondNameField.textProperty());
    editContactViewModel.mobilePhoneProperty().bindBidirectional(mobilePhoneField.textProperty());
    editContactViewModel.homePhoneProperty().bindBidirectional(homePhoneField.textProperty());
    editContactViewModel.addressProperty().bindBidirectional(addressField.textProperty());
    editContactViewModel.birthdayProperty().bindBidirectional(birthdayField.valueProperty());
    editContactViewModel.commentProperty().bindBidirectional(commentField.textProperty());
  }

  private void initializeValidations() {
    Builder builder = editContactViewModel.getContactBuilder();

    builder.addLastNameValidationListener(event -> validationEffect(event, lastNameField, lastNameMessageField));
    builder.addFirstNameValidationListener(event -> validationEffect(event, firstNameField, firstNameMessageField));
    builder.addSecondNameValidationListener(event -> validationEffect(event, secondNameField, secondNameMessageField));
    builder.addMobilePhoneValidationListener(event -> validationEffect(event, mobilePhoneField, mobilePhoneMessageField));
    builder.addHomePhoneValidationListener(event -> validationEffect(event, homePhoneField, homePhoneMessageField));
    builder.addAddressValidationListener(event -> validationEffect(event, addressField, addressMessageField));
    builder.addBirthdayValidationListener(event -> validationEffect(event, birthdayField.getEditor(), birthdayMessageField));
    builder.addCommentValidationListener(event -> validationEffect(event, commentField, commentMessageField));
  }

  private void validationEffect(ValidationEvent event, TextField textField, Label messageLabel) {
    messageLabel.setText(event.getValidationMessage());
    if (event.getValidationResult() == ValidationResult.CORRECT) {
      textField.setStyle("-fx-border-color: green;");
      messageLabel.setStyle("-fx-text-fill: green");
    }
    else if (event.getValidationResult() == ValidationResult.WARNING) {
      textField.setStyle("-fx-border-color: #CC9514;");
      messageLabel.setStyle("-fx-text-fill: #CC9514");
    }
    else if (event.getValidationResult() == ValidationResult.ERROR) {
      textField.setStyle("-fx-border-color: red;");
      messageLabel.setStyle("-fx-text-fill: red");
    }
  }

  private void closeWindow(InputEvent event) {
    final Node source = (Node) event.getSource();
    final Stage stage = (Stage) source.getScene().getWindow();
    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
  }
}
