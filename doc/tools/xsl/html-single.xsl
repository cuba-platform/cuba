<!--
  ~ Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
  ~ Haulmont Technology proprietary and confidential.
  ~ Use is subject to license terms.
  -->
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:import href="html/docbook.xsl"/>
    <xsl:import href="html-common.xsl"/>
	<xsl:param name="ignore.image.scaling">1</xsl:param>
	
	<!--  Number figures continuosly through the book--> 
	<xsl:template match="figure" mode="label.markup">
		<xsl:number format="1" from="book" level="any" /> 
	</xsl:template>

    <xsl:variable name="headercode">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js" type="text/javascript"></script>
        <script type="text/javascript">

            var allLinksInToc;
            var textNodes;

            function searchButtonClick(){
                var searchQuery = $('#toc-search-box')[0].value;
                if (searchBoxCheck(searchQuery) == true){
                    beginSearch(searchQuery);
                }
            }

            function findAllTextNodes(el) {
                return $(el).find(":not(iframe)").addBack().contents().filter(function() {
                    if (this.nodeType != 3) {
                        if (this.tagName.toLowerCase() == 'p') {
                            return this.innerText;
                        }
                    }
                    return (this.nodeType == 3) &amp;&amp;(this.nodeValue != '\n');
                });
            }

            function findTextInNode(textNode, searchQuery){
                var innerText = $(textNode).text();
                innerText = innerText.replace(/\s+/g,' ');
                if (innerText.toLowerCase().indexOf(searchQuery) >= 0) {
                    var currentEl = textNode;
                    var isNearTitlepageClassFinded = false;
                    while ((isNearTitlepageClassFinded != true) &amp;&amp; (currentEl != null)){ //find near element with className = 'titlepage'
                        var list = $(currentEl).siblings(); // get near elements
                        list.push(currentEl);
                        list = list.toArray();
                        for(var el in list){
                            if (list[el].className == 'titlepage'){
                                var linkText = $(list[el]).find('.title').text().substring(1).toLowerCase();
                                allLinksInToc.each(function(){ // search in toc and mark
                                    var innerCurrentText= $(this).text().toLowerCase();
                                    if(innerCurrentText.indexOf(linkText) >= 0 ){
                                        isNearTitlepageClassFinded = true;
                                        $(this).addClass('toc-search-result');
                                        $(this).removeClass('toc-search-hidden');
                                        selectChildrenOnFirtsLevel(this);
                                        return false;
                                    }
                                });
                            }
                        }
                        currentEl = currentEl.parentNode;
                    }
                }
            }

            function beginSearch(searchQuery){
                searchQuery = searchQuery.toLowerCase();
                clearLinksInToc();
                $(textNodes).each(function() {
                    findTextInNode(this, searchQuery)
                });
                tracePathFromRoot();
            }

            function selectChildrenOnFirtsLevel(link){ // mark childrens of current link on 1st level
                if ($(link.parentNode.parentNode).next('dd').length != 0){
                    var nextElement = $(link.parentNode.parentNode).next('dd');
                    var tempArr = $(nextElement.children().children());
                    for (var i = 0; i &lt; tempArr.length; i++){
                        if (tempArr[i].tagName.toLowerCase() == 'dt'){
                            var currentLink = $(tempArr[i]).find('a');
                            if (!currentLink.hasClass('toc-search-result')){
                                currentLink.removeClass('toc-search-hidden');
                            }
                        }
                    }
                }
            }

            function clearLinksInToc(){
                allLinksInToc.each(function(){
                    $(this).addClass('toc-search-hidden');
                    $(this).removeClass('toc-search-result');
                });
            }

            function tracePathFromRoot(){
                allLinksInToc.each(function() {
                    if ($(this).hasClass('toc-search-result')) {
                        var parent = this.parentNode;
                        while (parent.tagName.toLowerCase() != 'html') {
                            if (parent.tagName.toLowerCase() == 'dd')
                                $(parent).prev('dt').find('a').removeClass('toc-search-hidden');
                            parent = parent.parentNode;
                        }
                    }
                });
            }
            function keyDownTextField(event){
                var searchQuery = $('#toc-search-box')[0].value;
                if ( searchBoxCheck(searchQuery) == true){
                    var keyCode = event.keyCode;
                    if ( keyCode == 13){
                        var searchStr = $('#toc-search-box')[0].value;
                        beginSearch(searchQuery);
                    }
                }
            }

            function searchBoxCheck(searchQuery){
                if (searchQuery.length &lt; 3){
                    allLinksInToc.each(function(){
                        deactivate($(this))
                    });
                    return false;
                }
                return true;
            }

            function deactivate(link) {
                link.removeClass('toc-search-hidden');
                link.removeClass('toc-search-result');
            }

            function isTouchDevice() {
                var el = document.createElement('div');
                el.setAttribute('ongesturestart', 'return;');
                return typeof el.ongesturestart === "function";
            }

            $(document).ready(function() {
                // use template from document for panel
                var tocPanel = $('div#toc-panel-template').clone()[0];
                tocPanel.id = 'toc-panel';
                textNodes = findAllTextNodes(this);
                $('div#toc-panel-template').remove();

                $('div.book').add('<a href="#" id="toc-btn" class="toc-btn">. . .</a>').appendTo(document.body);
                $('div.book').add(tocPanel).appendTo(document.body);

                var tocPanelContent = $('div.toc').clone();
                tocPanelContent[0].id = 'toc-panel-content';
                tocPanelContent.appendTo('#toc-panel');

                var panel = $('#toc-panel');
                var button = $('#toc-btn');
                allLinksInToc = $('#toc-panel-content a');

                var isPanelActive = false;
                var isPanelTouched = false;

                if (isTouchDevice() == true){
                    document.addEventListener('touchmove', function(e) {
                        if ( isPanelTouched == false){
                            if (isPanelActive == true){
                                e.preventDefault();
                                panel.hide('fast');
                                button.removeClass('active');
                                isPanelActive = false;
                            }
                        }
                        isPanelTouched = false;
                    }, false);
                    document.getElementById('toc-panel-content').addEventListener('touchmove', function(e) {
                        isPanelTouched = true;
                    },false);
                }

                button.attr( 'href', 'javascript:void(0)' ).mousedown(function() {
                    panel.toggle('medium');
                    button.toggleClass('active');
                    document.getElementById('toc-search-box').focus();
                    isPanelActive = true;
                    return false;
                });

                $(document).bind('mousedown', function() {
                    panel.hide('fast');
                    button.removeClass('active');
                });

                panel.bind('mousedown', function(e) {
                    e.stopPropagation();
                });

                var searchBox = $('#toc-search-box')[0];
                var searchQuery = '';
            });
    </script>
    </xsl:variable>

    <xsl:template name="user.header.content">
        <div class="toc-panel" id="toc-panel-template">
            <div style="display: table; width:100%">
                <div style="display: table-row" class="toc-search">
                    <div style="display: table-cell;width: 100%;">
                        <input style="width: 100%" type="text" id="toc-search-box" onkeydown="keyDownTextField(event)"/>
                    </div>
                    <input style="display: table-cell;" type="button" id="searchButton" value="Поиск" onclick ="searchButtonClick()"/>

                </div>
            </div>
        </div>

        <xsl:copy-of select="$headercode"/>
    </xsl:template>

    <xsl:output method="html"
            encoding="UTF-8"
            indent="yes"/>

</xsl:stylesheet>