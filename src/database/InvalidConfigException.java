/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author Sunil
 */
public class InvalidConfigException extends Exception{
    public InvalidConfigException(String who) {
        super(who);
    }
}
