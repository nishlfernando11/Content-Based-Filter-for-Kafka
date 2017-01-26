package com.filter;

import java.lang.String;
import java.lang.System;

/**
 * Created with IntelliJ IDEA.
 * User: Irosha
 * Date: 9/15/16
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleContentConsumerTest {

    public static void main(String[] args){
        System.out.println("Testing...");
        System.out.println(args.length);

        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
