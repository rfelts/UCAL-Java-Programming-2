/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileIO;

import java.io.*;
import java.util.*;
import java.util.Map.*;

/**
 * Count the words in the file and store the information in two columns 
 * "Word" | "No of occurrences of that word".
 * Output this information to another file called result.txt. The program should 
 * also output this information to console. Output should be sorted in descending 
 * order, word with highest count must appear at the top.
 * @author rfelts
 */

public class FileIO {
    
    Map<String,Integer> wordMap = new HashMap<>();
    
    /***
     * Reads a file from disk. File must be in the project directory.
     * @param fileName - file to be read
     * @return String
     * @throws IOException 
     */
    private String readFile(String fileName){

        StringBuilder sb = new StringBuilder();
        // Read the file
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            if(br.ready()){
                while((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                    SplitWords(line);
                }
            }
         }
        catch(IOException ioe){
           System.out.println("Could not read file: " + ioe);
        }
        return sb.toString();
    }
    
     /***
     * Loops over a string splitting it on period, comma, and whitespace into words.
     * @param tempLine - the string to be split into the words
     */
    public void SplitWords(String tempLine){
        // Split the string to find words
        String[] strArray = tempLine.split("[,|/|(|\\s]+"); 
        for (String tempString : strArray) {
            // Strip unwanted leading and trailing chars
            tempString = tempString.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
            if(!tempString.isEmpty()){
                StoreWords(tempString.toLowerCase());
            }
        }

    }
    
    /***
    * Checks the HashMap for the key and adds an entry into the hashmap if appropriate.
    * @param wordKey - the string to be stored as the key
    */
    public void StoreWords(String wordKey){
        if(!wordMap.containsKey(wordKey)){
            //System.out.println("Inserting New Word " + wordKey);
            wordMap.put(wordKey, 1);
        }        
        else{
            int currValue = wordMap.get(wordKey);
            wordMap.replace(wordKey, currValue += 1);
            //System.out.println("Key " + wordKey + " already exists.\nCurrent value = " + currValue
            //        + " updated value = " + wordMap.get(wordKey) + "\n");      
        }
    }

    /***
    * Write the data to the console and a file.
    * @param sortedList - a sorted list containing an String, Integer entry
    */
    public void PrintData(List<Entry<String,Integer>> sortedList){
        // Create the file to print the data too
        File outFile = new File("./out/result.txt");
        if (CreateDirectory(outFile.getParentFile())){
            // Print the console header
            System.out.printf("%-20s | %s\n\n" , "Word", "No of occurrences of that word" );
            
            try(BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(outFile))) {
               String str = padRight("Word", 21) + "|" + padLeft("No of occurrences of that word", 31); 
               bufferWriter.write(str + "\n");
                for(Entry<String, Integer> listEntry: sortedList){
                    //Generate a formatted string
                    str = padRight(listEntry.getKey(), 21) + "|" + padLeft(listEntry.getValue().toString(), 10); 
                    // Write the data to the console
                    System.out.println(str);
                    //Write data to a file
                    bufferWriter.write(str + "\n");
                }
            }
            catch (IOException ioe) {
                System.out.println("Could not write file: " + ioe);
            }
        }    
    }
    
    /***
     * Sort the HasMap data
     * @return List - a sorted list
     */
    public List SortData(){
        // Convert the map to a list
        List<Entry<String,Integer>> sortedEntries = new ArrayList<>(wordMap.entrySet());
        // Sort the list using a comparator
        Collections.sort(sortedEntries, new Comparator<Entry<String,Integer>>() {
                @Override
                public int compare(Entry<String,Integer> e1, Entry<String,Integer> e2) {
                    return e2.getValue().compareTo(e1.getValue());
                }
            }
        );
        return sortedEntries;
    }   
    
    /*** 
     * Check to see if the driector exist and create it if needed
     * @param theDir - the directory to create if needed
     * @return boolean - ture if the directory exists or was created
     */
    public boolean CreateDirectory(File theDir){
        boolean result = false;
        // If the directory does not exist, create it
        if (!theDir.exists()) {
            try{
                theDir.mkdir();
                result = true;
            } 
            catch(SecurityException se){
                System.out.println("The directory couldn't be created: " + se);
            }
        }
        else if(theDir.exists()){
            result = true;            
        }
        return result;
    }
 
    /*** 
     * Format the string with padding on the left
     * @param stringValue - the string to pad
     * @param intPadding - the amount of padding
     * @return String - the formated string
     */    
    public String padLeft(String stringValue, int intPadding) {
        return String.format("%1$" + intPadding + "s", stringValue);  
    }
    
    /*** 
     * Format the string with padding on the right
     * @param stringWord - the string to pad
     * @param intPadding - the amount of padding
     * @return String - the formated string
     */    
    public String padRight(String stringWord, int intPadding) {
        return String.format("%1$-" + intPadding + "s", stringWord);  
    }
    
    public static void main (String[] args) {
        
        FileIO tempFileIO = new FileIO();
        String sampleFile = tempFileIO.readFile("sample.txt");
        tempFileIO.PrintData(tempFileIO.SortData());
    }
}
