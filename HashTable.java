///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            HashTable.java
// Files:            HashTableADT.java, HashTable.java, DataStructureADT.java
// Semester:         CS400 Spring Semester 2019
//
// Author:           Michael Connor Craney
// Email:            mcraney2@wisc.edu
// CS Login:         Craney
//
///////////////////////////CREDIT OUTSIDE HELP/////////////////////////////////
//                   
// Persons:          N/A
//
// Online sources:   N/A
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.ArrayList;
import java.lang.Math;

/*
 * This class implements a Hash Table using an Array of ArrayLists and the chained buckets resolution
 * for collisions.
 * 
 * @author Michael Connor Craney
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {
  
   /*
    * This private helper class pairs the key and value together so they can be stored as a pair
    * in the hash table
    * 
    * @author Michael Connor Craney
    */
    private class P {
      K key;
      V value;
    
      private P(K key, V value) {
        this.key = key;
        this.value = value;
      }
    }
	
	// Holds the total number of keys in the program
    private int numKeys;
    // Holds temporary key values for rehashing purposes
    private K tempKey;
    private V tempVal;
    // Hash Table
    private ArrayList<P>[] table;
    // Used to tell when rehashing needs to occur
    private double loadFactorThreshold;
    private double loadFactor;
    // Tells the total capacity of the array
    private int capacity;
    // Holds the hashInt when a key is given
    private int hashInt;
    // Holds old capacity
    private int oldCapacity;
   	
	/*
	 * Acts as the default constructor for the Hash Table
	 */
	public HashTable() {
	  this.numKeys = 0;
	  this.loadFactorThreshold = 0.75;
	  this.capacity = 5;
	  this.table = (ArrayList<P>[])new ArrayList<?>[capacity];
	}
	
	/*
	 * Acts as the non-default constructor for when rehashing must occur and a larger table size is needed
	 * 
	 * @param initialCapacity - the number of spots in the array for Hash IDs
	 * @param loadFactorThreshold - the threshold value for the load factor at which you will resize the array
	 */
	public HashTable(int initialCapacity, double loadFactorThreshold) {
	  this.numKeys = 0;
	  this.capacity = initialCapacity;
	  this.loadFactorThreshold = loadFactorThreshold;
	  this.table = (ArrayList<P>[])new ArrayList<?>[capacity];
	  
	}

	//////////////////////////
	// HashTable ADT Methods//
	//////////////////////////
	
	/* 
	 * Returns the collision resolution scheme used for this hash table.
     * Implement with one of the following collision resolution strategies.
     * Define this method to return an integer to indicate which strategy.
     *
     * 1 OPEN ADDRESSING: linear probe
     * 2 OPEN ADDRESSING: quadratic probe
     * 3 OPEN ADDRESSING: double hashing
     * 4 CHAINED BUCKET: array of arrays 
     * 5 CHAINED BUCKET(My Implementation): array of linked nodes
     * 6 CHAINED BUCKET: array of search trees
     * 7 CHAINED BUCKET: linked nodes of arrays
     * 8 CHAINED BUCKET: linked nodes of linked node
     * 9 CHAINED BUCKET: linked nodes of search trees
     * 
     * @return the value of the collision resolution
	 */
    public int getCollisionResolution() {
      return 5;
    }
	
	/* 
	 * Return the current Capacity (table size)
     * of the hash table array.
     *
     * The initial capacity must be a positive integer, 1 or greater
     * and is specified in the constructor.
     * 
     * Once increased, the capacity never decreases
     * 
     * @return the current capacity
     */
    public int getCapacity() {
      return capacity;
    }
    
    /*
     *  Calculates and returns the current load factor for this hash table
     *  
     *  @return the current load factors
     */ 
    public double getLoadFactor() {
      loadFactor = ((double)numKeys)/((double)capacity);
      return loadFactor;
    }
    
    /*
     * Returns the load factor threshold that was 
     * passed into the constructor when creating 
     * the instance of the HashTable.
     * When the current load factor is greater than or 
     * equal to the specified load factor threshold,
     * the table is resized and elements are rehashed.
     * 
     * @return load factor threshold value
    */
    public double getLoadFactorThreshold() {
      return loadFactorThreshold;
    }
    
    /////////////////////////////
    // DataStructureADT Methods//
    /////////////////////////////
    
    /*
     * Add the key,value pair to the data structure and increase the number of keys
     *
     * If key is null, throw IllegalNullKeyException;
     * If key is already in data structure, throw DuplicateKeyException();
     * 
     * @param key - the key to insert into the hash table
     * @param value - the value to insert into the hash table
     */
    public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
      // Check for a null key
      if(key == null) {
        throw new IllegalNullKeyException();
      }
      
      // Pair the key and value and generate the hash value so the pair can be placed in the array
      P pair = new P(key, value);
      hashInt = key.hashCode();
      hashInt = hashInt % capacity;
      
      // Check for negative hashInt
      if(hashInt < 0) {
        hashInt = Math.abs(hashInt);
      }
      
      // If the location is null, no values are stored there, create a new ArrayList which will act
      // as a hash bucket, adds the pair and sets the index of the array to the bucket
      if(table[hashInt] == null) {
        ArrayList<P> hashBucket = new ArrayList<P>();
        hashBucket.add(pair);
        numKeys++;
        table[hashInt] = hashBucket;
      }
      
      // Else, check for a duplicate key, if none is found, insert at the end of the list
      else {
        // Checks for duplicate
        for(int i = 0; i < table[hashInt].size(); i++) {
          if(table[hashInt].get(i).key.equals(key)) {
            throw new DuplicateKeyException();
          }
        }
        
        // If exception isn't thrown, duplicate key wasn't found and key is inserted
        table[hashInt].add(pair);
        numKeys++;
      }
      
      // Check if rehashing needs to occur due to exceeding threshold load factor
      if(getLoadFactor() >= getLoadFactorThreshold()) {
        // Change the capacity for the rehash
        oldCapacity = capacity;
        capacity = (capacity * 2) + 1;
        
        // Make  another HashTable instance to rehash the values with
        HashTable<K,V> rehashTable = new HashTable<K,V>(capacity, getLoadFactorThreshold());
        
        for(int i = 0; i < oldCapacity; ++i) {
          
          if(table[i] != null) {
            
            // Loop through bucketChain and insert into new arraylist
            for(int j = 0; j < table[i].size(); ++j) {
              tempKey = table[i].get(j).key;
              tempVal = table[i].get(j).value;
              rehashTable.insert(tempKey, tempVal);
            }
          }
        }
        
        // Set the rehashed table to the hash table
        table = rehashTable.table;
        
      }
      
      
    }
    
    /*
     * If key is found, remove the key,value pair from the data structure and decrease number of keys, return true
     * If key is null, throw IllegalNullKeyException
     * If key is not found, return false
     * 
     * @param key - the key to remove from the hash table
     * @return true if key was found and removed, false it if wasn't
     */
    public boolean remove(K key) throws IllegalNullKeyException {
      // Check for attempt at removal of a null key
      if(key == null) {
        throw new IllegalNullKeyException();
      }
      
      // Calculates the index the key to remove would be at through hash code
      hashInt = key.hashCode();
      hashInt = hashInt % capacity;
      
      // Check for negative hashInt
      if(hashInt < 0) {
        hashInt = Math.abs(hashInt);
      }
      
      // If the index is null, no values are stored there, return false
      if(table[hashInt] == null) {
        return false;
      }
      
      // Step through the bucket and check for the value to remove, removing if found
      else {
        for(int i = 0; i < table[hashInt].size(); ++i) {
          if(table[hashInt].get(i).key.equals(key)) {
            table[hashInt].remove(i);
            numKeys--;
            return true;
          }
        }
      }
      
      // If stepping through the hashbucket at the hashInt doesn't return true the key was not found
      return false;
      
    }
    
    /*
     * Returns the value associated with the specified key
     * Does not remove key or decrease number of keys
     *
     * If key is null, throw IllegalNullKeyException 
     * If key is not found, throw KeyNotFoundException()
     * 
     * @param the key to retrieve the value for
     * @return the value of the associated key if it is found
     */
    public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
      // Check for get call on a null key
      if(key == null) {
        throw new IllegalNullKeyException();
      }
      
      // Compute the hashInt of the key given
      hashInt = key.hashCode();
      hashInt = hashInt % capacity;
      
      // Check for negative hashInt
      if(hashInt < 0) {
        hashInt = Math.abs(hashInt);
      }
      
      // Check if the hashInt corresponds to a null spot in table array, throwing
      // a KeyNotFoundException if it's null
      if(table[hashInt] == null) {
        throw new KeyNotFoundException();
      }
      
      // Looks through array list at index hashInt for key, returning value if found 
      else {
        for(int i = 0; i < table[hashInt].size(); ++i) {
            if(table[hashInt].get(i).key.equals(key)) {
              return table[hashInt].get(i).value;
            }
        }
      }
      
      // If step through ArrayList didn't find key, throw KeyNotFoundException
      throw new KeyNotFoundException();
      
     }
    
    /*
     * Returns the number of key,value pairs in the data structure
     * 
     * @return numKeys - the number of keys in the data structure
     */
    public int numKeys() {
      return numKeys;
    }
    
}
