///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            HashTableTest.java
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

import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*; 
import org.junit.jupiter.api.Assertions;
 
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
 
import java.util.Random;

/*
 * Contains a variety of tests meant to test the functional abilities of the code based
 * on the given ADT classes it inherited from. Attempts to test all areas
 */
public class HashTableTest{

    HashTable<Integer,Integer> hash1;
    HashTable<String, String> hash2;
    int hashCode;
    
    @Before
    public void setUp() throws Exception {
      hash1 = createInstance();
      hash2 = createInstance2();
    }

    @After
    public void tearDown() throws Exception {
      hash1 = null;
      hash2 = null;
    }
    
    protected HashTable<String, String> createInstance2() {
        return new HashTable<String,String>();
    }
    
    protected HashTable<Integer, Integer> createInstance() {
        return new HashTable<Integer,Integer>();
    }
    
    @Test
    public void test000_collision_scheme() {
        HashTableADT htIntegerKey = new HashTable<Integer,String>();
        int scheme = htIntegerKey.getCollisionResolution();
        if (scheme < 1 || scheme > 9) 
            fail("collision resolution must be indicated with 1-9");
    }
        
    @Test
    public void test001_insertIllegalNullKey() {
        try {
            HashTableADT htIntegerKey = new HashTable<Integer,String>();
            htIntegerKey.insert(null, null);
            fail("should not be able to insert null key");
        } 
        catch (IllegalNullKeyException e) { /* expected */ } 
        catch (Exception e) {
            fail("insert null key should not throw exception "+e.getClass().getName());
        }
    }
    
    @Test
    public void test002_insertOneCallGet() {
      try {
        Integer key = 12;
        hash1.insert(key, 18);
        if(hash1.get(key) != 18) {
          fail("Get call on inserted key did not return correct value");
        }
      }
      catch(Exception e) {
        fail("Insert should have worked but exception was thrown");
        e.printStackTrace();
      }
    }
    
    @Test
    public void test003_getCollisionResolution() {
      try {
        if(hash1.getCollisionResolution() != 5) {
          fail("Get collision resolution returned the incorrect collision resolution");
        }
      }
      catch(Exception e) {
        fail("Insert should have worked but exception was thrown");
        e.printStackTrace();
      }
    }
    
    @Test
    public void test004_getCapacity() {
      try {
        if(hash2.getCapacity() !=  5) {
          fail("Get capacity call returned the incorrect capacity");
        }
      }
      catch(Exception e) {
        fail("Get capacity call should have worked but exception was thrown");
        e.printStackTrace();
      }
    }
    
    @Test
    public void test005_getLoadFactor() {
      try {
        double loadFactorCompare = 0.2;
        hash2.insert("Hello", "yes");
        if(hash2.getLoadFactor() != loadFactorCompare) {
          fail("Calculated load factor was not correct");
        }
      }
      catch(Exception e) {
        fail("Load Factor get call resulted in exception thrown");
        e.printStackTrace();
      }
    }
    
    @Test
    public void test006_getLoadFactorThreshold() {
      try {
        if(hash1.getLoadFactorThreshold() != 0.75) {
          fail("Load Factor Threshold was the wrong value");
        }
      }
      catch(Exception e) {
        fail("Insert should have worked but exception was thrown");
        e.printStackTrace();
      }
    }
    
    @Test
    public void test007_getOnNoSuchElementWithElementsInTable() {
      try {
        hash1.insert(12, 20);
        hash1.insert(32, 50);
        
        hash1.get(20);
        fail("Get on a non-existent element should have thrown a KeyNotFoundException");
      }
      catch(KeyNotFoundException e) {
        
      }
      catch(Exception e) {
        fail("Insert should have worked but exception was thrown");
        e.printStackTrace();
      }
    }
    
    @Test
    public void test008_getNoElements() {
      try {
        hash1.get(-1);
        fail("Get on a non-existent element should have thrown a KeyNotFoundException");
      }
      catch(KeyNotFoundException e) {
        
      }
      catch(Exception e) {
        fail("Insert should have worked but exception was thrown");
        e.printStackTrace();
      }
    }
    
    @Test
    public void test009_insertDuplicateKeyOnArrayThatExpanded() {
      try {
        hash2.insert("hi", "cool");
        hash2.insert("yes", "nice");
        hash2.insert("dope", "fresh");
        hash2.insert("yay", "not");
        hash2.insert("yes", "nice");
        fail("DuplicateKeyException should have been thrown");
      }
      catch(DuplicateKeyException e) {
        
      }
      catch(Exception e) {
        fail("Exception was thrown but it wasn't a DuplicateKeyException");
      }
    }
    
    @Test
    public void test010_insertMultipleValuesOnNonDefaultConstructor() {
      try {
        HashTable<Integer,String> hashCustom = new HashTable<Integer,String>(3, 0.4);
        hashCustom.insert(12, "cool");
        hashCustom.insert(14, "fantastic");
        hashCustom.insert(19, "yay");
        hashCustom.insert(26, "right");
        hashCustom.insert(17, "good");
        if(hashCustom.get(12) != "cool" && hashCustom.get(14) != "fantastic" && hashCustom.get(19) != "yay" 
            && hashCustom.get(26) != "right" && hashCustom.get(17) != "good") {
          fail("Inserting multiple values and calling contains on non default constructor didn't find all values");
        }
      }
      catch(Exception e) {
        fail("Unexpected exception thrown during insert or get on custom constructor");
      }
    }
    
