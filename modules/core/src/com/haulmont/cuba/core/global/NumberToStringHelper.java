/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 28.05.2009 8:53:30
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import java.util.Map;
import java.util.HashMap;

public class NumberToStringHelper {

    public static String getStringNumber(Number number, String messagePackForUnit, String messagePackForDigit, int multiplier) {
        return numberToStr(number, multiplier, messagePackForUnit, messagePackForDigit);
    }

    private static String numberToStr(Number value, int multiplier, String messagePackForUnit, String messagePackForDigit) {

        StringBuilder stringNumber = new StringBuilder();
        Map<String, String> highUnitMap = initUnitMap(messagePackForUnit, "high");
        Map<String, String> lowUnitMap = initUnitMap(messagePackForUnit, "low");

        int triadNum = 0;
        int theTriad;

        int intPart = value.intValue();
        int fractPart = (int) Math.round((value.doubleValue() - intPart) * multiplier);
        if (intPart > 0) {
            do {
                theTriad = intPart % 1000;
                stringNumber.insert(0, triadToWord(theTriad, triadNum, highUnitMap.get("UnitSex"), messagePackForDigit));
                if (triadNum == 0) {
                    int range10 = (theTriad % 100) / 10;
                    int range = theTriad % 10;
                    if (range10 == 1) {
                        stringNumber = stringNumber.append(highUnitMap.get("UnitAC"));
                    } else {
                        switch (range) {
                            case 1:
                                stringNumber = stringNumber.append(highUnitMap.get("UnitNC"));
                                break;
                            case 2:
                            case 3:
                            case 4:
                                stringNumber = stringNumber.append(highUnitMap.get("UnitGC"));
                                break;
                            default:
                                stringNumber = stringNumber.append(highUnitMap.get("UnitAC"));
                                break;
                        }
                    }
                }
                intPart = intPart / 1000;
                triadNum++;
            } while (intPart != 0);
        }

        if (fractPart > 0) {
            stringNumber = stringNumber.append(" ").append(triadToWord(fractPart, 0, lowUnitMap.get("UnitSex"), messagePackForDigit));
            if ((fractPart % 10) == 1) {
                stringNumber = stringNumber.append(lowUnitMap.get("UnitNC"));
            } else {
                switch (fractPart % 10) {
                    case 1:
                        stringNumber = stringNumber.append(lowUnitMap.get("UnitNC"));
                        break;
                    case 2:
                    case 3:
                    case 4:
                        stringNumber = stringNumber.append(lowUnitMap.get("UnitGC"));
                        break;
                    default:
                        stringNumber = stringNumber.append(lowUnitMap.get("UnitAC"));
                        break;
                }
            }
        }
//		stringNumber.setCharAt(0, Character.toUpperCase(stringNumber.charAt (0)));
        return stringNumber.toString();
    }

