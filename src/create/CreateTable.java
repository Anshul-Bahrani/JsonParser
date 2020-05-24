/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package create;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sunil
 */
// Responsible to add files named by user as the name of the table in databse with a timstamp so at the time of migration
// the order of table creation is maintained.
public class CreateTable {
    public CreateTable(String name) {
        String path = System.getProperty("user.dir");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        name = name + "_" + timestamp.getTime() + ".json";
        //return number of milliseconds since January 1, 1970, 00:00:00 GMT
        System.out.println(timestamp.getTime());
        path = path + "\\src\\database\\migrations\\" + name;
        File temp = new File(path);
        try {
            if (temp.createNewFile()) {
                System.out.println("File Created");
            }
            else {
                System.out.println("Error While Creating The File");
            }
        } catch (IOException ex) {
            Logger.getLogger(CreateTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(path);
    }
}
