/*
 * Author: Konstantin Krivopustov
 * Created: 16.05.2009 0:15:48
 * 
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Service interface to UniqueNumbers MBean
 */
public interface UniqueNumbersService
{
    String NAME = "cuba_UniqueNumbersService";

    long getNextNumber(String domain);
}
