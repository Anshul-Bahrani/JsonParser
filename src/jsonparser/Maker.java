/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import jsonparser.Utils;
import jsonparser.Checker;
/**
 *
 * @author Sunil
 */
public class Maker {
    // When we found the data type of the file, these methods are used to convert it and return the converted object
    // of the respective class, More about it in the README.md file.
    public static float makenum(String val) {
        return Float.parseFloat(val);
    }
    public static boolean makebool(String val) {
        if (val.equals("true")) {
            return true;
        }
        return false;
    }
    public static ArrayList makearr(String val) {
        ArrayList arr = new ArrayList<>();
        val = val.substring(1, val.length() - 1);
        val = val.trim();
//        print("Inside arr:" + val);
        ArrayList<String> strs;
        try {
            strs = Utils.stringyarr(val, ",");
//            print(strs);
            for(String str : strs) {
                str = str.trim();
                arr.add(Checker.checkvalue(str));
            }
        } catch (InvalidJsonException ex) {
//            print(ex);
        }
//        System.out.println("Printing for the last time:" + arr);
        return arr;
    }
        public  static LinkedHashMap makeobj(String content){
        LinkedHashMap elems = new LinkedHashMap<>();
        content = content.trim();
//        System.out.println("Testing My Class:");
//        System.out.println(content);
        String toconvert = content;
        try {
//            if(toconvert.charAt(0) != '{') { 
//                throw new InvalidJsonException("Line 1");
//            }
//            if(toconvert.charAt(toconvert.length() - 1) != '}') {
//                ArrayList lines = stringyarr(toconvert, "\n");
//                throw new InvalidJsonException("Line " + lines.size());
//            }
            toconvert = toconvert.substring(1, toconvert.length() - 1);
            toconvert = toconvert.trim();
//            print("Final elems: " + toconvert);
            ArrayList<String> items = Utils.stringyarr(toconvert, ",");
            for(String elem : items) {
                elem = elem.trim();
//                print(elem);
                ArrayList<String> keyvalue = Utils.stringyarr(elem, ":");
                if(keyvalue.size() != 2) {
                    throw new InvalidJsonException("Invalid json");
                }
                String key = keyvalue.get(0);
                key = key.trim();
                if(key.charAt(0) != '"' || key.charAt(key.length() - 1) != '"') {
                    throw new InvalidJsonException("Non-Stringy key found");
                }
                key = key.substring(1, key.length() - 1);
//                print("Key:" + key + " Value:" + keyvalue.get(1));
                String value = keyvalue.get(1);
                value = value.trim();
//                this.elems.put(key, value);
                Object temp = Checker.checkvalue(value);
                elems.put(key, temp);
                
            }
//            System.out.println("Moment of truth:");
//            System.out.println(elems);
//            Collection c = elems.values();
//            Iterator it = c.iterator();
//            while (it.hasNext()) {
//                Object o = it.next();
//                if (o != null)
//                        {
//                System.out.println(o + " " + o.getClass());
//                }
//            }
        }catch(InvalidJsonException e) {
            System.err.println(e);
        }
        return elems;
    }
}
