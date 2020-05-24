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

import jsonparser.JsonObject;
/**
 *
 * @author Sunil
 */
public class JsonParser {
    public JsonObject jsonObject;
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
    public JsonParser(String path) throws IOException {
        String content = "";
        try {
            // Since i have only test.json i have hardcoded the path to the file in the repository
            // it will be changed in the future verison.
            content = new String ( Files.readAllBytes(Paths.get(path) ) );

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
//         After opening the file, just pass the contents to the json object.
        this.jsonObject = new JsonObject(content, 0);
        System.out.println(jsonObject.obj());
//         if the json contains an array, the Object o should be of the type ArrayList, 
//         according to the contents of the json file, the class of 'o' will be different.
        System.out.println((jsonObject.o));
    }
//    private 
}