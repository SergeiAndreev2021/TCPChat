package TCPChat;

import TCPChat.spareparts.Message;
import TCPChat.spareparts.Connection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPServer {

    public static void main(String[] args) {
        new TCPServer(8080).startServer();
    }
    private int port;
    private ArrayBlockingQueue<Message> messages;
    public static CopyOnWriteArrayList<Connection> connections  = new CopyOnWriteArrayList<>();

    public TCPServer(int port) {
        this.port = port;
        this.messages = new ArrayBlockingQueue<Message>(8);
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started");

            new Thread(new MessagesToSend()).start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new MessagesConnectionToRead(clientSocket)).start();
            }
        } catch (IOException e) {
          e.getMessage();
        }
    }
    //=======================  RUNNABLE CLASSES========================//

    public class MessagesConnectionToRead implements Runnable {
        private Socket socket;
        private Connection connection;

        public MessagesConnectionToRead(Socket socket) throws IOException {
            this.socket = socket;
            this.connection = new Connection(socket);
        }

        @Override
        public void run() {
            try {
                Message message = connection.readMessage();
                 if (message !=null) {
                     messages.put(message);
                     connections.add(connection);
                     System.out.println(message.getSender()+ " is now in the chat");
                }

                   while (true) {
                       Message message1 = connection.readMessage();
                       messages.put(message1);
                   }

            } catch (InterruptedException e) {
               e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Hope to see u soon!");
            }
        }
    }
    public class MessagesToSend implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Message message = messages.take();
                    for (Connection connection : connections) {
                        if (connection.getId() != message.getSenderId()) {
                            connection.sendMessage(message);
                        }
                    }
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }
            }
        }
    }
}
