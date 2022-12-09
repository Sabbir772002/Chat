package com.example.chat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
public class User {
    String []name;
    @FXML
    Button button;
    @FXML
    TextArea showArea;
    @FXML
    TextField inputField;
    boolean isConnected = false;
    BufferedReader reader;
    String inputName;
    BufferedWriter writer;
    public User(){
        name= new String[]{"sabbir","shahin"};
    }
    @FXML
    void buttonPressed(){
        if(!isConnected) {
            // Client is not connected with the server, lets connect with server
            inputName = inputField.getText();
            inputField.clear();
            int i=1;
            for(String n : name){
              if(n.equals(inputName))i=1;

            }
            if(inputName == null || inputName.length() == 0|| i!=1){
                showArea.appendText("Enter a valid name!\n");
                return;
            }

            try {

                Socket sc = new Socket("localhost", 6601);
                OutputStreamWriter o = new OutputStreamWriter(sc.getOutputStream());
                writer = new BufferedWriter(o);
                writer.write(inputName+"\n");
                writer.flush();
                InputStreamReader isr = new InputStreamReader(sc.getInputStream());
                reader = new BufferedReader(isr);
                String name11 = "sabbir";
                String name12 = "shahin";
                //Anonymous inner class
                Thread serverListener = new Thread(){
                    @Override
                    public void run() {
                        while(true){
                            try {
                                String data = reader.readLine() + "\n";
                                showArea.appendText(data);
                            }
                            catch (SocketException e){

                                showArea.appendText("Connection lost!\n");
                                break;
                            }
                            catch (IOException e) {

                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                serverListener.start();
               // showArea.appendText("Connection established!\n");
           /*     Alert a = new Alert(Alert.AlertType.NONE);
                EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent e)
                            {

                                a.setAlertType(Alert.AlertType.CONFIRMATION);
                                a.show();
                            }
                        };*/

                BufferedReader in = null;
                File file = new File("msg.txt");
                try {
                    in = new BufferedReader(new FileReader(file));
                    String str;
                    while ((str = in.readLine()) != null) {
                        showArea.appendText(str+ "\n");
                    }
                } catch (IOException e) {
                } finally {
                    try { in.close(); } catch (Exception ex) { }
                }
                button.setText("Send");
                inputField.setPromptText("Write your message.");
                isConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }catch(Exception e){

            }
        }
        else{
            try {
                String msg = inputField.getText();
                inputField.clear();
                if (msg == null || msg.length() == 0) {
                    return;
                }
                File file = new File("msg.txt");
                FileWriter w = new FileWriter(file,true);
                w.write(inputName+": "+msg+ "\n");
                writer.write(msg + "\n");
                writer.flush();
                w.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}