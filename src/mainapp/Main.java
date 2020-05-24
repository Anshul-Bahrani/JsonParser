/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainapp;

import create.CreateTable;
import mainapp.Initializer;
import migrate.Migrate;
import database.Utils;
/**
 *
 * @author Sunil
 */
public class Main {
    public static void main(String args[]) {
//        CreateTable temp = new CreateTable("test");
        Initializer init = new Initializer();
        Migrate when = new Migrate(init);
        Utils.print(Utils.validateForJson("temp.json"));
    }
}
