/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainapp;

import database.DataBaseHelper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsonparser.JsonParser;

/**
 *
 * @author Sunil
 */
// This acts as a init as well as a dependency injector.
public class Initializer {
    private DataBaseHelper dbhelper;
    public Initializer() {
        // Determining the path for conf.json
        String path = System.getProperty("user.dir");
        path = path + "\\conf.json";
        LinkedHashMap contents = new LinkedHashMap();
        try {
            
            try {
                // Parsing the conf.json file.
                JsonParser json = new JsonParser(path);
                contents = (LinkedHashMap) json.jsonObject.o;
            } catch (IOException ex) {
           Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(contents.keySet());
    }
        // incase the format of config file is not a linkedhashmap as reuired the user gets an error.
    catch(ClassCastException ex) {
            System.out.println("Config file Format Error");
            System.exit(0);
        }
        // passing the dbname, username, port and other contents of config file to dbhelper.
    this.dbhelper = new DataBaseHelper(contents);
    }
    public DataBaseHelper getDbhelper() {
        return dbhelper;
    }
    
}
