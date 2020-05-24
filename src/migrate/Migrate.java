/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migrate;

import database.DataBaseHelper;
import database.Utils;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsonparser.JsonParser;
import mainapp.Initializer;

/**
 *
 * @author Sunil
 */
// responsible for validating and calling dbhelper to create tables.
public class Migrate {
    private DataBaseHelper dbhelper;
    // needs initializer class to get the dbhelper's instance.
    public Migrate(Initializer init) {
        this.dbhelper = init.getDbhelper();
        // defining the path to migrations folder.
        String path = System.getProperty("user.dir");
        path = path + "\\src\\database\\migrations";
        File dir = new File(path);
        String [] temp = dir.list();
//        for(String tempo : temp) {
//            Utils.print(tempo);
//        }
        // this method will sort the names of all the files according to the timestamp of the file.
        String [] sorted = Utils.sortTimestamp(temp);
        for(String file : sorted) {
            String newpath = path + "\\" + file;
            try {
                //openining each file and extracting the 'contents' key and value pair and 'constraints' key value pair.
                // no need of providing the table name as it will be the same name as the name of the folder.
                
                JsonParser parsed = new JsonParser(newpath);
                LinkedHashMap contents = (LinkedHashMap) parsed.jsonObject.o;
                LinkedHashMap columns = null, constraints = null;
                if(contents.get("columns") != null) {
                    columns = (LinkedHashMap) contents.get("columns");
                }
                else {
                    System.out.println("Columns not found in file " + file);
                    System.exit(0);
                }
                if(contents.get("constraints") != null) {
                    constraints = (LinkedHashMap) contents.get("constraints");
                }
                String filename = file.substring(0, file.length() - 19);
                Utils.print(filename);
                Utils.print(columns);
                Utils.print(constraints);
                // this is the main method which will generate the query and execute it on the database.
                this.dbhelper.create(filename, columns, constraints);
                
            } catch (IOException ex) {
                Logger.getLogger(Migrate.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(ClassCastException ex) {
                System.err.println("Format error of file " + file + " , check the format provided in test.json and try again");
                System.exit(0);
            } 
        }
    }
}
