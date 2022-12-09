package com.example.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
public class HelloApplication extends Application {
   public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 420);
        stage.setTitle("Chating!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

class MyServer {
    static int i=0;
    public static void main(String[] args) {
        try {
            System.out.println("Server is waiting for client.");
            ServerSocket serverSocket = new ServerSocket(6601);
            while (true){
                Socket sc = serverSocket.accept();
                Client client = new Client(sc);
                Thread t = new Thread(client);
                t.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

class Client implements Runnable{
    String clientName;
    BufferedReader reader;
    BufferedWriter writer;
    final static ArrayList<Client> clients = new ArrayList<>();
    Client(Socket sc){
        try {
            OutputStreamWriter o = new OutputStreamWriter(sc.getOutputStream());
            writer = new BufferedWriter(o);
            InputStreamReader isr = new InputStreamReader(sc.getInputStream());
            reader = new BufferedReader(isr);
            clientName = reader.readLine();
            clients.add(this);
            System.out.println("Client " + clientName + " connected.");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while(true){
            try {
                String data = reader.readLine();
                data = clientName + ": " + data + "\n";
                synchronized (clients){
                    for(Client client: clients){
                        client.writer.write(data);
                        client.writer.flush();
                    }
                }
            }
            catch (SocketException e){
                break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}