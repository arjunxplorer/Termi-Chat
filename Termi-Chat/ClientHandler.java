import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 * ClientHandler class represents a client connection
 * It extends the thread class to run concurrently with the main server thread
 * @author AS
 * @version 1.0
 */
class ClientHandler extends Thread {
    //socket representing the connection with client
    private Socket clientSocket;
    //reading input from the client
    private Scanner in;
    //priting output to the client
    private PrintWriter out;
    //username of the client
    private String username;

    /**
     * Construct a new ClientHandler object
     * @param socket the client socket
     */
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    /**
     * Handles the client connection
     * this method is executed whenn the thread is started
     */
    public void run() {
        try {
            //initialize the input and output streams
            in = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            //ask the client for the username
            out.println("Enter your username:");
            username = in.nextLine();

            
            out.println("Welcome, " + username + "!");
            
            //run the loop and notify other clients about the clients connection
            for (ClientHandler client : Server.clients) {
                if (client != this) {
                    sendMessage(client.username + " is also connected.");
                }
            }

            //read the message from the client
            String clientMessage;
            while (in.hasNextLine()) {
                clientMessage = in.nextLine();
                Server.displayMessage(username + ": " + clientMessage,this);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {

                //closing all the opened resources
                clientSocket.close();
                in.close();
                out.close();
                Server.removeClient(this);
                //notify other clients if they are disconnected
                Server.displayMessage(username + " has disconnected.", this);
            } catch (IOException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    /**
     * send message to the client
     * @param message message to be sent 
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * returns the username of the client
     * @return the username
     */
    public String getUserName(){
        return this.username;
    }
}
