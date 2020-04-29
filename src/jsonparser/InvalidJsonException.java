/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonparser;

/**
 *
 * @author Sunil
 */
// This class is used to throw an Exception along with some information about why the exception was thrown.
public class InvalidJsonException extends Exception {
    InvalidJsonException(String err) {
        super("Exception Ocurred at: " + err + " ,Unknown DataType.");
    }
}
