/**
 * When the server.java is run it should start a server at a port which will be a program argument
 * If port is not available appropriate error must be shown
 *
 * Server should open a GUI Window displaying the above components and the message …Ready to handle requests
 *
 * After receiving a request from a client it should display "A new request received from: Client name" (Client
 * will send the name with first request). Also a message should be returned back to the same client with their name
 * “Hello [ClientName]”.
 *
 * For simplicity one client name can be used only once. If a client tries to use a name that is already used, the
 * server should send back a message to restart the client with a different name and should reject the connection.
 *
 * Each subsequent message received from a client should be returned back to the client as is (as an echo). But also
 * be displayed in the window as [ClientName] : [Message from Client ] and time stamp.
 *
 * The server maintains a record of a client only for 2 mins. Each time a client sends a message the two minutes should
 * be reset and if the client does not communicate for 2 minutes, the server forgets about the client and the client
 * name can be reused.
 *
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.net.*;
import javax.swing.*;
import java.text.*;

// Server class
public class Server {

    private static final HashMap<String, Long> clientInfo = new HashMap<>();

    /**
     * Binds the server to a socket. Initializes the server ui. Verifies client names are unique. Creates a client
     * handler for each client.
     *
     * @param args - String representing the server port to be used.
     */

    private Server(String[] args) {

        // Create UI
        UIHandler serverUI = new UIHandler(args[0]);

        // Create a server socket and wait for clients to send data
        try{
            ServerSocket serverSock = new ServerSocket(Integer.parseInt(args[0]));

            // Keep the socket open
            try{
                while(true) {

                    // Creating a client handler thread
                    new ClientHandler(serverUI, serverSock.accept()).start();

                }
            }
            finally {
                serverSock.close();
            }

        }
        catch (UnknownHostException unKnownHostExcept) {
           unKnownHostExcept.printStackTrace();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    //Class used to process the server UI
    private class UIHandler {

        JFrame frame = new JFrame("Server");
        JTextArea message = new JTextArea();
        JLabel label = new JLabel();
        JScrollPane scrollPane = new JScrollPane(message);
        JButton quitButton = new JButton("Quit");

        /**
         * Creates a ui frame containing a label and text field.
         *
         * @param serverPort String representing the port the server is listening on.
         */

        private UIHandler(String serverPort) {

            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.TOP);
            label.setText("Server Port: " + serverPort + " …Ready to handle requests");

            message.setWrapStyleWord(true);
            message.setEditable(false);

            scrollPane.setBounds(50, 40, 400, 180);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            quitButton.setBounds(370, 230, 80,30);
            quitButton.addActionListener(closeButtonEvent -> {
                this.frame.dispose();
                System.exit(0);
            });

            frame.setBounds(20,40,500, 300);
            frame.add(scrollPane);
            frame.add(quitButton);
            frame.add(label);
            frame.setVisible(true);

        }
    }


    // Takes care of all the actions related to a client
    private class ClientHandler extends Thread {
        private String name;
        private Socket socket;
        private Boolean threadRunning = true;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        private UIHandler serverUI;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss:SSS");

        /**
         * Handles the client messages. Verifies the client name sent to the server is not already in use. The server
         * will report an error to the client and close the connection if the client name is already in use.
         *
         * Each message, after the initial client start up message, received from a client is returned back to the
         * client unmodified. But also is displayed in the textfield as [ClientName] : [Message from Client ] along
         * with a time stamp.
         *
         * @param socket the socket used for communicating with the client
         */

        private ClientHandler(UIHandler serverUI, Socket socket) {

            this.socket = socket;
            this.serverUI = serverUI;
            Date dateInfo = new Date();

            // Create input and output streams for receiving and sending data
            try {

                dataIn = new DataInputStream(this.socket.getInputStream());
                dataOut = new DataOutputStream(this.socket.getOutputStream());

                // Read the stream and get the current time
                name = dataIn.readUTF();
                long tempDate = dateInfo.getTime();

                /* Check the clients name hashmap for the clients name and add it if needed. Then send appropriate
                message to the client */
                synchronized (clientInfo) {
                    if (!clientInfo.containsKey(name)) {
                        clientInfo.put(name, tempDate);

                        // Check to see if there is already a message in the text area
                        if(serverUI.message.getText().trim().length() > 0){
                            // Load the client connection message into the Server UI
                            serverUI.message.append("\nA new request was received from: " + name + ": " +
                                    dateFormat.format(tempDate));
                        }
                        else{
                            // Load the initial client connection message into the Server UI
                            serverUI.message.setText("A new request was received from: " + name + ": " +
                                    dateFormat.format(tempDate));
                        }

                        // Send the initial server response to the client
                        dataOut.writeUTF( "Hello " + name);
                    }
                    else {

                        // Send the client name error message and close the connection
                        dataOut.writeUTF("The name " + name + " is already in use. " +
                                "\nPlease restart the client with a new name.\nClosing the connection.");
                        socket.close();
                        threadRunning = false;
                    }
                }
            }
            catch (IOException ioException) {
                System.out.println("IO Exception in the Client Handler");
                ioException.printStackTrace();
            }
        }


        public void run() {

            String clientMessage = null;
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            // Stop the thread
            if (!threadRunning) {
                return;
            }

            // Creating a Runnable Task to handle the timing out of the client connection after 2 minutes of inactivity
            Runnable task = () -> {

                    // Removing the client name
                    removeName(name);

                    // Shutting down the socket and data streams
                    try {
                        dataIn.close();
                        dataOut.close();
                        socket.close();
                    }
                    catch(IOException ioException) {
                        System.out.println("IO Exception in the Server Run Runnable method.");
                        ioException.printStackTrace();
                    }
                };

            // Start the scheduler
            Future<?> futureSchedule = scheduler.schedule(task, 2, TimeUnit.MINUTES);

            try {

                while (!this.socket.isClosed() || dataIn.available() > 0) {

                    // Read the client message
                    clientMessage = dataIn.readUTF();

                    // Get the current time and reset the timer
                    Date newDate = new Date();
                    futureSchedule.cancel(true);
                    futureSchedule = scheduler.schedule(task, 2, TimeUnit.MINUTES);

                    // Add the message to the ui text area
                    serverUI.message.append("\n" + name + ": " + clientMessage + ": " + dateFormat.format(newDate));

                    // Return the client message
                    dataOut.writeUTF(clientMessage);
                }
            }

            // Because the DataInputStream is waiting to read. When/if the socket is closed an IOException is expected
            catch (IOException ioException) {
                System.out.println("Most likely the client closed their connection.");
                System.out.println("Socket closed: " + socket.isClosed());
                ioException.printStackTrace();
            }

            // Clean up by shutting down the ScheduledExecutorService and outputting a client disconnect message to
            // the server UI
            finally {

                String disconnectMessage = "\n" + name + " was disconnected from the server.";

                // Get the current time
                Date newDate = new Date();
                if(futureSchedule.isDone()){
                    // Display the timeout disconnect message when the client's connection timesout
                    serverUI.message.append(disconnectMessage + " Their connection timed out after 2 minutes of " +
                            "inactivity: " + dateFormat.format(newDate));
                }
                else{
                    // Display the standard disconnect message when the client disconnects
                    serverUI.message.append(disconnectMessage + ": " +  dateFormat.format(newDate));
                    removeName(name);
                }
                scheduler.shutdown();
            }
        }

        /**
         * Removes the client names from the list
         *
         * @param name String containing the clients name
         */

        private void removeName(String name){
            synchronized (clientInfo) {
                if (clientInfo.containsKey(name)) {
                    clientInfo.remove(name);
                }
            }
        }
    }

    // Start the server program
    public static void main(String[] args) {

        if (args.length == 1) {
            new Server(args);
        }
        else {
            System.out.println("Invalid number of arguments.");
        }
    }
}
