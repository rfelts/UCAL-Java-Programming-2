/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public interface Cachable<K>{
    // Verifies the object used for the key does not already exist in the map
    public boolean cacheKey(K key);    
}

/**
 * Implement an in memory cache store where objects can be stored on a temporary 
 * basis using appropriate collection classes and generics. 
 * Provide a contract cacheKey allowing the cache users to create a key based on 
 * attributes of user's choice, this will create a problem of collision (two keys 
 * possibly having the same hash), provide a solution to this problem. Values 
 * can be objects of any type.
 * Submit a single java file with all the methods using Collections and generics. 
 * I should be able to write a main method to use your class and call each method 
 * with my choice of key and value. 
 * @author rfelts
 * @param <T> Represents the generic object used for the key
 * @param <V> Represents the generic object used for the value
 */
class CacheStore<T,V> implements Cachable{

    Map<T,V> cacheStore = new HashMap<>();

    // Adds an object to the HashMap
    public void insertObj(T key, V value){
        if(!cacheKey(key)){
            System.out.println("Inserting Object");
            cacheStore.put(key, value);
        }        
        else{
            System.out.println("Key already exists.\n" + 
                    cacheStore.keySet() + "\n");
        }
    }
    
    // Returns an object for the HashMap based on the key provided
    public V retrieveObj(T key){
        return cacheStore.get(key);
    }

    // Returns an object for the HashMap based on the key provided
    public V retrieveDeleteObject(T key){
        return cacheStore.remove(key);
    }
    
    // Clears the HashMap
    public void invalidate(){
        cacheStore.clear();
    }
    
    // Returns an arrayList of objects for the HashMap
    public ArrayList<V> getValues(ArrayList<T> keyList){
        ArrayList<V> valueList = new ArrayList <>();
        for(T key: keyList){
            valueList.add(retrieveObj(key));
            //System.out.println("Getting key: " + key);
        }
        return valueList;
    }

    @Override
    public boolean cacheKey(Object key) {
        System.out.println("In cacheKey");
        return cacheStore.containsKey(key);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.cacheStore);
        System.out.println("HashCode called: " + hash);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.hashCode() == obj.hashCode()) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass().hashCode() != obj.getClass().hashCode()) {
            return false;
        }
        final CacheStore<?, ?> other = (CacheStore<?, ?>) obj;
        return true;
    } 
    
}