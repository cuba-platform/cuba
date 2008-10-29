/*
 * Author: Konstantin Krivopustov
 * Date: 20.10.2008 21:04:56
 */
package com.haulmont.cuba.core;

import com.haulmont.chile.core.ChileCore;

public class CubaCore {

    public static void main(String[] args) {
        System.out.println("Hello from CubaCore");
        new ChileCore();
    }

    public String getName() {
        return "CubaCore";
    }
}
