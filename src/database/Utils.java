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
    public static String find(LinkedHashMap target, String name) throws InvalidConfigException {
        if(target.get(name) != null) {
            return (String) target.get(name);
        }
        else {
            throw new InvalidConfigException(name);
        }
    }
    
    
    public static void print(Object o) {
        System.out.println(o);
    }
    
    
    public static String[] sortTimestamp(String [] files) {
        for(int i = 0; i<files.length-1; i++) {
            Utils.print(files[i].substring(files[i].length() - 18, files[i].length() - 5));
         for (int j = i+1; j<files.length; j++) {
             
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
    
    
    public static boolean validateForJson(String name) {
        if (name.length() >= 5) {
            if(name.substring(name.length() - 5, name.length()).equals(".json")) {
                return true;
            }
        }
        return false;
    }
}
