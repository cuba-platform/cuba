<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="programlisting[@language = 'java']" mode="class.value">
        <xsl:value-of select="'programlisting language-java'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'sql']" mode="class.value">
        <xsl:value-of select="'programlisting language-sql'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'xml']" mode="class.value">
        <xsl:value-of select="'programlisting language-xml'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'html']" mode="class.value">
        <xsl:value-of select="'programlisting language-html'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'js']" mode="class.value">
        <xsl:value-of select="'programlisting language-javascript'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'css']" mode="class.value">
        <xsl:value-of select="'programlisting language-css'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'bash']" mode="class.value">
        <xsl:value-of select="'programlisting language-bash'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'bat']" mode="class.value">
        <xsl:value-of select="'programlisting language-bat'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'scss']" mode="class.value">
        <xsl:value-of select="'programlisting language-scss'"/>
    </xsl:template>

    <xsl:template match="programlisting[@language = 'json']" mode="class.value">
        <xsl:value-of select="'programlisting language-json'"/>
    </xsl:template>

</xsl:stylesheet>