
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class AddClient {

    /***
     * Send two integer to the server. Then prints out the result returned from the server.
     *
     * @param args -  String contianing to numbers that must be converted to
     */

    public AddClient(String[] args) {

        ArrayList<Integer> numbers = new ArrayList<>();

        // Open a client socket and create the output stream
        try (Socket clientSocket = new Socket(InetAddress.getLocalHost(), 7646)){
            try(DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream())){
                // Cycle over the args writing them to the output stream for the server
                for (String num: args) {
                    dataOut.writeInt(Integer.parseInt(num));
                }

                // Read the stream until there is no data
                do {
                    numbers.add(dataIn.readInt());
                } while (dataIn.available() > 0);

            }

            // Print out the data from the server
            int numberSize = numbers.size() - 1;
            System.out.print("Addition of ");
            for(int iLoop= 0; iLoop < numberSize; iLoop++){
                System.out.print(numbers.get(iLoop));

                // Determine if an = or + symbol should be printed
                System.out.print(iLoop == numberSize - 1 ? " = " :  " + ");
            }

            // Print the final value which should be the sum
            System.out.print(numbers.get(numberSize) + "\n");

        } catch (UnknownHostException unKnownHostExcept) {
            System.out.println("Unknown Host exception trying to get a socket in AddClient: ");
            unKnownHostExcept.printStackTrace();
        } catch (IOException ioException) {
            System.out.print("IO exception occurred in AddClient:");
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
