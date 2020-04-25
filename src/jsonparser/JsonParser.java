/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonparser;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Sunil
 */
public class JsonParser {
//    String content = "";
    /**
     * @param args the command line arguments
     */
//    public JsonParser(String path) {
//        try {
//            this.content = new String ( Files.readAllBytes(Paths.get(path + "\\src\\jsonparser\\test.json") ) );
//        } catch (IOException ex) {
//            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    // This is a Test class to test the JsonObject class,
    // I have used test.json in which i tried different json files using examples from the net.
    // In the future i plan to pass this class the path of the file itself and then this class can open the file
    // and pass the contents of the file to the JsonObject class.
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir");
        String content = "";
        System.out.println(System.getProperty("user.dir"));
        try {
            // Since i have only test.json i have hardcoded the path to the file in the repository
            // it will be changed in the future verison.
            Scanner scanner = new Scanner(new File(path + "\\src\\jsonparser\\test.json"));
            content = new String ( Files.readAllBytes(Paths.get(path + "\\src\\jsonparser\\test.json") ) );

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        // After opening the file, just pass the contents to the json object.
        JsonObject jo = new JsonObject(content, 0);
        // if the json contains an array, the Object o should be of the type ArrayList, 
        // according to the contents of the json file, the class of 'o' will be different.
        System.out.println((jo.o));
    }
//    private 
}

class JsonObject {
    int whichline = 0;
    public Object o;
    // The extra whichline argument is a modification that i have left for the future to print the line in which the
    // error ocurred for the convinience of the user to debug the json for errors, ofcourse there are online tools which
    // which do this in a more efficient and better way but this will just give the user an idea.
    JsonObject (String content, int whichline) {
        this.whichline = whichline;
        // checkvalue is the heart of the class, it checks in which format the content of json file is and converts 
        // that content to a suitable java class. More about the function is described just above the function.
        // And it throws an InvalidJsonException which is my class defined at the end of the file.
        try {
            this.o = this.checkvalue(content);
//            System.out.println("Finallyy:" + temp.getClass().getSimpleName());
        } catch (InvalidJsonException ex) {
            System.out.println(ex);
        }
    }
    public String toString(String content) {
        return ((this.o).toString());
    }
    // This method is just the alternative of split() method of String class in java
    // Why it was needed and how it works is described in README.md.
    private ArrayList stringyarr(String str, String splitter) throws InvalidJsonException{
        char split = splitter.charAt(0);
        ArrayList<String> arr = new ArrayList<>();
        boolean flag = false;
        Stack<String> st = new Stack<>();
        int last = -1;
        int i = 0;
        str = str.trim();
//        System.out.println("Splitter: "+split);
        for(i = 0;i < str.length(); i ++) {
            if (str.charAt(i) == '[' && !flag) {
                st.push("[");
            }
            if (str.charAt(i) == ']' && !flag) {
                if(st.peek() == "[") {
                    st.pop();
                }
                else {
                    throw new InvalidJsonException("Invalid Data Type");
                }
            }
            if (str.charAt(i) == '{' && !flag) {
                st.push("{");
            }
            if (str.charAt(i) == '}' && !flag) {
                if(st.peek() == "{") {
                    st.pop();
                }
                else {
                    throw new InvalidJsonException("Invalid Data Type");
                }
            }
            if (str.charAt(i) == split && !flag && st.empty()) {
                arr.add(str.substring(last + 1, i));
                last = i;
            }
            if(str.charAt(i) == '"' && !flag) {
                flag = true;
            }
            else{
                if(str.charAt(i) == '"' && flag) {
                    flag = false;
                }
            }
        }
        if ((i - 1) != last) {
            arr.add(str.substring(last + 1, i));
        }
        else {
            throw new InvalidJsonException("Unknown value found after ,");
        }
//        System.out.println("Array elems:"+arr);
        return  arr;
    }
    // This class is just a nested if-else structure to determine what json type the content is and return the
    // respective converted class for the same.
    private Object checkvalue(String value) throws InvalidJsonException{
        value = value.trim();
        if (isitnum(value)) {
                    return makenum(value);
                }
                else {
                    if (isitbool(value)) {
                        return makebool(value);
                    }
                    else {
                        if (isitnull(value)) {
                            return null;
                        }
                        else {
                            if (isitarr(value)) {
                                return makearr(value);
                            }
                            else {
                                if (isitstr(value)) {
                                    value = value.substring(1, value.length() - 1);;
                                    return value;
                                }
                                else {
                                    if (isitobj(value)) {
                                        return makeobj(value);
                                    }
                                    else {
                          // If the content is none of the above class, then it is surely an error in the syntax of json.
                                        System.out.println(value);
                                        throw new InvalidJsonException("Invalid Data Type");
                                    }
                                }
                            }
                        }
                    }
                }
    }
    // Following are the methods to check the datatype of content of Strings passed to it.
    // More about it in the README.md file.
    private boolean isitnum(String val) {
        try {
            float i = Float.parseFloat(val);
        }catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
    private boolean isitbool(String val) {
        if (val.equals("true") || val.equals("false")) {
            return true;
        }
        return false;
    }
    private boolean isitnull(String val) {
        if (val.equals("null")) {
            return true;
        }
        return false;
    }
    private boolean isitarr(String val) {
        if (val.charAt(0) == '[' && val.charAt(val.length() - 1) == ']') {
            return true;
        }
        return false;
    }
    private boolean isitstr(String val) {
        if (val.charAt(0) == '"' && val.charAt(val.length() - 1) == '"') {
            return true;
        }
        return false;
    }
    private boolean isitobj(String val) {
        if (val.charAt(0) == '{' && val.charAt(val.length() - 1) == '}') {
            return true;
        }
        return false;
    }
    // When we found the data type of the file, these methods are used to convert it and return the converted object
    // of the respective class, More about it in the README.md file.
    private float makenum(String val) {
        return Float.parseFloat(val);
    }
    private boolean makebool(String val) {
        if (val.equals("true")) {
            return true;
        }
        return false;
    }
    private ArrayList makearr(String val) {
        ArrayList arr = new ArrayList<>();
        val = val.substring(1, val.length() - 1);
        val = val.trim();
//        print("Inside arr:" + val);
        ArrayList<String> strs;
        try {
            strs = stringyarr(val, ",");
//            print(strs);
            for(String str : strs) {
                str = str.trim();
                arr.add(checkvalue(str));
            }
        } catch (InvalidJsonException ex) {
//            print(ex);
        }
//        System.out.println("Printing for the last time:" + arr);
        return arr;
    }
        private LinkedHashMap makeobj(String content){
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
            ArrayList<String> items = stringyarr(toconvert, ",");
            for(String elem : items) {
                elem = elem.trim();
//                print(elem);
                ArrayList<String> keyvalue = stringyarr(elem, ":");
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
                Object temp = checkvalue(value);
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
    // This methos is just a handy way to print something.
    private void print(Object o) {
        System.out.println(o);
    }
}
// This class is used to throw an Exception along with some information about why the exception was thrown.
class InvalidJsonException extends Exception {
    InvalidJsonException(String err) {
        super("Exception Ocurred at: " + err + " ,Unknown DataType.");
    }
}