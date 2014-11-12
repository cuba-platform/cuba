<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:import href="html/chunkfast.xsl"/>
    <xsl:import href="html-common.xsl"/>

    <xsl:param name="chunk.section.depth">0</xsl:param>
    <xsl:param name="chunk.quietly">1</xsl:param>
	<xsl:param name="admon.textlabel">0</xsl:param>

    <!-- HEADERS AND FOOTERS -->

    <!-- Use custom header -->
    <xsl:template name="header.navigation">
        <xsl:param name="next"/>
        <xsl:param name="prev"/>
        <xsl:if test=". != /book">
            <div class='navheader'>
                <xsl:call-template name="navlinks">
                    <xsl:with-param name="next" select="$next"/>
                    <xsl:with-param name="prev" select="$prev"/>
                </xsl:call-template>
            </div>
        </xsl:if>
    </xsl:template>

    <!-- Use custom footer -->
    <xsl:template name="footer.navigation">
        <xsl:param name="next"/>
        <xsl:param name="prev"/>
        <div class='navfooter'>
            <xsl:call-template name="navlinks">
                <xsl:with-param name="next" select="$next"/>
                <xsl:with-param name="prev" select="$prev"/>
            </xsl:call-template>
        </div>
    </xsl:template>

    <xsl:template name="navlinks">
        <xsl:param name="next"/>
        <xsl:param name="prev"/>
        <div>
            <div class="navbar">
                <xsl:if test="count($prev)>0">
                    <xsl:call-template name="customXref">
                        <xsl:with-param name="target" select="$prev"/>
                        <xsl:with-param name="content">
                            <xsl:text>Previous</xsl:text>
                        </xsl:with-param>
                    </xsl:call-template>
                    <span>|</span>
                </xsl:if>
                <xsl:call-template name="customXref">
                    <xsl:with-param name="target" select="/book"/>
                    <xsl:with-param name="content">
                        <xsl:text>Contents</xsl:text>
                    </xsl:with-param>
                </xsl:call-template>
                <xsl:if test="count($next)>0">
                    <span>|</span>
                    <xsl:call-template name="customXref">
                        <xsl:with-param name="target" select="$next"/>
                        <xsl:with-param name="content">
                            <xsl:text>Next</xsl:text>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </div>
        </div>
    </xsl:template>

    <xsl:output method="html"
            encoding="UTF-8"
            indent="yes"/>

</xsl:stylesheet>