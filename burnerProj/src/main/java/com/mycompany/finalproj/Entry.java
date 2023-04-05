/** *************************************
 * Project: Entry class
 * Programmer: Aris Ariawan
 * Date: April, 2023
 * Program: Entry.java
 ************************************** */
package com.mycompany.finalproj;

/**
 *
 * @author arisariawan
 */
public class Entry extends Object {
    
    //this class allows for any type of object to be stored in type 'Entry'
    private Object value;
    
    public Entry(Object value){
        this.value = value;
    }
    
    public Object getValue(){
        return value;
    }
    
}
