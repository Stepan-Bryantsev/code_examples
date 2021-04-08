/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.ViewModels;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;
import ru.hse.edu.phone.Models.Contact;
import ru.hse.edu.phone.Models.ContactsHolder;
import ru.hse.edu.phone.Models.ContactsLoadException;
import ru.hse.edu.phone.Settings;

public class LoadDataViewModel {

  private final SimpleBooleanProperty isExport;
  private final SimpleStringProperty path = new SimpleStringProperty();
  private final SimpleStringProperty fileName = new SimpleStringProperty();
  private final ContactsHolder contactsHolder;

  public LoadDataViewModel(ContactsHolder contactsHolder, boolean isExport) {
    this.contactsHolder = contactsHolder;
    this.isExport = new SimpleBooleanProperty(isExport);
  }

  public void browse() {
    if (isExport.getValue()) {
      exportData();
    } else {
      importData();
    }
  }

  public boolean confirm() {
    if (path.getValue() == null || path.getValue().isEmpty()) {
      messageAlert("Ошибка валидации", "Укажите путь.", AlertType.ERROR);
      return false;
    }
    if (isExport.getValue()) {
      if (fileName.getValue() == null || fileName.getValue().isEmpty()) {
        messageAlert("Ошбка валидации", "Укажите имя файла.", AlertType.ERROR);
        return false;
      }
      try {
        if (!contactsHolder.exportData(
            new File(path.getValue(), fileName.getValue() + Settings.EXTENSION).getPath(), false)) {
          if (agreeAlert("Файл уже существует", "Переписать файл с данными?")) {
            contactsHolder.exportData(
                new File(path.getValue(), fileName.getValue() + Settings.EXTENSION).getPath(),
                true);
          } else {
            return false;
          }
        }
      } catch (ContactsLoadException ex) {
        messageAlert(ex.getExceptionHeader(), ex.getMessage(), AlertType.ERROR);
        return false;
      }
    } else {
      try {
        Pair<Collection<Contact>, Collection<Contact>> failed = contactsHolder
            .importData(path.getValue());
        if (!failed.getKey().isEmpty() || !failed.getValue().isEmpty()) {
          messageAlert("Предупреждение!",
              failed.getKey().size() + "  контактов не прошли валидацию.\n"
                  + failed.getValue().size() + " контактов уже были загружены.",
              AlertType.WARNING);
        }
      } catch (ContactsLoadException ex) {
        messageAlert(ex.getExceptionHeader(), ex.getMessage(), AlertType.ERROR);
        return false;
      }
    }
    return true;
  }

  private void messageAlert(String title, String message, AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);

    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.getDialogPane().getStylesheets()
        .add(getClass().getResource(Settings.GENERAL_STYLE).toExternalForm());

    alert.showAndWait();
  }

  private boolean agreeAlert(String title, String message) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle(title);

    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.getDialogPane().getStylesheets()
        .add(getClass().getResource(Settings.GENERAL_STYLE).toExternalForm());

    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && (result.get() == ButtonType.OK);
  }

  private void importData() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open data file");
    fileChooser.getExtensionFilters().add(new ExtensionFilter(
        "CONTACTS files (*" + Settings.EXTENSION + ")", "*" + Settings.EXTENSION));
    File file = fileChooser.showOpenDialog(null);

    if (file != null) {
      path.setValue(file.getPath());
    }
  }

  private void exportData() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Open export data folder");
    File exportDirectory = chooser.showDialog(null);

    if (exportDirectory != null) {
      path.setValue(exportDirectory.getPath());
    }
  }

  public SimpleStringProperty pathProperty() {
    return path;
  }

  public SimpleBooleanProperty isExportProperty() {
    return isExport;
  }

  public SimpleStringProperty fileNameProperty() {
    return fileName;
  }
}
