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
// This the main class where the app starts.
public class Main {
    public static void main(String args[]) {
        // the create table line is purposely commented because i ran it one time and created the test file in database migrations
        // and thus using that as test.
//        CreateTable temp = new CreateTable("test");
// Purpose of initializer and migrate were explained in the READ.md file on git.
        Initializer init = new Initializer();
        Migrate when = new Migrate(init);
    }
}
