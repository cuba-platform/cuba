/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
function isPc() {
    var userAgent = navigator.userAgent.toLowerCase();
    var notPcOsArr = ["ipad", "iphone", "ipod", "windows phone", "phone", "android"];
    for (var i = 0; i < notPcOsArr.length; i++) {
        if (userAgent.indexOf(notPcOsArr[i]) >= 0) {
            return false;
        }
    }
    return true;
}

if (isPc()) {

    var allLinksInToc;
    var textNodes;
    var TextNodeElement = function (text, node) {
        this.text = text;
        this.node = node;
    };
    var tree;
    var middleDiv;
    var linkClickCheck;
    var linksTocSearchResult = [];
    var isShowPanel;

    function convertListInTree() {
        var dlRoot = $('div.toc dl.toc:first');
        var htmlList = '<ul class="' + dlRoot.attr('class') + '" id="toc_tree">';

        var dtElement = $(dlRoot).children('dt');
        dtElement.each(function () {
            htmlList += createLi(this);
        });

        return htmlList += '</ul>';

        function createLi(dt) {
            var htmlLi = '<li>' + dt.innerHTML;
            var nextElement = dt.nextElementSibling || dt.nextSibling;
            if (nextElement === null) {
                htmlLi += '</li>';
                return htmlLi;
            }

            if (nextElement.nodeName.toLowerCase() == 'dd') {
                htmlLi += '<ul>';
                var ddChildren = nextElement.childNodes;
                for (var i = 0; i < ddChildren.length; i++) {
                    if (ddChildren[i].nodeName.toLowerCase() == 'dl') {
                        var dlChildren = ddChildren[i].childNodes;
                        for (var j = 0; j < dlChildren.length; j++) {
                            if (dlChildren[j].nodeName.toLowerCase() == 'dt') {
                                htmlLi += createLi(dlChildren[j]);
                            }
                        }
                        break;
                    }
                }
                htmlLi += '</ul>';
            }
            htmlLi += '</li>';
            return htmlLi
        }
    }

    function addTocSearchResult(link) {
        $(link).addClass('toc-search-result');
        linksTocSearchResult.push(link);
    }

    function removeTocSearchResult() {
        for (var i = 0; i < linksTocSearchResult.length; i++) {
            $(linksTocSearchResult[i]).removeClass('toc-search-result');
        }
        linksTocSearchResult.length = 0;
    }

    function replaceTextNode(textNode) {
        if (textNode === undefined) {
            return "";
        }
        return textNode.replace(/\n\t\v\r/g, '').replace(/^\s+|\s+$/g, '').replace(/\s+/g, ' ').toLowerCase();
    }

    function openNodesForSearch(li) {
        li.css('display', 'block');
        var searchResultLi = $('a.toc-search-result', tree).closest('li');
        var childrenSearchLi = $('ul', searchResultLi).children('li');
        childrenSearchLi.css('display', 'block');
        var eventDiv = li.children('div.hitarea');
        eventDiv.each(function () {
            if ($(this).hasClass('expandable-hitarea')) {
                $(this).click();
            }
        });
    }

    function openYourselfAndHighlightForSearch(link) {
        addTocSearchResult(link);
        var parentsLi = $(link).parents('li');
        openNodesForSearch(parentsLi);
    }

    function findAllTextNodes(el) {
        var documentNotParse = $(el).find(":not(iframe, style, script, comment)").addBack().contents();

        return $.map(documentNotParse, function (val) {
            if (val.nodeType != 3) {
                if (val.nodeType === 8) {
                    return null;
                }

                if (val.tagName.toLowerCase() == 'p') {
                    var text = replaceTextNode(val.innerText || val.textContent);
                    if (text.length > 0)
                        return new TextNodeElement(text, val);
                }
            } else {
                text = replaceTextNode(val.nodeValue);
                if (text.length > 0) {
                    return new TextNodeElement(text, val);
                }
            }

            return null;
        });
    }

    function findTextInNode(textNode, searchQuery) {
        var innerText = textNode.text;
        if (innerText.indexOf(searchQuery) >= 0) {
            var currentEl = textNode.node;
            var isNearTitlepageClassFinded = false;
            while ((isNearTitlepageClassFinded != true) && (currentEl != null)) {
                var list = $(currentEl).siblings();
                list = list.toArray();
                list.push(currentEl);
                for (var i = 0; i < list.length; i++) {
                    var listItem = list[i];
                    if (listItem.className == 'titlepage') {
                        //todo ie9( css3(
                        var linkHref = $(listItem).find('.title:first').find('a[href]:first').attr('href');
                        var link = findLinkByHref(linkHref);
                        if (link !== null) {
                            isNearTitlepageClassFinded = true;
                            openYourselfAndHighlightForSearch(link);
                            break;
                        }
                    }
                }
                currentEl = currentEl.parentNode;
            }
        }
    }

    function beginSearch(searchQuery) {
        searchQuery = replaceTextNode(searchQuery);
        $('li', tree).css('display', 'none');
        removeTocSearchResult();
        $(textNodes).each(function () {
            findTextInNode(this, searchQuery)
        });
    }

    function searchBoxCheck() {
        var query = $('#toc-search-box').val();
        if (query.length < 3) {
            return null;
        }
        return query;
    }

    $.fn.scrollStopped = function (callback) {
        $(this).scroll(function () {
            var self = this, $this = $(self);
            if ($this.data('scrollTimeout')) {
                clearTimeout($this.data('scrollTimeout'));
            }
            $this.data('scrollTimeout', setTimeout(callback, 500, self));
        });
    };

    function closeNodesForLocation(closeDiv) {
        closeDiv.each(function () {
            if ($(this).hasClass('collapsable-hitarea')) {
                $(this).click();
            }
        });
    }

    function openNodesForLocation(openDiv) {
        openDiv.each(function () {
            if ($(this).hasClass('expandable-hitarea')) {
                $(this).click();
            }
        });
    }

    function currentLocation(){
        var middlePosition = middleDiv.offset();
        var nearestTitlepage = $.nearest({x: middlePosition.left, y: middlePosition.top}, 'div.titlepage a[href]')[0];
        var isNearTitlepageClassFinded = false;
        var link = null;
        while ((isNearTitlepageClassFinded != true) && (nearestTitlepage != null)) {
            var list = $(nearestTitlepage).siblings();
            list = list.toArray();
            list.push(nearestTitlepage);
            for (var i = 0; i < list.length; i++) {
                var listItem = list[i];
                if (listItem.className == 'titlepage') {
                    var linkHref = $(listItem).find('.title:first').find('a[href]:first').attr('href');
                    link = findLinkByHref(linkHref);
                    if (link !== null) {
                        isNearTitlepageClassFinded = true;
                        break;
                    }
                }
            }
            nearestTitlepage = nearestTitlepage.parentNode;
        }
        return link;
    }

    function locationPanel(middleDiv) {
        if ((searchBoxCheck() === null) && isShowPanel) {
            if (!linkClickCheck) {
                var link = currentLocation();
                if (link !== null) {
                    var openNodesDiv = $('div.hitarea', '#toc-panel-content').filter('.collapsable-hitarea');
                    var potentialOpenDiv = $(link).parents('li').children('div.hitarea');
                    var closeNodesDiv = $.map(openNodesDiv, function (val) {
                        if ($.inArray(val, potentialOpenDiv) === -1) {
                            return val;
                        } else {
                            return null;
                        }
                    });

                    closeNodesForLocation($(closeNodesDiv));
                    removeTocSearchResult();
                    addTocSearchResult(link);
                    openNodesForLocation(potentialOpenDiv);
                }
            } else {
                linkClickCheck = false;
                return false;
            }
        }
    }


    function findLinkByHref(href) {
        var element = null;
        allLinksInToc.each(function () {
            var innerCurrentHref = $(this).attr('href');
            if (innerCurrentHref === href) {
                element = this;
                return false;
            }
            return true;
        });
        return element;
    }


    function linkClick(event) {
        if (searchBoxCheck() === null) {
            linkClickCheck = true;
            removeTocSearchResult();
            var hash = location.hash;
            var myLocation = location;
            var link = findLinkByHref(hash);
            var potentialOpenDiv = $(link).parents('li').children('div.hitarea');
            potentialOpenDiv.each(function () {
                if ($(this).hasClass('expandable-hitarea')) {
                    $(this).click();
                }
            });
            setTimeout(function () {
                addTocSearchResult(link);
                window.location = myLocation;
            }, 0);
            window.location = myLocation;
        }
    }


    $(document).ready(function () {
        var bookLeftShow = '26em';
        var titles = [];

        $('.title').each(function () {
            var innerLinks = $(this).children('a');
            if (innerLinks.size() == 1) {
                titles.push({tag: this, anchor: innerLinks[0].name});
            }
        });

        $.each(titles, function (index, item) {
            $(item.tag).append('<a class="title-anchor" href="#' + item.anchor + '">[#]</a>');
        });

        //init
        textNodes = findAllTextNodes(document);
        $('<a href="#" id="toc-btn" class="toc-btn">. . .</a>').appendTo(document.body);
        middleDiv = $('#toc-btn');
        var panel = $('#toc-panel');
        var book = $('div.book');
        //it is PC
        panel.css('display', 'block');
        book.css({
            'left': bookLeftShow
        });
        var closePanel = $('#close-panel');
        var tocPanelContent = $('#toc-panel-content');
        $(convertListInTree()).appendTo(tocPanelContent);
        allLinksInToc = $('a', tocPanelContent);
        tree = $("#toc_tree");

        //events
        tree.treeview({
            collapsed: true,
            animated: "medium",
            persist: "location",
            unique: false,
            control: "#treecontrol"
        });

        var isShift = false;
        $(document).keyup(function (e) {
            if (e.which == 16) isShift = false;
        }).keydown(function (e) {
                if (e.which == 16) isShift = true;
                if (e.which == 191 && isShift == true) {
                    if (!isShowPanel) {
                        middleDiv.trigger('click');
                    } else {
                        closePanel.trigger('click');
                    }
                    return false;
                }
            });

        $(window).bind('hashchange', linkClick);
        $('a', tree).bind('click', function () {
            linkClickCheck = true;
        });

        linkClickCheck = true;

        $('#formSearch').submit(function (e) {
            e.preventDefault();
            var searchQuery = searchBoxCheck();
            if (searchQuery !== null) {
                beginSearch(searchQuery);
            } else {
                $('li', tree).css('display', 'block');
                locationPanel(middleDiv);
            }
            return false;
        });

        $(window).scrollStopped(function () {
            locationPanel(middleDiv);
        });

        closePanel.bind('click', function (e) {
            e.preventDefault();
            isShowPanel = false;
            var linkLocation;
            if(searchBoxCheck() === null){
                linkLocation = linksTocSearchResult[0]
            }else{
                linkLocation = currentLocation();
            }

            panel.css('display', 'none');
            book.css('left', '2.5em');
            middleDiv.css('display', 'block');
            location.href = linkLocation.href;
        });

        isShowPanel = true;


        middleDiv.bind('click', function (e) {
            e.preventDefault();
            isShowPanel = true;
            var linkLocation = currentLocation();
            locationPanel(middleDiv);
            panel.css('display', 'block');
            book.css('left', bookLeftShow);
            middleDiv.css('display', 'none');
            location.href = linkLocation.href;
        });

        if (location.hash.length > 0) {
            $(window).trigger('hashchange');
        }
        $('pre.programlisting').each(function (i, e) {
            var $e = $(e);
            if ($e.hasClass('language-java')
                || $e.hasClass('language-xml')
                || $e.hasClass('language-css')
                || $e.hasClass('language-javascript')
                || $e.hasClass('language-html')
                || $e.hasClass('language-bash')
                || $e.hasClass('language-bat')
                || $e.hasClass('language-sql')
                || $e.hasClass('language-scss')
                || $e.hasClass('language-json')) {

                hljs.highlightBlock(e);
            }
        });
    });
}
