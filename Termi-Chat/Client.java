/**
 * Client class for Chat Application
 * @author AS
 * @version 1.0
 */

import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    /**
     * Main method to start the client connection with the server
     * @param args command line argument 
     * @throws IOException throws an exception
     */
    public static void main(String[] args) throws IOException {
        //creating a socket to connect with the server with matching port
        try (Socket socket = new Socket("localhost", 8818)) {
            //getting input stream from the server
            try (Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()))) {
                //sending messages to the server
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                //getting user input from the console
                try (Scanner userInput = new Scanner(new InputStreamReader(System.in))) {
                    //starting a new thread to read messages fromt the server
                    new Thread(() -> {
                        String message;
                        while (in.hasNextLine()) {
                            message = in.nextLine();
                            System.out.println(message);
                        }
                    }).start();

                    // Send messages to server
                    String message;
                    while (true) {
                        message = userInput.nextLine();
                        //exit the loop if messsage is null
                        if(message == null){
                            break;
                        }
                        //otherwise send message to server
                        out.println(message);
                    }
                }
            }
        }
    }
}
