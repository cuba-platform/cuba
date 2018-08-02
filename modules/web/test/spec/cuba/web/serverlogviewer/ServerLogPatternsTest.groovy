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

import java.util.regex.Pattern

class ServerLogPatternsTest extends Specification {

    def "highlight lowered attention by patterns"() {
        given: "open server log window"
        def serverLog = new ServerLogWindow()

        when: "line not from stack trace includes pattern's value"
        def line = "myLine consists of some-symbols"
        def pattern = /.*some-symbol./
        def transformedLine = serverLog.replaceSpaces(line)
        def transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        def changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "2018-07-20 12:36:27.955 DEBUG [http-nio-8080-exec-7] " +
                "com.haulmont.cuba.gui.theme.ThemeConstantsRepository - Loading theme constants"
        pattern = /http-nio-8080-exec-\d*/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace doesn't include pattern's value"
        line = "2018-07-20 12:36:27.955 DEBUG [http-nio-8080-exec-77] " +
                "com.haulmont.cuba.gui.theme.ThemeConstantsRepository - Loading theme constants"
        pattern = /http-nio-8080-exec-\D/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine != serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace with complex pattern"
        line = "at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_171]"
        pattern = /.*at.*sun[\.]reflect[\.]NativeMethodAccessorImpl/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at com.sun.proxy.\$Proxy28.executeUpdate (Unknown Source)"
        pattern = /at com[\.]sun[\.]proxy[\.][\$]Proxy/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.lang.reflect.Constructor.newInstance(Constructor.java:408)"
        pattern = /at java[\.]lang[\.]reflect[\.]Constructor[\.]newInstance/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.security.ProtectionDomain\$1.doIntersectionPrivilege(ProtectionDomain.java:75)"
        pattern = /at java[\.]security[\.]ProtectionDomain[\$]1[\.]doIntersectionPrivilege|/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at groovy.myPackage.error."
        pattern = /at groovy[\.]|/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.lang.reflect.Method.invoke(Unknown Source)"
        pattern = /at java[\.]lang[\.]reflect[\.]Method[\.]invoke|/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.rmi.server.SkeletonNotFoundException"
        pattern = /at java[\.]rmi[\.]|/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.security.AccessControlContext\$1.doIntersectionPrivilege(AccessControlContext.java:87)"
        pattern = /at java[\.]security[\.]AccessControlContext[\$]1[\.]doIntersectionPrivilege/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.security.AccessController.doPrivileged"
        pattern = /at java[\.]security[\.]AccessController[\.]doPrivileged/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.security.ProtectionDomain\$1.doIntersectionPrivilege(Unknown Source)"
        pattern = /at java[\.]security[\.]ProtectionDomain[\$]1[\.]doIntersectionPrivilege/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.security.ProtectionDomain\$JavaSecurityAccessImpl.doIntersectionPrivilege(Unknown Source)"
        pattern = /at java[\.]security[\.]ProtectionDomain[\$]JavaSecurityAccessImpl[\.]doIntersectionPrivilege/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.util.Spliterators\$"
        pattern = /at java[\.]util[\.]Spliterators[\$]/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "[na:1.8.0_77] at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:481)"
        pattern = /at java[\.]util[\.]stream[\.]AbstractPipeline[\.]copyInto/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)"
        pattern = /at java[\.]util[\.]stream[\.]AbstractPipeline[\.]evaluate/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:502)"
        pattern = /at java[\.]util[\.]stream[\.]AbstractPipeline[\.]wrapAndCopyInto/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.util.stream.ReduceOps\$ReduceOp.evaluateSequential(ReduceOps.java:708)"
        pattern = /at java[\.]util[\.]stream[\.]ReduceOps[\$]/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at java.util.stream.ReferencePipeline\$2\$1.accept(ReferencePipeline.java:174)"
        pattern = /at java[\.]util[\.]stream[\.]ReferencePipeline[\$]/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at org.codehaus.groovy.tools.GroovyStarter.rootLoader(GroovyStarter.java:109)"
        pattern = /at org[\.]codehaus[\.]groovy[\.]/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at org.gradle.internal.progress.DefaultBuildOperationExecutor.run(DefaultBuildOperationExecutor.java:56)"
        pattern = /at org[\.]gradle[\.]/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)"
        pattern = /at sun[\.]reflect[\.]/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at sun.rmi.server.UnicastServerRef.dispatch(Unknown Source)"
        pattern = /at sun[\.]rmi[\.]/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = "at com.vaadin.event.EventRouter.fireEvent(EventRouter.java:164)"
        pattern = /at com[\.]vaadin[\.]event[\.]EventRouter[\.]fireEvent/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)

        when: "line from stack trace includes pattern's value"
        line = " at com.vaadin.server.ServerRpcManager.applyInvocation(ServerRpcManager.java:162)"
        pattern = /at com[\.]vaadin[\.]server[\.]ServerRpcManager/
        transformedLine = serverLog.replaceSpaces(line)
        transformedPattern = Pattern.compile(serverLog.replaceSpaces(pattern))
        changedLine = serverLog.highlightLoweredAttention(transformedLine, transformedPattern)

        then:
        changedLine == serverLog.getLoweredAttentionLine(transformedLine)
    }
}
