/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonparser;

import jsonparser.Checker;
/**
 *
 * @author Sunil
 */
public class JsonObject {
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

    
    public String obj(){
        return this.o.getClass().getSimpleName();
    }
    


    // This method is just a handy way to print something.
    private void print(Object o) {
        System.out.println(o);
    }
}