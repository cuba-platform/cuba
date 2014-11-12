<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:import href="html/docbook.xsl"/>
    <xsl:import href="html-common.xsl"/>
    <xsl:param name="ignore.image.scaling">0</xsl:param>
    <xsl:param name="toc.section.depth">5</xsl:param>
    <xsl:param name="section.autolabel">1</xsl:param>
    <xsl:param name="chapter.autolabel">1</xsl:param>
    <xsl:param name="section.autolabel.max.depth">5</xsl:param>
    <xsl:param name="admon.textlabel">0</xsl:param>

    <!--  Number figures continuosly through the book-->
    <xsl:template match="figure" mode="label.markup">
        <xsl:number format="1" from="book" level="any"/>
    </xsl:template>

    <xsl:variable name="headercode">
        <script src="jquery-1.11.1.min.js" type="text/javascript"></script>
        <script src="jquery.treeview.js" type="text/javascript"></script>
        <script src="jquery.nearest.min.js" type="text/javascript"></script>
        <script src="highlight.pack.js" type="text/javascript"></script>
        <script src="single-features.js" type="text/javascript"></script>
        <link href="jquery.treeview.css" rel="stylesheet" type="text/css"/>
        <link href="highlight.idea.css" type="text/css" rel="stylesheet"/>
        <style type="text/css">
            .title-anchor {
            text-decoration: inherit;
            }
            .title:hover .title-anchor {
            visibility: visible;
            }
            .title .title-anchor {
            visibility: hidden;
            }
            .treeview ul {
            background-color: #404040;
            }
        </style>
    </xsl:variable>

    <xsl:template name="user.header.content">
        <xsl:variable name="docLang" select="//book/@lang"/>

        <xsl:variable name="search">
            <xsl:choose>
                <xsl:when test="$docLang = 'ru'">Поиск</xsl:when>
                <xsl:when test="$docLang = 'en'">Search</xsl:when>
                <xsl:otherwise>Search</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="collapseAll">
            <xsl:choose>
                <xsl:when test="$docLang = 'ru'">Свернуть все</xsl:when>
                <xsl:when test="$docLang = 'en'">Collapse all</xsl:when>
                <xsl:otherwise>Search</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="expandAll">
            <xsl:choose>
                <xsl:when test="$docLang = 'ru'">Развернуть все</xsl:when>
                <xsl:when test="$docLang = 'en'">Expand all</xsl:when>
                <xsl:otherwise>Search</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="closePanel">
            <xsl:choose>
                <xsl:when test="$docLang = 'ru'">Скрыть панель</xsl:when>
                <xsl:when test="$docLang = 'en'">Hide panel</xsl:when>
                <xsl:otherwise>Search</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="content">
            <xsl:choose>
                <xsl:when test="$docLang = 'ru'">Содержание</xsl:when>
                <xsl:when test="$docLang = 'en'">Content</xsl:when>
                <xsl:otherwise>Search</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>


        <!--<xsl:choose>-->
            <!--<xsl:when test="$docLang = ru">-->
                <!--<xsl:variable name="search">Поиск</xsl:variable>-->
                <!--<xsl:variable name="collapseAll" select="Свернуть/все"/>-->
                <!--<xsl:variable name="expandAll" select="Развернуть/все"/>-->
                <!--<xsl:variable name="closePanel" select="Закрыть/панель"/>-->
                <!--<xsl:variable name="content" select="Содержание"/>-->
            <!--</xsl:when>-->
            <!--<xsl:when test="$docLang = en">-->
                <!--<xsl:variable name="search" select="Search"/>-->
                <!--<xsl:variable name="collapseAll" select="Collapse/all"/>-->
                <!--<xsl:variable name="expandAll" select="Expand/all"/>-->
                <!--<xsl:variable name="closePanel" select="Close/panel"/>-->
                <!--<xsl:variable name="content" select="Content"/>-->
            <!--</xsl:when>-->
            <!--<xsl:otherwise>-->
                <!--<xsl:variable name="search" select="Search"/>-->
                <!--<xsl:variable name="collapseAll" select="Collapse/all"/>-->
                <!--<xsl:variable name="expandAll" select="Expand/all"/>-->
                <!--<xsl:variable name="closePanel" select="Close/panel"/>-->
                <!--<xsl:variable name="content" select="Content"/>-->
            <!--</xsl:otherwise>-->
        <!--</xsl:choose>-->


        <div id="toc-panel" class="toc-panel">
            <div style="display: table; width:100%">
                <div class="toc-search" style="display: table-row; width: auto;">
                    <form id="formSearch" action="#">
                        <input id="toc-search-box" type="text" style="width: 80%;"/>
                        <input id="searchButton" type="submit">
                            <xsl:attribute name="value">
                                <xsl:value-of select="$search"/>
                            </xsl:attribute>
                        </input>
                    </form>
                </div>
            </div>
            <div id="treecontrol">
                <a href="#">
                    <xsl:value-of select="$collapseAll"/>
                </a>
                <span class="separator-link"> | </span>
                <a href="#">
                    <xsl:value-of select="$expandAll"/>
                </a>
                <span class="separator-link"> | </span>
                <a href="#" id="close-panel" style="white-space: pre;">
                    <xsl:value-of select="$closePanel"/>
                </a>
            </div>
            <div class="toc" id="toc-panel-content">
                <p>
                    <b>
                        <xsl:value-of select="$content"/>
                    </b>
                </p>
            </div>
        </div>

        <xsl:copy-of select="$headercode"/>
    </xsl:template>

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>
</xsl:stylesheet>