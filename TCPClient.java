package TCPChat;

import TCPChat.spareparts.Connection;
import TCPChat.spareparts.Message;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

    public static void main(String[] args) {
        new TCPClient("127.0.0.1", 8080);
    }
    private String ip;
    private int port;
    private Scanner scanner;
    private Connection connection;


    public TCPClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.scanner = new Scanner(System.in);
        try {
            this.connection = new Connection(new Socket(ip, port));
        } catch (IOException e) {
            e.getMessage();
        }

        new Thread(new ClientSender(connection)).start();
        new Thread(new ClientReader(connection)).start();
    }
  // ===================== RUNABLE CLASSES  =========================//
    class ClientReader implements Runnable {
        private Connection connection;

        public ClientReader(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                while (true)  {
                    Message fromServer = connection.readMessage();
                    System.out.println("message from " + fromServer.getSender()
                            + ": " + fromServer.getText()
                              +" "+ fromServer.getDateTime());
                }
            } catch (NullPointerException ex) {
                System.out.println("Server gone wrong. Please try next time");
            }
        }
    }

    class ClientSender implements Runnable {
        private Connection connection;

        public ClientSender(Connection connection) {
            this.connection = connection;
        }
        @Override
        public void run() {
            System.out.println("Enter ur name");
            String userName = scanner.nextLine();
            String text;

            while (true) {
                System.out.println("Enter ur message");
                text = scanner.nextLine();
                connection.sendMessage(Message.getMessage(userName, text));
            }

        }
    }
}
