package cn.riversky.controller;

import cn.riversky.config.StageManager;
import cn.riversky.domain.UmsAdmin;
import cn.riversky.service.UmsAdminService;
import cn.riversky.view.FxmlView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author riversky
 * @date 2020/12/11
 **/
@Controller
public class UserController implements Initializable {
    @FXML
    private Button btnLogout;

    @FXML
    private Label userId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker dob;

    @FXML
    private RadioButton rbMale;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton rbFemale;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button reset;

    @FXML
    private Button saveUser;

    @FXML
    private TableView<UmsAdmin> userTable;

    @FXML
    private TableColumn<UmsAdmin, Long> colUserId;

    @FXML
    private TableColumn<UmsAdmin, String> colFirstName;

    @FXML
    private TableColumn<UmsAdmin, String> colLastName;

    @FXML
    private TableColumn<UmsAdmin, LocalDate> colDOB;

    @FXML
    private TableColumn<UmsAdmin, String> colGender;

    @FXML
    private TableColumn<UmsAdmin, String> colRole;

    @FXML
    private TableColumn<UmsAdmin, String> colEmail;

    @FXML
    private TableColumn<UmsAdmin, Boolean> colEdit;

    @FXML
    private MenuItem deleteUsers;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private UmsAdminService userService;
    private ObservableList<UmsAdmin> userList = FXCollections.observableArrayList();
    private ObservableList<String> roles = FXCollections.observableArrayList("Admin", "User");

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
    @FXML
    private void logout(ActionEvent event) throws IOException {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void saveUser(ActionEvent event){

        if(validate("First Name", getFirstName(), "[a-zA-Z]+") &&
                validate("Last Name", getLastName(), "[a-zA-Z]+") &&
                emptyValidation("DOB", dob.getEditor().getText().isEmpty()) &&
                emptyValidation("Role", getRole() == null) ){

            if(userId.getText() == null || userId.getText() == ""){
                if(validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+") &&
                        emptyValidation("Password", getPassword().isEmpty())){

                    UmsAdmin user = new UmsAdmin();
                    user.setUsername(getFirstName());
                    user.setNickName(getLastName());
                    user.setLoginTime(getDob());
                    user.setNote(getGender());
                    user.setPassword(getRole());
                    user.setEmail(getEmail());
                    user.setPassword(getPassword());

                    UmsAdmin newUser = userService.save(user);

                    saveAlert(newUser);
                }

            }else{
                UmsAdmin user = userService.find(Long.parseLong(userId.getText()));
                user.setUsername(getFirstName());
                user.setNickName(getLastName());
                user.setLoginTime(getDob());
                user.setNote(getGender());
                user.setEmail(getRole());
                UmsAdmin updatedUser =  userService.update(user);
                updateAlert(updatedUser);
            }

            clearFields();
            loadUserDetails();
        }


    }

    @FXML
    private void deleteUsers(ActionEvent event){
        List<UmsAdmin> users = userTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if(action.get() == ButtonType.OK){
            userService.deleteInBatch(users);
        }

        loadUserDetails();
    }

    private void clearFields() {
        userId.setText(null);
        firstName.clear();
        lastName.clear();
        dob.getEditor().clear();
        rbMale.setSelected(true);
        rbFemale.setSelected(false);
        cbRole.getSelectionModel().clearSelection();
        email.clear();
        password.clear();
    }

    private void saveAlert(UmsAdmin user){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user "+user.getNickName()+" "+user.getUsername() +" has been created and \n"+getGenderTitle(user.getNote())+" id is "+ user.getId() +".");
        alert.showAndWait();
    }

    private void updateAlert(UmsAdmin user){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user "+user.getNickName()+" "+user.getUsername() +" has been updated.");
        alert.showAndWait();
    }

    private String getGenderTitle(String gender){
        return (gender.equals("Male")) ? "his" : "her";
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public LocalDate getDob() {
        return dob.getValue();
    }

    public String getGender(){
        return rbMale.isSelected() ? "Male" : "Female";
    }

    public String getRole() {
        return cbRole.getSelectionModel().getSelectedItem();
    }

    public String getEmail() {
        return email.getText();
    }

    public String getPassword() {
        return password.getText();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cbRole.setItems(roles);

        userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        // Add all users into table
        loadUserDetails();
    }



    /*
     *  Set All userTable column properties
     */
    private void setColumnProperties(){
		/* Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd/MM/yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }
		     @Override
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));*/

        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("loginTime"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("note"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<UmsAdmin, Boolean>, TableCell<UmsAdmin, Boolean>> cellFactory =
            new Callback<TableColumn<UmsAdmin, Boolean>, TableCell<UmsAdmin, Boolean>>()
            {
                @Override
                public TableCell<UmsAdmin, Boolean> call( final TableColumn<UmsAdmin, Boolean> param)
                {
                    final TableCell<UmsAdmin, Boolean> cell = new TableCell<UmsAdmin, Boolean>()
                    {
                        Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                        final Button btnEdit = new Button();

                        @Override
                        public void updateItem(Boolean check, boolean empty)
                        {
                            super.updateItem(check, empty);
                            if(empty)
                            {
                                setGraphic(null);
                                setText(null);
                            }
                            else{
                                btnEdit.setOnAction(e ->{
                                    UmsAdmin user = getTableView().getItems().get(getIndex());
                                    updateUser(user);
                                });

                                btnEdit.setStyle("-fx-background-color: transparent;");
                                ImageView iv = new ImageView();
                                iv.setImage(imgEdit);
                                iv.setPreserveRatio(true);
                                iv.setSmooth(true);
                                iv.setCache(true);
                                btnEdit.setGraphic(iv);

                                setGraphic(btnEdit);
                                setAlignment(Pos.CENTER);
                                setText(null);
                            }
                        }

                        private void updateUser(UmsAdmin user) {
                            userId.setText(Long.toString(user.getId()));
                            firstName.setText(user.getUsername());
                            lastName.setText(user.getNickName());
                            dob.setValue(user.getLoginTime());
                            if(user.getEmail().equals("Male")){
                                rbMale.setSelected(true);
                            } else{
                                rbFemale.setSelected(true);
                            }
                            cbRole.getSelectionModel().select(user.getIcon());
                        }
                    };
                    return cell;
                }
            };



    /*
     *  Add All users to observable list and update table
     */
    private void loadUserDetails(){
        userList.clear();
        userList.addAll(userService.findAll());

        userTable.setItems(userList);
    }

    /*
     * Validations
     */
    private boolean validate(String field, String value, String pattern){
        if(!value.isEmpty()){
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if(m.find() && m.group().equals(value)){
                return true;
            }else{
                validationAlert(field, false);
                return false;
            }
        }else{
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty){
        if(!empty){
            return true;
        }else{
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if(field.equals("Role")){
            alert.setContentText("Please Select "+ field);
        }
        else{
            if(empty) {
                alert.setContentText("Please Enter "+ field);
            }  else {
                alert.setContentText("Please Enter Valid "+ field);
            }
        }
        alert.showAndWait();
    }
}
