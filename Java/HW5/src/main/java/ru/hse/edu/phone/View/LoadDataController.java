/**
 * @author <a href="mailto:sabryantsev@edu.hse.ru"> Stepan Bryantsev</a>
 */

package ru.hse.edu.phone.View;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.hse.edu.phone.ViewModels.LoadDataViewModel;


/**
 * Контроллер окна импорта / экспорта.
 */
public class LoadDataController {

  private LoadDataViewModel loadDataViewModel;

  @FXML TextField pathField;
  @FXML TextField fileNameField;
  @FXML Pane fileNamePane;

  @FXML private void onBrowse() {
    loadDataViewModel.browse();
  }
  @FXML private void onConfirm(InputEvent event) {
    if (loadDataViewModel.confirm()) {
      closeWindow(event);
    }
  }

  @FXML public void closeWindow(InputEvent event) {
    final Node source = (Node) event.getSource();
    final Stage stage = (Stage) source.getScene().getWindow();
    stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
  }

  public void setLoadDataViewModel(LoadDataViewModel loadDataViewModel) {
    this.loadDataViewModel = loadDataViewModel;

    pathField.textProperty().bindBidirectional(loadDataViewModel.pathProperty());
    loadDataViewModel.fileNameProperty().bind(fileNameField.textProperty());

    fileNamePane.setVisible(loadDataViewModel.isExportProperty().getValue());
    fileNamePane.setManaged(loadDataViewModel.isExportProperty().getValue());
  }

}
