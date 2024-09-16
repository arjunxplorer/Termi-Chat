/**
 * Server class for Chat Application
 * @author AS
 * @version 1.0
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;


public class Server {
    //declaring serverSocket for port connection
    private static ServerSocket serverSocket;
    //to control multiple clients
    static ArrayList<ClientHandler> clients = new ArrayList<>();
    //file where all the messages will be stored
    private static String fileName = "messages.txt";
    /**
     * Main method to establish the server for client connection
     * @param args command line argument
     * @throws IOException throws an Input/Output exception
     */

    public static void main(String[] args) throws IOException {
        //opening connection at port 8818
        serverSocket = new ServerSocket(8818);
        System.out.println("Server is live");
        //getting ipadress
        System.out.println("Connection established with " + serverSocket.getInetAddress());

        while (true) {
            //waiting for a client connection
            Socket clientSocket = serverSocket.accept();

            //creating a new clientHandler instance for the new client
            ClientHandler clientHandler = new ClientHandler(clientSocket);

            //adding client to the list of other connected clients
            clients.add(clientHandler);

            //start the clientHandler thread to handle the new client
            clientHandler.start();
        }
    }


    /**
     * Displays the messages sent by one client to multiple clients connected to the server
     * @param message takes message to be displayed
     * @param sender information of who is sending the message
     */
   
    public static void displayMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) { 
                client.sendMessage(message);
            }
        }
        saveMessage(message); 
    }

   /**
    * Saves the conversation into a text file
    * @param message takes the message to be saved
    */
    private static void saveMessage(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * removes a client from the ClientHandler and displays a message
     * @param client client to be removed
     */
    public static void removeClient(ClientHandler client) {
        String username = client.getUserName();
        clients.remove(client);
        System.out.println(username+" has disconnected");
    }
}

