
 /*
  * Copyright (c) 2008-2018 Haulmont.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

 package com.haulmont.cuba.testmodel.setget;

 import com.haulmont.chile.core.annotations.MetaClass;
 import com.haulmont.cuba.core.entity.StandardEntity;

 import javax.persistence.Transient;
 import java.util.Map;

 @MetaClass(name = "test$SetGetEntity")
 public class SetGetEntity<T> extends StandardEntity {

     @Transient
     private Map<String, Integer> map;

     @Transient
     private int[] intArray;

     @Transient
     private StandardEntity[] standardEntityArray;

     @Transient
     private T genericField;

     @Transient
     private T[] genericArray;

     @Transient
     private Map<T, Integer> genericMap;

     @Transient
     private byte byteField;

     @Transient
     private short shortField;

     @Transient
     private char charField;

     @Transient
     private int intField;

     @Transient
     private long longField;

     @Transient
     private float floatField;

     @Transient
     private double doubleField;

     @Transient
     private boolean booleanField;

     public Map<String, Integer> getMap() {
         return map;
     }

     public void setMap(Map<String, Integer> map) {
         this.map = map;
     }

     public int[] getIntArray() {
         return intArray;
     }

     public void setIntArray(int[] intArray) {
         this.intArray = intArray;
     }

     public StandardEntity[] getStandardEntityArray() {
         return standardEntityArray;
     }

     public void setStandardEntityArray(StandardEntity[] standardEntityArray) {
         this.standardEntityArray = standardEntityArray;
     }

     public T getGenericField() {
         return genericField;
     }

     public void setGenericField(T genericField) {
         this.genericField = genericField;
     }

     public T[] getGenericArray() {
         return genericArray;
     }

     public void setGenericArray(T[] genericArray) {
         this.genericArray = genericArray;
     }

     public Map<T, Integer> getGenericMap() {
         return genericMap;
     }

     public void setGenericMap(Map<T, Integer> genericMap) {
         this.genericMap = genericMap;
     }

     public int getIntField() {
         return intField;
     }

     public void setIntField(int intField) {
         this.intField = intField;
     }

     public byte getByteField() {
         return byteField;
     }

     public void setByteField(byte byteField) {
         this.byteField = byteField;
     }

     public short getShortField() {
         return shortField;
     }

     public void setShortField(short shortField) {
         this.shortField = shortField;
     }

     public char getCharField() {
         return charField;
     }

     public void setCharField(char charField) {
         this.charField = charField;
     }

     public long getLongField() {
         return longField;
     }

     public void setLongField(long longField) {
         this.longField = longField;
     }

     public float getFloatField() {
         return floatField;
     }

     public void setFloatField(float floatField) {
         this.floatField = floatField;
     }

     public double getDoubleField() {
         return doubleField;
     }

     public void setDoubleField(double doubleField) {
         this.doubleField = doubleField;
     }

     public boolean isBooleanField() {
         return booleanField;
     }

     public void setBooleanField(boolean booleanField) {
         this.booleanField = booleanField;
     }
 }