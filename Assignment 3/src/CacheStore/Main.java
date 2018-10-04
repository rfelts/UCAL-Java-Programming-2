/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheStore;

import java.util.ArrayList;

/**
 *
 * @author rfelts
 */
public class Main {
    
    public static void main(String args []){
        
        System.out.println("Checking String Keys");

        CacheStore<String,String> stringCache = new CacheStore<>();
        
        for(int iLoop = 0; iLoop < 10; iLoop++){
            String sKey = "Key" + iLoop;
            String sValue = "Value " + iLoop;
            stringCache.insertObj(sKey, sValue);
        }
        
        stringCache.insertObj("Key1", "Duplicate");
        
        System.out.println("Retrieving object: " + 
                stringCache.retrieveObj("Key9"));
        
        System.out.println("Retrieving and deleting object: " + 
                stringCache.retrieveDeleteObject(("Key2")));
        
        System.out.println("Retrieving object: " + 
                stringCache.retrieveObj("Key2"));
        
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add("Key0");
        keyList.add("Key1");
        keyList.add("Key0");
        keyList.add("Key3");
        keyList.add("Key4");
        keyList.add("Key5");
        
        ArrayList<String> valueList = stringCache.getValues(keyList);
        System.out.println("\nGetting list of values:");
        for(String value: valueList){
            System.out.println("Value = " + value);
        }
        
        System.out.println("\nClearing the map.");
        stringCache.invalidate();
        
        ArrayList<String> valueList2 = stringCache.getValues(keyList);
        System.out.println("\nGetting list of values:");
        for(String value: valueList2){
            System.out.println("Value = " + value);
        }        

        System.out.println("\nChecking Interger Keys");
        CacheStore<Integer,String> intCache = new CacheStore<>();
        
        for(int iLoop = 0; iLoop < 10; iLoop++){
            Integer iKey = iLoop;
            String sValue = "Value " + iLoop;
            intCache.insertObj(iKey, sValue);
        }
        
        Integer newInt = new Integer(88);
        intCache.insertObj(newInt, "Eighty Eight");
        
        Integer newInt5 = new Integer(5);
        intCache.insertObj(newInt5, "Duplicate");
        
        System.out.println("Retrieving object: " + 
                intCache.retrieveObj(1));
        
        System.out.println("Retrieving and deleting object: " + 
                intCache.retrieveDeleteObject((2)));
        Integer newInt2 = new Integer(2);
        System.out.println("Retrieving object: " + 
                intCache.retrieveObj(newInt2));
        
        ArrayList<Integer> intKeyList = new ArrayList<>();
        intKeyList.add(6);
        intKeyList.add(2);
        intKeyList.add(4);
        intKeyList.add(5);
        intKeyList.add(88);
        intKeyList.add(9);
        
        ArrayList<String> intValueList = intCache.getValues(intKeyList);
        System.out.println("\nGetting list of values:");
        for(String value: intValueList){
            System.out.println("Value = " + value);
        }
        
        System.out.println("\nClearing the map.");
        intCache.invalidate();
        
        ArrayList<String> intValueList2 = intCache.getValues(intKeyList);
        System.out.println("\nGetting list of values:");
        for(String value: intValueList2){
            System.out.println("Value = " + value);
        }        

    }          
    
}
