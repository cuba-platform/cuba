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

package spec.cuba.web.serverlogviewer

import com.haulmont.cuba.web.app.ui.serverlogviewer.ServerLogWindow
import spock.lang.Specification

class ServerLogPatternsTest extends Specification {

    def "highlight lowered attention by patterns"() {
        given: "open server log window"
        def serverLog = new ServerLogWindow()

        when: "line not from stack trace includes pattern's value"
        def line = "myLine consists of some-symbols"
        def pattern = ".*some-symbol."
        def changedLine = serverLog.highlightLoweredAttention(line, pattern)

        then:
        changedLine == "<span class='c-log-lowered-attention'>" + line + "</span>"

        when: "line from stack trace includes pattern's value"
        line = "2018-07-20 12:36:27.955 DEBUG [http-nio-8080-exec-7] " +
                "com.haulmont.cuba.gui.theme.ThemeConstantsRepository - Loading theme constants"
        pattern = "http-nio-8080-exec-\\d*"
        changedLine = serverLog.highlightLoweredAttention(line, pattern)

        then:
        changedLine == "<span class='c-log-lowered-attention'>" + line + "</span>"

        when: "line from stack trace doesn't include pattern's value"
        line = "2018-07-20 12:36:27.955 DEBUG [http-nio-8080-exec-77] " +
                "com.haulmont.cuba.gui.theme.ThemeConstantsRepository - Loading theme constants"
        pattern = "http-nio-8080-exec-\\D"

        changedLine = serverLog.highlightLoweredAttention(line, pattern)

        then:
        changedLine != "<span class='c-log-lowered-attention'>" + line + "</span>"

        when: "line from stack trace with complex pattern"
        line = "at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_171]"
        pattern = ".*at.*sun.reflect.NativeMethodAccessorImpl"

        changedLine = serverLog.highlightLoweredAttention(line, pattern)

        then:
        changedLine == "<span class='c-log-lowered-attention'>" + line + "</span>"

        when: "line from stack trace with specific symbols with complex pattern"
        line = "&nbsp;&nbsp;&nbsp;&nbsp;at&nbsp;sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_171]"
        pattern = ".*at.*sun.reflect.NativeMethodAccessorImpl"

        changedLine = serverLog.highlightLoweredAttention(line, pattern)

        then:
        changedLine == "<span class='c-log-lowered-attention'>" + line + "</span>"
    }
}
