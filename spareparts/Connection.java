package TCPChat.spareparts;

import TCPChat.TCPServer;
import java.io.*;
import java.net.Socket;

public class Connection implements AutoCloseable {

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String senderName;
    private int id;

    public Connection(Socket socket) {
        try {
            this.socket = socket;
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection with "+ senderName+" lost");
            closeEverything(socket,input,output);
        }
    }
    public int getId() {
        return id;
    }

    public void sendMessage(Message message)  {

        try {
            message.setDateTime();
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            closeEverything(socket,input,output);
        }

    }
    public Message readMessage() {
        Message message = null;
        try {
          message = (Message) input.readObject();
            senderName = message.getSender();
            id = message.getSenderId();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection with "+ senderName+" lost");
            closeEverything(socket,input,output);
        }
        return message;
    }

    @Override
    public String toString() {
        return  " SenderName :" + senderName;
    }

    @Override
    public void close() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
           e.getMessage();
        }

    }
    public void removeConnection(){
        TCPServer.connections.remove(this);
    }
    public void closeEverything(Socket socket, ObjectInputStream input,ObjectOutputStream output){
         removeConnection();
        try{
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
