/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 29.05.2010 13:30:49
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

public interface ShortcutAction extends Action {

    KeyCombination getKeyCombination();

    class KeyCombination {
        private final Key key;
        private final Modifier[] modifiers;

        public KeyCombination(Key key, Modifier... modifiers) {
            if (key == null && modifiers == null) {
                throw new IllegalArgumentException("Combination is empty");
            }
            this.key = key;
            this.modifiers = modifiers;
        }

        public Key getKey() {
            return key;
        }

        public Modifier[] getModifiers() {
            return modifiers;
        }
    }

    enum Key {
        ENTER(13),
        ESCAPE(27),
        PAGE_UP(33),
        PAGE_DOWN(34),
        TAB(9),
        ARROW_LEFT(37),
        ARROW_UP(38),
        ARROW_RIGHT(39),
        ARROW_DOWN(40),
        BACKSPACE(8),
        DELETE(46),
        INSERT(45),
        END(35),
        HOME(36),
        F1(112),
        F2(113),
        F3(114),
        F4(115),
        F5(116),
        F6(117),
        F7(118),
        F8(119),
        F9(120),
        F10(121),
        F11(122),
        F12(123),
        A(65),
        B(66),
        C(67),
        D(68),
        E(69),
        F(70),
        G(71),
        H(72),
        I(73),
        J(74),
        K(75),
        L(76),
        M(77),
        N(78),
        O(79),
        P(80),
        Q(81),
        R(82),
        S(83),
        T(84),
        U(85),
        V(86),
        W(87),
        X(88),
        Y(89),
        Z(90),
        NUM0(48),
        NUM1(49),
        NUM2(50),
        NUM3(51),
        NUM4(52),
        NUM5(53),
        NUM6(54),
        NUM7(55),
        NUM8(56),
        NUM9(57),
        SPACEBAR(32);

        private int code;

        Key(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    enum Modifier {
        SHIFT(16),
        CTRL(17),
        ALT(18);

        private int code;

        Modifier(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static int[] codes(Modifier... modifiers) {
            if (modifiers == null) {
                return new int[0];
            }
            int[] codes = new int[modifiers.length];
            for (int i = 0; i < modifiers.length; i++) {
                codes[i] = modifiers[i].code;
            }
            return codes;
        }
    }

}
