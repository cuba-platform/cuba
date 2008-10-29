/*
 * Author: Konstantin Krivopustov
 * Date: 20.10.2008 21:04:56
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.CubaCore;

public class CubaWeb {

    public static void main(String[] args) {
        System.out.println("Hello from CubaWeb");
        new CubaCore();
    }

    public String getName() {
        return "CubaWeb";
    }
}
