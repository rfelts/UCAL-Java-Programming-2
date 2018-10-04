/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import  java.util.Objects;
/**
 *
 * @author rfelts
 */
public class StringQuestion1 {
    public static void main(String[] args) {
        String str1 = "Hello World !" ;
        System.out.println("This is str1.");
        System.out.println(str1.getClass().getName() + " @" + Integer.toHexString(System.identityHashCode(str1)));
        System.out.println(str1.getClass().getName() + " @" + Integer.toHexString(str1.hashCode()));

        String str2 = new String("Hello World !") ;
        System.out.println("This is str2.");
        System.out.println(str2.getClass().getName() + " @" + Integer.toHexString(System.identityHashCode(str2)));
        System.out.println(str2.getClass().getName() + " @" + Integer.toHexString(str2.hashCode()));

        String str3 = "Hello World !" ;
        System.out.println("This is str3.");
        System.out.println(str3.getClass().getName() + " @" + Integer.toHexString(System.identityHashCode(str3)));
        System.out.println(str3.getClass().getName() + " @" + Integer.toHexString(str3.hashCode()));

        String str4 = "Hello world !" ;
        System.out.println("This is str4.");
        System.out.println(str4.getClass().getName() + " @" + Integer.toHexString(System.identityHashCode(str4)));
        System.out.println(str4.getClass().getName() + " @" + Integer.toHexString(str4.hashCode()));

        StringBuffer str5 = new StringBuffer("Hello World !") ;
        //System.out.println(str5.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(str5)));

        String str6 = str2 ;
        //System.out.println(str6.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(str6)));
        
        str6 = str2+str4;
       // System.out.println(str6.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(str6)));
        
        //String str2=str4;
        str6=null;
        //System.out.println(str6.getClass().getName() + " @" + Integer.toHexString(System.identityHashCode(str6)));
        
        System.out.println("str1 is " + str1 + "\nstr2 is " + str2 + "\nstr3 is " + str3 + "\nstr4 is " + str4);

    }
    
}
