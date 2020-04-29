/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonparser;

import java.util.ArrayList;
import java.util.Stack;
import jsonparser.InvalidJsonException;
/**
 *
 * @author Sunil
 */
public class Utils {
    // This method is just the alternative of split() method of String class in java
    // Why it was needed and how it works is described in README.md.
    public static ArrayList stringyarr(String str, String splitter) throws InvalidJsonException{
        char split = splitter.charAt(0);
        ArrayList<String> arr = new ArrayList<>();
        boolean flag = false;
        Stack<String> st = new Stack<>();
        int last = -1;
        int i = 0;
        str = str.trim();
//        System.out.println("Splitter: "+split);
        for(i = 0;i < str.length(); i ++) {
            if (str.charAt(i) == '[' && !flag) {
                st.push("[");
            }
            if (str.charAt(i) == ']' && !flag) {
                if(st.peek() == "[") {
                    st.pop();
                }
                else {
                    throw new InvalidJsonException("Invalid Data Type");
                }
            }
            if (str.charAt(i) == '{' && !flag) {
                st.push("{");
            }
            if (str.charAt(i) == '}' && !flag) {
                if(st.peek() == "{") {
                    st.pop();
                }
                else {
                    throw new InvalidJsonException("Invalid Data Type");
                }
            }
            if (str.charAt(i) == split && !flag && st.empty()) {
                arr.add(str.substring(last + 1, i));
                last = i;
            }
            if(str.charAt(i) == '"' && !flag) {
                flag = true;
            }
            else{
                if(str.charAt(i) == '"' && flag) {
                    flag = false;
                }
            }
        }
        if ((i - 1) != last) {
            arr.add(str.substring(last + 1, i));
        }
        else {
            throw new InvalidJsonException("Unknown value found after ,");
        }
//        System.out.println("Array elems:"+arr);
        return  arr;
    }
}
