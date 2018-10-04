
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Write a program to accept a List of Strings and output a list of all the Strings 
 * with length less than 8 characters. Change the case to all upper case, and 
 * display it Sorted (Using the Java Lambdas and Streams API).
 * 
 * @author rfelts
 */

public class LambdaStreamExample {
    
    private List<String> processStrings(List<String> paramStringList){
        
        return paramStringList.stream().filter(stringValue -> stringValue.length() < 8)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
        
    }
    
    public static void main(String [] args){
    
        List<String> stringList = Arrays.asList("Sid", "Marty", "Pony", "Babby", "Xerces", 
            "Dupo", "Pody" , "Mango", "lexi", "Manny", "Formand" , "Draco", 
            "Sequan","Sore hofh");
        
        
        LambdaStreamExample test = new LambdaStreamExample();
        System.out.println(test.processStrings(stringList));
    }
}
