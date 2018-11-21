package app;

import java.net.URL;

import java.util.ResourceBundle;

import client.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ClientController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField statusTextField;
    
    @FXML
    private Label loginLabel;
    
    @FXML
    private ListView<String> loggedList;

    @FXML
    private TextField messageTextField;
    
    @FXML
    private TextField messageField;

    @FXML
    private TextField loggedTextField;
    
    @FXML
    private TextField meassageDestTextField;
    
    @FXML
    private TextField portTextField;

    ClientConnection client;
    String login;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		
	}
	public void registerButton() {
		client.register(loginTextField.getText(), passwordTextField.getText(), passwordTextField.getText() );
		//registerButton.setDisable(true);
	}
	public void loginButton() {
		System.out.println(loginTextField.getText());
		System.out.println(passwordTextField.getText());
		client.login(loginTextField.getText(), passwordTextField.getText());
		login = loginTextField.getText();
		loginLabel.setText(login);
		//loginButton.setDisable(true);
	}
	public void setStatusTextField(String text) {
		statusTextField.setText(text);
	}
	public void connectToServer() {
		client = new ClientConnection(Integer.valueOf(portTextField.getText()), this);
		client.start();
		System.out.println("Connected here:");
	}
	public void sendMessageToUser() {
		System.out.println("Wiadomosc powinna zostac wys³ana");
		client.sendMessageToUser(login,meassageDestTextField.getText(),messageField.getText());
	}
	public void getLoggedList() {
		System.out.println("Pobieram liste zalogowanych u¿ytkowników");
		client.getUsersList(login);
	}
	public void setMessageTextField(String text) {
		messageTextField.setText(text);
	}
	public String getLogin() {
		return this.login;
	}
	public void setLoggedTextField(String value) {
		loggedTextField.setText(value);
	}
	public void setMeassageDestTextField(String value) {
		meassageDestTextField.setText(value);
	}
}
