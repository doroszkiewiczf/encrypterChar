package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import app.ClientController;
import cipher.CipherEncrypter;
import jsonParser.JsonMessage;
import jsonParser.MessageType;

public class ClientConnection extends Thread {

	private int port;
	private String host;
	private Socket socket;
	private ObjectInputStream input;
    private ObjectOutputStream output;
    private ClientController controller;
    private CipherEncrypter encrypter;
    
    @Override
    public void run() {
        boolean work = true;
        try {
            while (work) {
                String message = (String) input.readObject();
                System.out.println(message);
                message = encrypter.decrypt(message, "");
                System.out.println(message);
                
                JsonMessage jsonMessage = new JsonMessage(message);
                controller.setStatusTextField(jsonMessage.toString());
                if (jsonMessage.getMsgType() == MessageType.PING) {
                	JsonMessage pingMessage = new JsonMessage(MessageType.PONG, controller.getLogin());
                	sendMessage(pingMessage.toString());
                }
                if (jsonMessage.getMsgType() == MessageType.TEXT && !jsonMessage.getP1().equals("true")) {
                	String text = jsonMessage.getP3();
//                	System.out.println("text: " + text);
//                	System.out.println(getEncodingKey(jsonMessage.getP1(), jsonMessage.getP2()));
//                	text = encrypter.decrypt(text, getEncodingKey(jsonMessage.getP1(), jsonMessage.getP2()));
                	text = encrypter.decrypt(text,getEncodingKey(jsonMessage.getP1(), jsonMessage.getP2()));
                	controller.setMessageTextField(text);
                }
                if (jsonMessage.getMsgType() == MessageType.LIST) {
                	controller.setLoggedTextField("" + jsonMessage.getP1());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public ClientConnection(int port, ClientController controller) {
    	try {
    		encrypter = new CipherEncrypter("");
    		this.controller = controller;
    		this.port = port;
    		InetAddress address = InetAddress.getLocalHost();
            socket=new Socket(address, port);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
    }
    public void sendMessage(String message) {
    	
    	message = encrypter.encrypt(message, "");
    	try {
            output.writeObject(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void register(String login, String pass1, String pass2) {
    	JsonMessage message = new JsonMessage(MessageType.REG, login,pass1, pass2);
    	sendMessage(message.toString());
    }
    public void login(String login, String pass) {
    	JsonMessage message = new JsonMessage(MessageType.LOGIN, login, pass);
    	sendMessage(message.toString());
    }
    public void sendMessageToUser(String login, String loginDest, String text) {
//    	System.out.println(getEncodingKey(login, loginDest));
//    	System.out.println(text);
//    	text = encrypter.encrypt(text, getEncodingKey(login, loginDest));
//    	//text = encrypter.encrypt(text, "derder");
//    	System.out.println("text: " + text);
//    	
//    	text = encrypter.decrypt(text, getEncodingKey(login, loginDest));
//    	System.out.println("decr: " + text);
    	text = encrypter.encrypt(text, getEncodingKey(login, loginDest));
//    	System.out.println("enc: " + text);
    	JsonMessage message = new JsonMessage(MessageType.TEXT, login, loginDest, text);
    	sendMessage(message.toString());
    }
    public void getUsersList(String login) {
    	JsonMessage message = new JsonMessage(MessageType.LIST, login, "t");
    	sendMessage(message.toString());
    }
    
    public String receiveMessage(){
        try {
            return input.readObject().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new String();
    }
    public String getEncodingKey(String from, String to) {
    	String key;
    	if (from.compareTo(to) > 0 || from.compareTo(to) == 0) {
    		key = from + to;
    	} else {
    		key = to + from;
    	}
    	return key;
    }
    public void close() throws IOException{
    	socket.close();
    	output.close();
    	input.close(); 
    }
}
