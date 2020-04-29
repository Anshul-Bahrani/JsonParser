/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonparser;

import jsonparser.Maker;
import jsonparser.InvalidJsonException;
/**
 *
 * @author Sunil
 */
public class Checker {
        // This class is just a nested if-else structure to determine what json type the content is and return the
    // respective converted class for the same.
    public static Object checkvalue(String value) throws InvalidJsonException{
        value = value.trim();
        if (isnum(value)) {
            return Maker.makenum(value);
        }
        else if (isbool(value)) {
                return Maker.makebool(value);
        }
        else if (isnull(value)) {
                return null;
        }
        else if (isarr(value)) {
                return Maker.makearr(value);
        }
        else if (isstr(value)) {
                value = value.substring(1, value.length() - 1);;
                return value;
        }
        else if (isobj(value)) {
                return Maker.makeobj(value);
        }
        else 
            // If the content is none of the above class, then it is surely an error in the syntax of json.
            System.out.println(value);
            throw new InvalidJsonException("Invalid Data Type");
    }
    // Following are the methods to check the datatype of content of Strings passed to it.
    // More about it in the README.md file.
    public static boolean isnum(String val) {
        try {
            float i = Float.parseFloat(val);
        }catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
    public static boolean isbool(String val) {
        if (val.equals("true") || val.equals("false")) {
            return true;
        }
        return false;
    }
    public static boolean isnull(String val) {
        if (val.equals("null")) {
            return true;
        }
        return false;
    }
    public static boolean isarr(String val) {
        if (val.charAt(0) == '[' && val.charAt(val.length() - 1) == ']') {
            return true;
        }
        return false;
    }
    public static boolean isstr(String val) {
        if (val.charAt(0) == '"' && val.charAt(val.length() - 1) == '"') {
            return true;
        }
        return false;
    }
    public static boolean isobj(String val) {
        if (val.charAt(0) == '{' && val.charAt(val.length() - 1) == '}') {
            return true;
        }
        return false;
    }
}
