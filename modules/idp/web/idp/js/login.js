$(function () {
    function getUrlQueryParameter(param) {
        var vars = {};
        window.location.href.replace( location.hash, '' ).replace(
            /[?&]+([^=&]+)=?([^&]*)?/gi,
            function( m, key, value ) {
                vars[key] = value !== undefined ? value : '';
            }
        );

        if (param) {
            return vars[param] ? vars[param] : null;
        }
        return vars;
    }

    var spQueryParameter = getUrlQueryParameter('sp');
    var serviceProviderUrl = "";
    if (spQueryParameter != null) {
        serviceProviderUrl = decodeURIComponent(spQueryParameter);
    }

    var uri = window.location.toString();
    if (window.history && uri.indexOf("?") > 0) {
        var clean_uri = uri.substring(0, uri.indexOf("?"));
        window.history.replaceState({}, document.title, clean_uri);
    }

    $(".form-signin").submit(function (e) {
        $("button.auth-submit").prop('disabled', true);
        $(".alert").hide();

        var selectedLocale = null;
        var $inputLocale = $("#inputLocale");
        if ($inputLocale.is(":visible")) {
            selectedLocale = $inputLocale.val();
        }

        $.ajax({
            type: "POST",
            url: "auth",
            data: JSON.stringify({
                username: $('.form-username').val(),
                password: $('.form-password').val(),
                serviceProviderUrl: serviceProviderUrl,
                locale: selectedLocale
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (result) {
                if (result.errorCode == null) {
                    $(".idp_loggedin").show();

                    setTimeout(function () {
                        window.location = result.serviceProviderUrl;
                    }, 750);
                } else {
                    $("button.auth-submit").prop('disabled', false);

                    if ("invalid_params" == result.errorCode
                        || "invalid_credentials" == result.errorCode) {
                        $(".idp_failed").show();
                    } else {
                        $(".idp_error").show();
                    }
                }
            },
            error: function () {
                $("button.auth-submit").prop('disabled', false);
                $(".idp_error").show();
            }
        });

        e.preventDefault();
    });

    var localizedHandler = function() {
        var htmlEl = document.documentElement;
        var l10n = document.webL10n;

        htmlEl.lang = l10n.getLanguage();
        htmlEl.dir = l10n.getDirection();
        htmlEl.classList.add(l10n.getLanguage());

        window.removeEventListener("localized", localizedHandler, false);

        // get available locales
        $.ajax({
            type: "GET",
            url: "locales",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (result) {
                if (result.localeSelectVisible) {
                    $localeSelect = $("#inputLocale");
                    var firstLangCode = null;
                    // prepare locales
                    $.each(result.locales, function (key, value) {
                        if (firstLangCode == null) {
                            firstLangCode = key;
                        }

                        $localeSelect.append("<option value=\"" + key + "\"> " + value + "</option>");
                    });

                    // show locale selector
                    $(".locale-control").show();

                    if (firstLangCode != null && l10n.getLanguage() != firstLangCode) {
                        l10n.setLanguage(firstLangCode);
                    }

                    $localeSelect.on('change', function() {
                        l10n.setLanguage(this.value);
                    });
                }
            },
            error: function () {
                $(".idp_error").show();
            }
        });
    };
    window.addEventListener('localized', localizedHandler, false);
});