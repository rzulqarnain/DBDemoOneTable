package viewandcontroller;

import exceptions.CourseException;
import managers.CourseManager;
import database.SetupDatabase;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import models.Course;

public class FXMLDocumentController implements Initializable {

    private ObservableList<Course> tblCourseModel;

    @FXML
    private TableView<Course> tblCourse;
    @FXML
    private TableColumn<Course, String> colCourseId;
    @FXML
    private TableColumn<Course, String> colCourseName;
    @FXML
    private TableColumn<Course, Integer> colCourseCredit;
    @FXML
    private TextField txtCourseId;
    @FXML
    private TextField txtCourseName;
    @FXML
    private TextField txtCourseCredits;
    @FXML
    private ComboBox<Integer> cmbCourseCreditsMin;
    @FXML
    private ComboBox<Integer> cmbCourseCreditsMax;
    @FXML
    private ComboBox<String> cmbCoursePrefix;
    @FXML
    private TextField txtCourseNameContains;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // setup course credits min max combos
        cmbCourseCreditsMin.getItems().clear();
        cmbCourseCreditsMax.getItems().clear();
        for (int i = 1; i <= 9; i++) {
            cmbCourseCreditsMin.getItems().add(i);
            cmbCourseCreditsMax.getItems().add(i);
        }

        // setup course prefix combos
        cmbCoursePrefix.getItems().clear();
        cmbCoursePrefix.getItems().add("CP");
        cmbCoursePrefix.getItems().add("CR");
        cmbCoursePrefix.getItems().add("MA");
        cmbCoursePrefix.getItems().add("MC");

        // setup table -- each column is a field from the record to be displayed in the table
        colCourseId.setCellValueFactory(new PropertyValueFactory(Course.ID));
        colCourseName.setCellValueFactory(new PropertyValueFactory(Course.NAME));
        colCourseCredit.setCellValueFactory(new PropertyValueFactory(Course.CREDITS));

        tblCourseModel = FXCollections.observableList(new ArrayList<Course>());
        tblCourse.setItems(tblCourseModel);
    }

    private void refreshCourseTable(List<Course> courses) throws SQLException {
        tblCourseModel.clear();
        for (Course course : courses) {
            tblCourseModel.add(course);
        }
    }

    @FXML
    private void handleShowAllCourse(ActionEvent event) {
        try {
            refreshCourseTable(CourseManager.findAll());
        } catch (SQLException ex) {
            showAlert(ex.getMessage());
        }
    }

    @FXML
    private void handleShowCourseByCredits(ActionEvent event) {

        Integer min = cmbCourseCreditsMin.getSelectionModel().getSelectedItem();
        Integer max = cmbCourseCreditsMax.getSelectionModel().getSelectedItem();

        if (min != null && max != null) {
            try {
                refreshCourseTable(CourseManager.findByCredits(min, max));
            } catch (SQLException ex) {
                showAlert(ex.getMessage());
            }
        }
    }

    @FXML
    private void handleShowCourseByPrefix(ActionEvent event) {

        String prefix = cmbCoursePrefix.getSelectionModel().getSelectedItem();

        if (prefix != null) {
            try {
                refreshCourseTable(CourseManager.findByCourseId(prefix));
            } catch (SQLException ex) {
                showAlert(ex.getMessage());
            }
        }
    }

    @FXML
    private void handleShowCourseByName(ActionEvent event) {

        String contains = txtCourseNameContains.getText();

        try {
            refreshCourseTable(CourseManager.findByCourseName(contains));
        } catch (SQLException ex) {
            showAlert(ex.getMessage());
        }
    }

    @FXML
    private void handleCreateCourse(ActionEvent event) {
        handleCreateOrUpdate(true);
    }

    @FXML
    private void handleUpdateCourse(ActionEvent event) {
        handleCreateOrUpdate(false);
    }

    private void handleCreateOrUpdate(boolean create) {
        try {
            Course course = getCourseData();
            Course found = CourseManager.findCourse(course.getId());
            if (create) {
                if (found == null) {
                    CourseManager.create(course);
                    refreshCourseTable(CourseManager.findAll());
                } else {
                    showAlert("Course already exists!");
                }
            } else if (found != null) {
                CourseManager.update(course);
                refreshCourseTable(CourseManager.findAll());
            } else {
                showAlert("Course does not exist!");
            }
        } catch (CourseException | SQLException e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCourse(ActionEvent event) {

        try {
            CourseManager.delete(txtCourseId.getText());
            refreshCourseTable(CourseManager.findAll());
        } catch (SQLException ex) {
            showAlert(ex.getMessage());
        }
    }

    private Course getCourseData() throws CourseException {
        return new Course(
                txtCourseId.getText(),
                txtCourseName.getText(),
                txtCourseCredits.getText()
        );
    }

    private void setCourseData(Course course) {
        txtCourseId.setText(course.getId());
        txtCourseName.setText(course.getName());
        txtCourseCredits.setText(String.valueOf(course.getCredits()));
    }

    @FXML
    private void handleCourseClick(MouseEvent event) {

        Course data = tblCourse.getSelectionModel().getSelectedItem();

        if (data != null) {
            setCourseData(data);
        }
    }

    @FXML
    private void handleDeleteDB(ActionEvent event) {
        try {
            SetupDatabase.deleteDB();
        } catch (IOException ex) {
            showAlert(ex.toString());
        }
    }

    @FXML
    private void handleCreateDB(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Please wait...");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
        try {
            SetupDatabase.createDB();
            alert.setResult(ButtonType.CLOSE);
        } catch (IOException ex) {
            alert.setResult(ButtonType.CLOSE);
            showAlert(ex.toString());
        }
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }
}
