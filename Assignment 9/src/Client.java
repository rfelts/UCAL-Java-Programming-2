/***
 * Client program takes the client name and server ports as program arguments.
 *
 * When Client.java is run it opens a GUI window and immediately connects to the server by sending the name and port.
 *
 * The Hello message when received from the server should be displayed in the window.
 *
 * Users should be able to type a message and click send, that message when successfully sent to the server should
 * appear in the text area as Sent : [Message].
 *
 * On receipt of the same message from the server, The text area should display the message Received : [Message].
 *
 * Clients can leave without notifying the server. The server maintains a record of a client only for 2 mins.
 *
 * Each time a client sends a message the two minutes should be reset and if the client does not communicate for
 * 2 minutes, the server forgets about the client and the client name can be reused.
 *
 */

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class Client {

    private Socket clientSocket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;
    private UIHandler clientUI;

    /**
     * Initialize the client ui, create a connection with the server, processes the incoming data from the server
     *
     * @param args String arguments for the client's name and server port
     */

    private Client(String args[]) {

        // Create the UI
        clientUI = new UIHandler(args[0], args[1]);

        try {
            // Create a connection with the server and send the initial message containing the client name
            clientSocket = new Socket(InetAddress.getLocalHost(), Integer.parseInt(args[1]));
            dataOut = new DataOutputStream(clientSocket.getOutputStream());
            dataOut.writeUTF(args[0]);

            //Create a buffer for the incoming server messages
            dataIn = new DataInputStream(clientSocket.getInputStream());

            // Keep processing data while the socket and input stream are open
            while(!clientSocket.isClosed() || dataIn.available() > 0) {

                // Determine if this is the first entry into the text area
                if(clientUI.serverTextArea.getText().length() == 0){
                    clientUI.serverTextArea.setText(dataIn.readUTF());
                }

                // Put the server response in the server message text field
                clientUI.serverTextArea.append("\nReceived: " + dataIn.readUTF());
            }
        }
        catch (UnknownHostException unKnownHostExcept) {
            unKnownHostExcept.printStackTrace();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
        finally {
            // If no connection to the server exists add a message to the text area
            clientUI.serverTextArea.append("\nSocket is closed. Restart to send messages.");
        }
    }

    /**
     * Send the messages pulled from the text area to the server
     *
     * @param clientMessage String containing the message to send to the server
     */

    private void sendMessage(String clientMessage){

        if(!clientSocket.isClosed()){
            try {
                dataOut.writeUTF(clientMessage);
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else{
            clientUI.serverTextArea.append("\nSocket is closed. Restart to send messages.");
        }
    }


    //Class used to process the server UI
    private class UIHandler {

        JFrame frame = new JFrame();
        JTextArea serverTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(serverTextArea);
        JTextField clientTextField = new JTextField();
        JLabel label = new JLabel();
        JButton sendButton = new JButton("Send");
        JButton closeButton = new JButton( "Quit");

        /**
         * Creates a ui frame containing a label, text area, text field, and buttons. Also sends data to the server via
         * the button's action handler
         *
         * @param name String containing the client's name
         * @param clientPort String representing the port the server is listening on.
         */

         private UIHandler(String name, String clientPort) {
             label.setHorizontalAlignment(SwingConstants.CENTER);
             label.setVerticalAlignment(SwingConstants.TOP);
             label.setText("Client Name: " + name + " - Port: " + clientPort);

             serverTextArea.setEditable(false);

             clientTextField.setBounds(50, 190, 300, 30);
             clientTextField.setEditable(true);

             // The enter key pressed in the text field sends the contents to the server by activating the send button
             clientTextField.addKeyListener(new KeyListener() {
                 @Override
                 public void keyPressed(KeyEvent enterEvent) {
                     if (enterEvent.getKeyCode() == KeyEvent.VK_ENTER){
                        sendButton.doClick();
                     }
                 }
                 @Override
                 public void keyReleased(KeyEvent arg0) {
                     // Do nothing
                 }

                 @Override
                 public void keyTyped(KeyEvent arg0) {
                     // Do nothing
                 }
             });

             scrollPane.setBounds(50, 30, 300, 140);
             scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

             closeButton.setBounds(190, 230, 80,30);

             // Dismiss the UI and quit the program
             closeButton.addActionListener(closeButtonEvent -> {
                 this.frame.dispose();
                 System.exit(0);
                     });

             sendButton.setBounds(270, 230, 80,30);

             // Send text filed content to server and clear the text field when the send button is pressed
             sendButton.addActionListener(sendButtonEvent -> {
                 String tempMessage = clientTextField.getText();
                 serverTextArea.append("\nSent: " + tempMessage);
                 sendMessage(tempMessage);
                 clientTextField.setText(null);
             });

             frame.setBounds(560, 40,400, 300);
             frame.setTitle("Client: " + name);
             frame.add(scrollPane);
             frame.add(clientTextField);
             frame.add(closeButton);
             frame.add(sendButton);
             frame.add(label);
             frame.setVisible(true);
         }

    }

    // Start the client program
    public static void main(String[] args) {

        if (args.length == 2) {
            new Client(args);
        } else {
            System.out.println("Invalid number of arguments.");
        }
    }
}

