/***
 * Russell Felts
 * Java Programming 2 Assignment 8
 *
 * 1) What is the difference between traditional Java IO and NIO?
 *      IO is stream oriented and NIO is buffer oriented.
 *      IO streams are blocking while NIO is non blocking.
 *      NIO uses selectors to allow threads to manage multiple connections.
 *
 * What are some advantages of one above the other, please explain for both.
 *      Advantages of NIO -
 *          Copying can be accomplished by directly transferring data from one channel to another. So copying large
 *          files may be faster.
 *          Allows managing multiple channels (network connections or files) using only a single (or few) threads. (I.E.
 *          managing all outbound connection with a single thread.)
 *      Advantages of IO -
 *          No data (buffer) managemnt overhead. You know when data is collected from the stream it's ready to be
 *          processed.
 *
 * 2) How does the TCP handshake work?
 *      Client send a meesage to the server letting it know it wants to start communication.
 *      Server responds to the client acknowledging it got the client's request.
 *      Client acknowledges the response from the server and a reliable connection is established.
 *
 * 3) What is the difference between TCP and UDP?
 *      TCP is considered reliable while UDP is not.
 *
 * Why is UDP unreliable?
 *      Packets are simply sent without knowing if the receiving computer is listening or available. So, the
 *
 * 4) What are some good applications for using UDP?
 *      When you are delivering data that can be lost because newer data coming in will replace that previous data/state.
 *      For example, Weather data, video streaming, VoIP, notifications, or gaming data.
 **
 * 5) What are the two types of sockets?
 *      Client(connecting) and Server(listening)
 *
 * Can the server sockets accept multiple connections at the same time?
 *      Yes
 *
 * Would the other clients block?
 *      Client connections requests are queued. However, client requests can be serviced simultaneously through the use
 *      of threads--one thread to process each client connection.
 *
 * Is the order maintained?
 *      Since the connection are queued, the server must accept the connections sequentially.
 *
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class AddServer {

    /***
     * Sends a responds to the client with the addition of these two integers received from the client.
     *
     * @param args - the port to be used by the server socket
     */

    public AddServer(String[] args) {

        ArrayList<Integer> numbers = new ArrayList<>();

        // Create a server socket and wait for the client to send data
        try (ServerSocket serverSock = new ServerSocket(Integer.parseInt(args[0]))){
            Socket socket = serverSock.accept();

            // Create input and output streams for receiving and sending data
            try(DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream())) {

                // Read the stream until there is no data
                do {
                    numbers.add(dataIn.readInt());
                } while (dataIn.available() > 0);


                // Sum the list values
                numbers.add(numbers.stream().mapToInt(Integer::intValue).sum());

                // Cycle over the args writing them to the output stream for the server
                for (int num: numbers) {
                    dataOut.writeInt(num);
                }

            }

        } catch (UnknownHostException unKnownHostExcept) {
            System.out.println("Unknown host exception trying to get a socket in AddServer:");
            unKnownHostExcept.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("An IO exception has occured in AddServer:");
            ioException.printStackTrace();
        }

    }


    public static void main(String[] args) {
        if (args.length == 1) {
            new AddServer(args);
        } else {
            new AddClient(args);
        }
    }
}



