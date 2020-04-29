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

import jsonparser.Checker;
import jsonparser.InvalidJsonException;
import jsonparser.Maker;
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
//     This is a Test class to test the JsonObject class,
//     I have used test.json in which i tried different json files using examples from the net.
//     In the future i plan to pass this class the path of the file itself and then this class can open the file
//     and pass the contents of the file to the JsonObject class.
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
//         After opening the file, just pass the contents to the json object.
        JsonObject jo = new JsonObject(content, 0);
//         if the json contains an array, the Object o should be of the type ArrayList, 
//         according to the contents of the json file, the class of 'o' will be different.
        System.out.println((jo.o));
        ArrayList ar = (ArrayList)(jo.o);
        System.out.println(ar.get(0));
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
            this.o = Checker.checkvalue(content);
//            System.out.println("Finallyy:" + temp.getClass().getSimpleName());
        } catch (InvalidJsonException ex) {
            System.out.println(ex);
        }
    }
    public String toString(String content) {
        return ((this.o).toString());
    }

    
    
    


    // This methos is just a handy way to print something.
    private void print(Object o) {
        System.out.println(o);
    }
}