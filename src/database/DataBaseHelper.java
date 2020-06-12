/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsonparser.JsonParser;
import java.sql.*;


import database.Utils;
import database.InvalidConfigException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 *
 * @author Sunil
 */
public class DataBaseHelper {
    private String dbname;
    private String port = "3306";
    private String username;
    private String password;
    private Connection con;
    
    public DataBaseHelper(LinkedHashMap contents) {
        // Loading the required drivers.
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
    catch(ClassNotFoundException ex) {
        System.out.println("Error while loading drivers");
        System.exit(0);
        }
        try {
            // extracting the db details from the contents of config file.
            // if a certain property is not found, the user gets an error explaining the same.
            this.dbname = Utils.find(contents, "dbname");
            this.port = Utils.find(contents, "port");
            this.username = Utils.find(contents, "username");
            this.password = Utils.find(contents, "password");
        }
        catch(InvalidConfigException ex) {
            System.out.println(ex);
            System.exit(0);
        }
        Utils.print(this.dbname + this.password + this.port + this.username);
        try { 
            //establishing a connection with the database.
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:" + this.port + "/" + this.dbname,this.username,this.password);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
//        Statement stmt=con.createStatement();  
//        ResultSet rs=stmt.executeQuery("select * from users");  
//        while(rs.next())  
//        System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
//        con.close();  
    }
    
    
    // main method for creation of tables in the database.
    public boolean create(String tablename, LinkedHashMap columns, LinkedHashMap constraints) {
        try {
            // autocommit false because this is the start of the transaction.
            // the reason behind creating a transaction is because of constraints like
            // NOT NULL, DEFAULT i thought of creating multiple ALTER TABLE queries, 
            // thus inall the whole set of queries should be commited at the end so there is no half commit ments.
            con.setAutoCommit(false);
            String stmt = "CREATE OR REPLACE TABLE " + tablename + " ( ";
            Iterator it = columns. entrySet(). iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                stmt = stmt + "`" + pair.getKey() + "` " + pair.getValue() + " , ";
            }
            Utils.print(stmt);
            stmt = stmt.substring(0, stmt.length() - 2);
            stmt = stmt + ");";
            Utils.print(stmt);
            Statement st = con.createStatement();
            st.execute(stmt);
            stmt = "";
            // decided to create a separate method so that all the constarints i am providing until now are visible clearly.
            HashMap<String, Integer> validconstraints = this.giveConstraints();
            int count = 0;
            // not necessary that everytime a table needs constraints.
            if (constraints != null) {
                
                Iterator it1 = constraints. entrySet(). iterator();
                while(it1.hasNext()) { 
                    Map.Entry pair = (Map.Entry)it1.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    String temp = pair.getKey().toString().toUpperCase();
                    // if the name of the constriant is not something i proide, then throw an exception indicating the same.
                    if (validconstraints.get(temp) != null) {
                        ArrayList<Object> vals;
                        // for unique and primary key.
                        // for example refer the test.json file in the migrations folder
                        // this will be the partial query generated for unique constraint:
                        //  ALTER TABLE test ADD CONSTRAINT `unique1` UNIQUE (id);
                        //ALTER TABLE test ADD CONSTRAINT `unique2` UNIQUE (name);
                        // and this for primary key:
                        //  ALTER TABLE test ADD CONSTRAINT `primary key3` PRIMARY KEY (id);
                        // count is maintained to give a unique constarint name for every constraint.
                        if (validconstraints.get(temp) == 0) {
                            vals = (ArrayList<Object>) pair.getValue();
                            Utils.print(pair.getValue().getClass().getSimpleName());
                            for (int i = 0;i < vals.size(); i ++) {
                                count ++;
                                stmt = "ALTER TABLE " + tablename + " ADD ";
                                String val = (String) vals.get(i);
                                stmt = stmt + "CONSTRAINT `" + temp.toLowerCase() + count + "` " + temp + " (" + val + ");";
//                                stmt = stmt + val + ", ";
                                Utils.print(stmt);
                                st = con.createStatement();
                                st.execute(stmt);
                                
                            }
//                            stmt = stmt.substring(0, stmt.length() - 2);
//                            stmt = stmt + "), ";
                        }
                        // for the same example the foreign key constraint is :
                        // ALTER TABLE test ADD CONSTRAINT `foreign key3` FOREIGN KEY (id) REFERENCES temp(id);
                        // ALTER TABLE test ADD CONSTRAINT `foreign key4` FOREIGN KEY (tempid) REFERENCES temp(id);
                        if (validconstraints.get(temp) == 1) {
                            vals = (ArrayList<Object>) pair.getValue();
                            Utils.print("forr" + pair.getValue().getClass().getSimpleName());
                            for (int i = 0;i < vals.size(); i ++) {
                                LinkedHashMap val = (LinkedHashMap) vals.get(i);
                                Utils.print(vals.get(i));
                                if (val.size() != 1) {
                                    System.err.println("Invalid Foreign key constraint, please refer test for example");
                                    throw new SQLException();
                                }
                                Iterator keyval = val.entrySet().iterator();
                                Utils.print(val.keySet());
                                Map.Entry keyvalpair = (Map.Entry)keyval.next();
                                String key = (String) keyvalpair.getKey();
                                ArrayList<Object> references = (ArrayList<Object>) keyvalpair.getValue();
                                if (references.size() != 2) {
                                    System.err.println("Invalid Foreign key constraint, please refer test for example");
                                    throw new SQLException();
                                }
                                stmt = "ALTER TABLE " + tablename + " ADD ";
                                String reftable = (String) references.get(0);
                                String refcol = (String) references.get(1);
                                stmt = stmt + "CONSTRAINT `" + temp.toLowerCase() + count + "` " + temp.toUpperCase() + " (" + key + ") REFERENCES " + reftable + "(" + refcol + ");";
                                count ++;
                                Utils.print(stmt);
                                st = con.createStatement();
                                st.execute(stmt);
                            }
                        }
                        // this is for not null constraint
                        // output of eg. test file is:
                        // ALTER TABLE test MODIFY id int NOT NULL;
                        //ALTER TABLE test MODIFY name varchar(255) NOT NULL;
                        // now i need to search the whole key value pairs of column to get the datatype of the key provided
                        // for default constraint.
                        // after doing that only the string or varchar datatypes needed a single quotes.
                        if(validconstraints.get(temp) == 2) {
                            vals = (ArrayList<Object>) pair.getValue();
                            Utils.print("forr        " + pair.getValue().getClass().getSimpleName());
                            for (int i = 0;i < vals.size(); i ++) {
                                count ++;
                                String val = (String) vals.get(i);
                                it = columns. entrySet(). iterator();
                                String datatype = "";
                                while(it.hasNext()) {
                                    pair = (Map.Entry)it.next();
                                    if(pair.getKey().equals(val)) {
                                        datatype = pair.getValue().toString();
                                    }
                                }
                                if(datatype.equals("")) {
                                    Utils.print("Invalid key found in NOT NULL constraint: " + val);
                                    throw new SQLException();
                                }
                                stmt = "ALTER TABLE " + tablename + " MODIFY ";
                                stmt = stmt + val + " "+ datatype + " " + temp + ";";
                                Utils.print(stmt);
                                st = con.createStatement();
                                st.execute(stmt);
                                
                            }
                        }
                        // This is for the default value 
                        // output for eg. test file is:
                        // ALTER TABLE test ALTER name SET  DEFAULT 'unknown'
                        //ALTER TABLE test ALTER id SET  DEFAULT 5.0
                        if(validconstraints.get(temp) == 3) {
                            LinkedHashMap valls = (LinkedHashMap<Object, Object>) pair.getValue();
                            Utils.print("forr        " + pair.getValue().getClass().getSimpleName());
                            Iterator itt = valls. entrySet(). iterator();
                            while(itt.hasNext()) {
                                pair = (Map.Entry)itt.next();
                                Utils.print(pair.getValue().getClass().getSimpleName());
                                if(pair.getValue().getClass().getSimpleName().equals("String")) {
                                    stmt = "ALTER TABLE " + tablename + " ALTER " + pair.getKey() + " SET  "+ temp + " '" + pair.getValue() + "'";
                                }
                                else {
                                    stmt = "ALTER TABLE " + tablename + " ALTER " + pair.getKey() + " SET  "+ temp + " " + pair.getValue();
                                }
                                
                                Utils.print(stmt);
                                st = con.createStatement();
                                st.execute(stmt);
                            }
                        }
                        //this is for index and unique index 
                        // output for eg. test file is
                        // CREATE UNIQUE INDEX index7 ON test (id ,name);
                        // output will be same for index as well as unique index
                        if(validconstraints.get(temp) == 4) {
                            vals = (ArrayList<Object>) pair.getValue();
                            stmt = "CREATE INDEX index_name\n" + "ON table_name (column1, column2, ...);";
                            stmt = "CREATE " + temp + " " + "index" + count + " ON " + tablename + " ("; 
                            for (int i = 0;i < vals.size(); i ++) {
                                count ++;
                                String val = (String) vals.get(i);
                                stmt = stmt + val + " ,";
                                
                            }
                            stmt = stmt.substring(0, stmt.length() - 2);
                            stmt = stmt + ");";
                            Utils.print(stmt);
                            st = con.createStatement();
                            st.execute(stmt);
                        }
                        // This is for check constriant
                        // eg of test file 
                        // ALTER TABLE test ADD CONSTRAINT check10 CHECK (id<=500 AND name!= 'Anshul');
                        // This will be the output after the following if....
                        // now this is not the best made check constraint but i thought hard and found some ways but i dont know if they were right or no.
                        if(validconstraints.get(temp) == 5) {
                            count++;
                            String valss = (String) pair.getValue();
                            stmt = "ALTER TABLE " + tablename + " ADD CONSTRAINT " + temp.toLowerCase() + count + " " + temp + " (" + valss + ");";
                            Utils.print(stmt);
                            st = con.createStatement();
                            st.execute(stmt);
                        }
                    }
                    else {
                        System.err.println("Invalid Constraint Found: " +  temp);
                        throw new SQLException();
                    }
                }
            }
//            stmt = stmt.substring(0, stmt.length() - 2);
//            stmt = stmt + ";";
//            Utils.print(stmt);
            //
            //
            // final query generated
            // CREATE OR REPLACE TABLE test ( `id` int , `name` varchar(255) , `tempid` int , CONSTRAINT `unique0` UNIQUE (id, name), CONSTRAINT `primary key1` PRIMARY KEY (id), CONSTRAINT `foreign key2` FOREIGN KEY (id) REFERENCES temp(id) ,CONSTRAINT `foreign key3` FOREIGN KEY (tempid) REFERENCES temp(id));
            // for adding NOT NULL and DEFAULT constraints i have thought of generating ALTER TABLE queries to do so 
            // and have doubts that is this the right way to go.
            //
            //
//            st = con.createStatement();
//            st.execute(stmt);
            con.commit();
        }catch(SQLException ex) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex1) {
                System.out.println("Error While rolling back");
            }
            System.out.println("Error while creating the table");
            return false;
        }catch(ClassCastException ex) {
            System.out.println("Format Error " + ex);
            System.exit(0);
        }
        try {
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Table " + tablename + " Created Successfully");
        return true;
    }
    public void close() {
        try {
            this.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // In a huge doubt that the way which i have provided the methods are correct or no, so waiting until i get a green flag
    // on these.
    // having codes such as 0 for unique and primary key as they have same syntax and thus using the same for loop to generate the syntax.
    public HashMap<String, Integer> giveConstraints() {
        HashMap<String, Integer> toreturn = new HashMap<>();
        toreturn.put("UNIQUE", 0);
        toreturn.put("PRIMARY KEY", 0);
        toreturn.put("FOREIGN KEY", 1);
        toreturn.put("NOT NULL", 2);
        toreturn.put("DEFAULT", 3);
        toreturn.put("INDEX", 4);
        toreturn.put("UNIQUE INDEX", 4);
        toreturn.put("CHECK", 5);
        return toreturn;
    }
}
