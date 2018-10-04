/***
 * Russell Felts
 * Java Programming 2 - Assignment 7 Java Concurrency
 *
 * 1) What is the difference between multi threading and multi processing?
 * - Multiprocessing is using more than one CPUs/processors of the system.
 * - Multithreading is allowing a process to create/execute multiple threads for execution of the process.
 *
 * 2) What are the two primary ways to create separate Threads in Java ?
 * - Having a class extend the Thread class.
 * - Having a class implement the Runnable interface.
 *
 * 3) What does the "synchronized" keyword do ?
 * - Coordinates the use of the address space and other resources (mutual exclusion) and ensures
 * that the each thread has to have a lock before it executes.
 *
 * 4) What are intrinsic locks and explicit locks
 * - Intrinsic lock is the built in object lock
 * - Explicit lock is a user defined lock
 *
 * 5) Why is wait() method only allowed to be called in a synchronized context?
 * - Because it uses/waits for the lock acquired via the synchronized context?
 *
 * 6) Implement a program NumberPrinter.java which works as 2 threads, one thread prints odd numbers and another prints
 * even numbers.
 */

public class NumberPrinter implements Runnable {

    private static IncrementNumber incrementNumber;
    private Thread newThread;

    /***
     * Constructor
     * Creates an increment number object and a new thread and sets the thread name.
     * @param oddEven - String that represents which method to call and the thread name.
     */

    public NumberPrinter(String oddEven) {
        incrementNumber = new IncrementNumber();
        newThread = new Thread(this, oddEven.toLowerCase());
    }

    /***
     * The method used to start the threads. Contains logic to handle ensuring the main thread does not complete before
     * the other threads.
     */

    public void start(){

        newThread.start();

        // Join the main thread when the even thread is started, in this scenario, ensures the main thread doesn't
        // finish until the even thread completes. Again, in this scenario, the even thread  is the last thread to
        // complete.
        if(newThread.getName().equals("even")) {
            try {
                newThread.join();
            } catch (InterruptedException intEx) {
                System.out.println("Interrupted doing join " + intEx);
            }
        }

    }

    /***
     * Overrides the run method. Determines which method to call based on the thread name.
     */

    @Override
    public void run(){
        if (newThread.getName().toLowerCase().equals("odd")) {
            incrementNumber.IncrementOddNumber();
        } else {
            incrementNumber.IncrementEvenNumber();
        }
    }

}

/***
 * Class used to hold the synchronized methods for incrementing odd and even numbers.
 */

class IncrementNumber{

    // Set the initial lock state to even to ensure the odd thread executes first
    private static String lockState = "even";

    /***
     * Synchronized method that increments odd numbers.
     */

    protected synchronized void IncrementOddNumber(){

        // Set the initial odd number
        int oddNumber = 1;

        while (oddNumber <= 1000) {
            System.out.println(oddNumber);

            // Increment to the next odd number
            oddNumber += 2;

            // Change the lock state so the even thread can run
            lockState = "odd";
            notify();

            // Monitor the lock state and wait until the thread can execute again
            try {

                while (lockState.equals("odd")) {
                    wait();
                }

            } catch (InterruptedException interruptedEx) {
                System.out.println("Odd thread interrupted: " + interruptedEx);
            }
        }

        // This thread is done so notify the other waiting thread that it can run.
        lockState = "odd";
        notify();
    }

    /***
     * Synchronized method that increments even numbers.
     */

    protected synchronized void IncrementEvenNumber(){

        // Set the initial odd number
        int evenNumber = 2;

        while (evenNumber <= 1000) {
            System.out.println(evenNumber);

            // Increment to the next even number
            evenNumber += 2;

            // Change the lock state so the odd thread can run
            lockState = "even";
            notify();

            // Monitor the lock state and wait until the thread can execute again
            try {

                while (lockState.equals("even")) {
                    wait();
                }
            } catch (InterruptedException interruptedEx) {
                System.out.println("Even thread interrupted: " + interruptedEx);
            }
        }

        // This thread is done so notify the other waiting thread that it can run.
        lockState = "even";
        notify();
    }
}
