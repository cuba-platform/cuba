/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:10:48
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface UniqueNumbersMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=UniqueNumbers";

    UniqueNumbersAPI getAPI();

    long getCurrentNumber(String domain);

    void setCurrentNumber(String domain, long value);
}
