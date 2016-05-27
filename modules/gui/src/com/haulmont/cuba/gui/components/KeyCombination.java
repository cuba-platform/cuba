/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.gui.components;

import com.google.common.base.Objects;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;

import java.util.Arrays;

public class KeyCombination {
    private static final String SHORTCUT_PREFIX = "shortcut.";
    private static final Modifier[] EMPTY_MODIFIERS = new Modifier[0];

    private final Key key;
    private final Modifier[] modifiers;

    /**
     * Creates a new <code>KeyCombination</code> instance from a string representation.
     *
     * @param keyString string of type "Modifiers-Key", e.g. "Alt-N". Case-insensitive.
     * @return new instance
     */
    public static KeyCombination create(String keyString) {
        if (keyString == null) {
            return null;
        }
        keyString = keyString.toUpperCase();

        Key key;
        Modifier[] modifiers = null;

        if (keyString.contains("-")) {
            String[] keys = keyString.split("-", -1);

            int modifiersCnt = keys.length;

            //try {
            key = Key.valueOf(keys[modifiersCnt - 1]);
            --modifiersCnt;
            /*} catch (IllegalArgumentException e) {
                //ignore
            }*/
            modifiers = new Modifier[modifiersCnt];
            for (int i = 0; i < modifiersCnt; i++) {
                modifiers[i] = Modifier.valueOf(keys[i]);
            }
        } else {
            key = Key.valueOf(keyString);

        }
        return new KeyCombination(key, modifiers);
    }

    public String format() {
        Messages messages = AppBeans.get(Messages.NAME);
        StringBuilder sb = new StringBuilder();
        if (modifiers != null) {
            for (Modifier modifier : modifiers) {
                if (sb.length() > 0) {
                    sb.append("+");
                }
                sb.append(messages.getMessage(getClass(), SHORTCUT_PREFIX + modifier.name()));
            }
        }
        if (sb.length() > 0) {
            sb.append("+");
        }
        sb.append(messages.getMessage(getClass(), SHORTCUT_PREFIX + key.name()));
        return sb.toString();
    }

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

    public enum Key {
        ENTER(13, '\n'),
        ESCAPE(27),
        PAGE_UP(33),
        PAGE_DOWN(34),
        TAB(9, '\t'),
        ARROW_LEFT(37),
        ARROW_UP(38),
        ARROW_RIGHT(39),
        ARROW_DOWN(40),
        BACKSPACE(8),
        BACKSLASH(220, 0x5C),
        DELETE(46, 0x7F),
        INSERT(45, 0x9B),
        END(35),
        HOME(36),
        SPACEBAR(32),

        PLUS(107),
        MINUS(109),
        DIVIDE(111),
        MULTIPLY(106),

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

        KEY0(48),
        KEY1(49),
        KEY2(50),
        KEY3(51),
        KEY4(52),
        KEY5(53),
        KEY6(54),
        KEY7(55),
        KEY8(56),
        KEY9(57),

        NUM0(96),
        NUM1(97),
        NUM2(98),
        NUM3(99),
        NUM4(100),
        NUM5(101),
        NUM6(102),
        NUM7(103),
        NUM8(104),
        NUM9(105);

        private int code;
        private int virtualKey;

        Key(int code) {
            this.code = code;
            this.virtualKey = code;
        }

        Key(int code, int virtualKey) {
            this.code = code;
            this.virtualKey = virtualKey;
        }

        public int getCode() {
            return code;
        }

        public int getVirtualKey() {
            return virtualKey;
        }
    }

    public enum Modifier {
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

    public static int[] getShortcutModifiers(KeyCombination.Modifier[] modifiers) {
        if (modifiers == null) {
            return null;
        }
        int[] res = new int[modifiers.length];
        for (int i = 0; i < modifiers.length; i++) {
            res[i] = modifiers[i].getCode();
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        KeyCombination other = (KeyCombination) obj;

        KeyCombination.Modifier[] modifiers = this.modifiers != null ? this.modifiers : EMPTY_MODIFIERS;
        KeyCombination.Modifier[] otherModifiers = other.modifiers != null ? other.modifiers : EMPTY_MODIFIERS;

        return this.key == other.key && Arrays.equals(modifiers, otherModifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, modifiers);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("key", key)
                .add("modifiers", modifiers)
                .toString();
    }
}