    @Test
    public void test011_removeNullKey() {
      try {
        hash2.remove(null);
        fail("Remove call on null key threw no exception");
      }
      catch(IllegalNullKeyException e) {
      }
      catch(Exception e) {
        fail("Attempt to remove a null key resulted in wrong exception thrown");
      }
    }
    
    @Test
    public void test012_insertRemoveGetOnNonDefaultConstructor() {
      try {
        HashTable<Integer,Integer> hashCustom = new HashTable<Integer,Integer>(2, 0.6);
        hashCustom.insert(12, 12);
        hashCustom.insert(14, 14);
        hashCustom.insert(19, 19);
        hashCustom.insert(26, 26);
        hashCustom.remove(14);
        hashCustom.insert(17, 17);
        hashCustom.get(14);
        fail("Get call on inserted then removed key did not throw KeyNotFoundException");
      }
      catch(KeyNotFoundException e) {
        
      }
      catch(Exception e) {
        fail("Unexpected exception thrown during insert or get on custom constructor");
      }
    }
    
    @Test
    public void test013_insertMultipleKeysCallNumKeys() {
      try {
        hash1.insert(82,12);
        hash1.insert(903,10);
        hash1.insert(572,8);
        hash1.insert(991223,4);
        if(hash1.numKeys() != 4) {
          fail("NumKeys did not correctly keep track of number of inserted keys");
        }
      }
      catch(Exception e) {
        fail("Insertion of multiple keys resulted in exception thrown");
      }
    }
    
    @Test
    public void test014_removeDecreasesNumKeys() {
      try {
        hash1.insert(83, 19);
        hash1.insert(12, 2);
        hash1.remove(83);
        if(hash1.numKeys() != 1) {
          fail("Removal of a key did not correctly result in decremented number of keys");
        }  
      }
      catch(Exception e) {
        fail("Insertion or removal caused unexpected exception");
      }
    }
    
    @Test
    public void test015_insertOnNonDefaulConstructorAndCallRemoveOnNonExistentKey() {
      try {
        HashTable<Integer,String> hashCustom = new HashTable<Integer,String>(3, 0.4);
        hashCustom.insert(12, "cool");
        hashCustom.insert(14, "fantastic");
        hashCustom.insert(19, "yay");
        hashCustom.insert(26, "right");
        hashCustom.insert(17, "good");
        if(hashCustom.remove(88) != false) {
          fail("Inserting multiple values and calling remove on non existent value didn't return false");
        }
      }
      catch(Exception e) {
        fail("Unexpected exception thrown during insert or get on custom constructor");
      }
    }
    
    @Test
    public void test016_getOnNullKey() {
      try {
        hash2.get(null);
        fail("Get call on null key didn't thrown IllegalNullKeyException");
      }
      catch(IllegalNullKeyException e) {
        
      }
      catch(Exception e) {
        fail("Get on null key threw the wrong exception type");
      }
    }
    
    @Test
    public void test017_multipleInsertsAndRemovalsThenGetCall() {
      try {
        hash1.insert(83, 40);
        hash1.insert(9, 8);
        hash1.insert(-20, 8);
        hash1.remove(9);
        hash1.insert(33, 12);
        hash1.insert(4000, 16);
        hash1.remove(4000);
        
        if(hash1.get(33) != 12 && hash1.get(-20) != 8 && hash1.get(83) != 40) {
          fail("Get called on values supposed to be in list failed");
        }
        
      }
      catch(Exception e) {
        fail("Unexpected exception thrown during multiple inserts, removals, and get calls");
      }
    }
    
    @Test
    public void test018_lotsOfInsertsAndRemovesCheckNumKeys() {
      try {
        hash1.insert(83, 40);
        hash1.insert(9, 8);
        hash1.insert(-20, 8);
        hash1.remove(9);
        hash1.insert(33, 12);
        hash1.insert(4000, 16);
        hash1.remove(4000);
        hash1.insert(87, 41);
        hash1.insert(92, 83);
        hash1.insert(-60, 9);
        hash1.remove(-20);
        hash1.insert(34, 13);
        hash1.insert(400500, 17);
        hash1.remove(400500);
        
        System.out.print(hash1.numKeys());
        if(hash1.numKeys() != 6) {
          fail("Numkeys was not the correct value after multiple inserts and removals");
        }
      }
      catch(Exception e) {
         fail("Inserts and removals caused unexpected exception to be thrown");
      }
    }
    
    @Test
    public void test019_removeReturnsTrueWhenCalledAndRemoves() {
      try {
        hash2.insert("jey", "yaaa");
        if(hash2.remove("jey") != true) {
          fail("Removal of key did not return true");
        }
      }
      catch(Exception e) {
        fail("Insert or remove threw an unexpected exception");
      }
    }
    
    @Test
    public void test020_insertsThenCallRemoveReturnsFalseOnNonExistent() {
      try {
        hash2.insert("yes", "no");
        if(hash2.remove("no") != false) {
          fail("Remove call on non-existent key didn't retun false");
        }
      }
      catch(Exception e) {
        fail("Insertion or removal threw an unexpected error");
      }
    }
    
}
