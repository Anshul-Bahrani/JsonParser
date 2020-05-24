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
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
    catch(ClassNotFoundException ex) {
        System.out.println("Error while loading drivers");
        System.exit(0);
        }
        try {
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
    
    public boolean create(String tablename, LinkedHashMap columns, LinkedHashMap constraints) {
        try {
            con.setAutoCommit(false);
            String stmt = "CREATE OR REPLACE TABLE " + tablename + " ( ";
            System.out.println();
            Iterator it = columns. entrySet(). iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                stmt = stmt + "`" + pair.getKey() + "` " + pair.getValue() + " , ";
            }
            Utils.print(stmt);
            HashMap<String, Integer> validconstraints = this.giveConstraints();
            int count = 0;
            
            if (constraints != null) {
                
                Iterator it1 = constraints. entrySet(). iterator();
                while(it1.hasNext()) {
                    
                    Map.Entry pair = (Map.Entry)it1.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    String temp = pair.getKey().toString().toUpperCase();
                    
                    if (validconstraints.get(temp) != null) {
                        ArrayList<Object> vals = (ArrayList<Object>) pair.getValue();
                        if (validconstraints.get(temp) == 0 && vals.size() > 0) {
                            stmt = stmt + "CONSTRAINT `" + temp.toLowerCase() + count + "` " + temp + " (";
                            count ++;
                            Utils.print(pair.getValue().getClass().getSimpleName());
                            for (int i = 0;i < vals.size(); i ++) {
                                String val = (String) vals.get(i);
                                stmt = stmt + val + ", ";
                            }
                            stmt = stmt.substring(0, stmt.length() - 2);
                            stmt = stmt + "), ";
                        }
                        if (validconstraints.get(temp) == 1) {
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
                                String reftable = (String) references.get(0);
                                String refcol = (String) references.get(1);
                                stmt = stmt + "CONSTRAINT `" + temp.toLowerCase() + count + "` " + temp.toUpperCase() + " (" + key + ") REFERENCES " + reftable + "(" + refcol + ") ,";
                                count ++;
                            }
                        }
                        if(validconstraints.get(temp) == 1) {
                            
                        }
                    }
                    else {
                        System.err.println("Invalid Constraint Found: " +  temp);
                        throw new SQLException();
                    }
                }
            }
            stmt = stmt.substring(0, stmt.length() - 2);
            stmt = stmt + ");";
            Utils.print(stmt);
            Statement st = con.createStatement();
            st.execute(stmt);
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
    public HashMap<String, Integer> giveConstraints() {
        HashMap<String, Integer> toreturn = new HashMap<>();
        toreturn.put("UNIQUE", 0);
        toreturn.put("PRIMARY KEY", 0);
        toreturn.put("FOREIGN KEY", 1);
        return toreturn;
    }
}
