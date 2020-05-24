/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.LinkedHashMap;


import database.InvalidConfigException;
import java.util.ArrayList;
import java.util.Arrays;
/**
 *
 * @author Sunil
 */
public class Utils {
    // finding the key 'name' in the linkedhashmap 'target'(used in the dbhelper's constructor).
    public static String find(LinkedHashMap target, String name) throws InvalidConfigException {
        if(target.get(name) != null) {
            return (String) target.get(name);
        }
        else {
            // if the key s not found, throw an exception so that the user gets to know which property was not found
            // in the config file.
            throw new InvalidConfigException(name);
        }
    }
    
    
    public static void print(Object o) {
        System.out.println(o);
    }
    
    // just a nrmall bubble sort method comparing the time stamps of the files.
    public static String[] sortTimestamp(String [] files) {
        for(int i = 0; i<files.length-1; i++) {
//            Utils.print(files[i].substring(files[i].length() - 18, files[i].length() - 5));
         for (int j = i+1; j<files.length; j++) {
             // since timestamp is 13 chars long the substring comparison is lenth - 18 to lenth - 5(excluding the
             // .json extension.
            if(files[i].substring(files[i].length() - 18, files[i].length() - 5).compareTo(files[j].substring(files[i].length() - 18, files[i].length() - 5))>0) {
               String temp = files[i];
               files[i] = files[j];
               files[j] = temp;
            }
         }
      }
      System.out.println(Arrays.toString(files));
        return files;
    }
    
    // checking the last 5 chars of the file to be .json.
    public static boolean validateForJson(String name) {
        if (name.length() >= 5) {
            if(name.substring(name.length() - 5, name.length()).equals(".json")) {
                return true;
            }
        }
        return false;
    }
}