    private static String triadToWord(int triad, int triadNum, String Sex, String messagePack) {
        StringBuffer triadWord = new StringBuffer(100);

        if (triad == 0) {
            return triadWord.toString();
        }

        int range = triad / 100;
        switch (range) {
            default:
                break;
            case 1:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred1") + " ");
                break;
            case 2:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred2") + " ");
                break;
            case 3:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred3") + " ");
                break;
            case 4:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred4") + " ");
                break;
            case 5:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred5") + " ");
                break;
            case 6:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred6") + " ");
                break;
            case 7:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred7") + " ");
                break;
            case 8:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred8") + " ");
                break;
            case 9:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "hundred9") + " ");
                break;
        }

        range = (triad % 100) / 10;
        switch (range) {
            default:
                break;
            case 2:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "ten2") + " ");
                break;
            case 3:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "ten3") + " ");
                break;
            case 4:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "ten4") + " ");
                break;
            case 5:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "ten5") + " ");
                break;
            case 6:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "ten6") + " ");
                break;
            case 7:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "ten7") + " ");
                break;
            case 8:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "ten8") + " ");
                break;
            case 9:
                triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "ten9") + " ");
                break;
        }

        int range10 = range;
        range = triad % 10;
        if (range10 == 1) {
            switch (range) {
                case 0:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen0") + " ");
                    break;
                case 1:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen1") + " ");
                    break;
                case 2:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen2") + " ");
                    break;
                case 3:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen3") + " ");
                    break;
                case 4:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen4") + " ");
                    break;
                case 5:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen5") + " ");
                    break;
                case 6:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen6") + " ");
                    break;
                case 7:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen7") + " ");
                    break;
                case 8:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen8") + " ");
                    break;
                case 9:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "afterTen9") + " ");
                    break;
            }
        } else {
            switch (range) {
                default:
                    break;
                case 1:
                    if (triadNum == 1)
                        triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit1F") + " ");
                    else if (Sex.equals("M")) triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit1M") + " ");
                    if (Sex.equals("F")) triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit1F") + " ");
                    break;
                case 2:
                    if (triadNum == 1)
                        triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit2F") + " ");
                    else if (Sex.equals("M")) triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit2M") + " ");
                    if (Sex.equals("F")) triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit2F") + " ");
                    break;
                case 3:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit3") + " ");
                    break;
                case 4:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit4") + " ");
                    break;
                case 5:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit5") + " ");
                    break;
                case 6:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit6") + " ");
                    break;
                case 7:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit7") + " ");
                    break;
                case 8:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit8") + " ");
                    break;
                case 9:
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "unit9") + " ");
                    break;
            }
        }

        switch (triadNum) {
            default:
                break;
            case 1:
                if (range10 == 1)
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "thousandAC") + " ");
                else {
                    switch (range) {
                        default:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "thousandAC") + " ");
                            break;
                        case 1:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "thousandNC") + " ");
                            break;
                        case 2:
                        case 3:
                        case 4:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "thousandGC") + " ");
                            break;
                    }
                }
                break;
            case 2:
                if (range10 == 1)
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "millionAC") + " ");
                else {
                    switch (range) {
                        default:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "millionAC") + " ");
                            break;
                        case 1:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "millionNC") + " ");
                            break;
                        case 2:
                        case 3:
                        case 4:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "millionGC") + " ");
                            break;
                    }
                }
                break;
            case 3:
                if (range10 == 1)
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "milliardAC") + " ");
                else {
                    switch (range) {
                        default:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "milliardAC") + " ");
                            break;
                        case 1:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "milliardNC") + " ");
                            break;
                        case 2:
                        case 3:
                        case 4:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "milliardGC") + " ");
                            break;
                    }
                }
                break;
            case 4:
                if (range10 == 1)
                    triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "trillionAC") + " ");
                else {
                    switch (range) {
                        default:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "trillionAC") + " ");
                            break;
                        case 1:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "trillionNC") + " ");
                            break;
                        case 2:
                        case 3:
                        case 4:
                            triadWord = triadWord.append(MessageProvider.getMessage(messagePack, "trillionGC") + " ");
                            break;
                    }
                }
                break;
        }
        return triadWord.toString();
    }

    private static Map<String, String> initUnitMap(String messagePackForUnit, String prefix) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("UnitNC", MessageProvider.getMessage(messagePackForUnit, prefix + "UnitNC"));
        map.put("UnitGC", MessageProvider.getMessage(messagePackForUnit, prefix + "UnitGC"));
        map.put("UnitAC", MessageProvider.getMessage(messagePackForUnit, prefix + "UnitAC"));
        map.put("UnitSex", MessageProvider.getMessage(messagePackForUnit, prefix + "UnitSex"));

        return map;
    }

    private static Map<String, String> initDigitThousandMap(String messagePack, String prefix) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("DigitNC", MessageProvider.getMessage(messagePack, prefix + "NC"));
        map.put("DigitGC", MessageProvider.getMessage(messagePack, prefix + "GC"));
        map.put("DigitAC", MessageProvider.getMessage(messagePack, prefix + "AC"));

        return map;
    }

    private static Map<String, String> initDigitHundredMap(String messagePack, String prefix) {
        Map<String, String> map = new HashMap<String, String>();

        for (int i = 0; i < 10; i++) {
            String s = MessageProvider.getMessage(messagePack, prefix + i);
            if (s.contains(prefix) && (i == 1 || i == 2)) {
                s = MessageProvider.getMessage(messagePack, prefix + i + "F");
                map.put(prefix + i + "F", s);
                s = MessageProvider.getMessage(messagePack, prefix + i + "M");
                map.put(prefix + i + "M", s);
                continue;
            }
            map.put(prefix + i, s);
        }

        return map;
    }
}
