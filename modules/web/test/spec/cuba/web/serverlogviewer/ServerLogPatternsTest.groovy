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

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.haulmont.cuba.core.global.Resources
import com.haulmont.cuba.core.sys.ResourcesImpl
import com.haulmont.cuba.web.app.ui.serverlogviewer.ServerLogWindow
import spock.lang.Shared
import spock.lang.Specification

import java.util.regex.Pattern

@SuppressWarnings("GroovyAccessibility")
class ServerLogPatternsTest extends Specification {

    @Shared
    ServerLogWindow serverLog

    @Shared
    Resources resources

    @Shared
    List correctLines

    @Shared
    List correctPatterns

    @Shared
    List incorrectLines

    @Shared
    def incorrectPatterns

    def setupSpec() {
        serverLog = new ServerLogWindow()
        def loader = getClass().getClassLoader()
        resources = new ResourcesImpl(loader, null)
        loadData()
    }

    def "check the line doesn't match pattern"(String line, String pattern) {
        when:
        def transformedLine = serverLog.replaceSpaces(line)
        def transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        def changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine != serverLog.getLoweredAttentionLine(transformedLine)

        where:
        line << incorrectLines
        pattern << incorrectPatterns
    }

    def "check the line matches pattern"(String line, String pattern) {
        when:
        def transformedLine = serverLog.replaceSpaces(line)
        def transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        def changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        where:
        line << correctLines
        pattern << correctPatterns
    }

    def loadData() {
        loadCorrectTestData()
        loadIncorrectTestData()
    }

    def loadIncorrectTestData() {
        def incorrectTestDataPath = "/spec/cuba/web/serverlogviewer/incorrectTestData.json"
        def json = resources.getResourceAsString(incorrectTestDataPath)
        def data = new Gson().fromJson(json, ArrayList.class) as List<LinkedTreeMap>

        incorrectLines = new ArrayList<>()
        incorrectPatterns = new ArrayList<>()
        for (LinkedTreeMap object : data) {
            String line = object.get("line").toString()
            incorrectLines.add(line)
            String pattern = object.get("pattern").toString()
            incorrectPatterns.add(pattern)
        }
    }

    def loadCorrectTestData() {
        def correctTestDataPath = "/spec/cuba/web/serverlogviewer/correctTestData.json"
        def json = resources.getResourceAsString(correctTestDataPath)
        def data = new Gson().fromJson(json, ArrayList.class) as List<LinkedTreeMap>

        correctLines = new ArrayList<>()
        correctPatterns = new ArrayList<>()
        for (LinkedTreeMap object : data) {
            String line = object.get("line").toString()
            correctLines.add(line)
            String pattern = object.get("pattern").toString()
            correctPatterns.add(pattern)
        }
    }
}