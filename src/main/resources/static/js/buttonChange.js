$(document).ready(function () {
    $("#btn-uk").click(function () {
        window.location.href = updateURLParameter(window.location.href, "lang", "uk")
    });
    $("#btn-ru").click(function () {
        window.location.href = updateURLParameter(window.location.href, "lang", "ru")
    });
    $("#btn-en").click(function () {
        window.location.href = updateURLParameter(window.location.href, "lang", "en")
    });


});

function updateURLParameter(url, param, paramVal) {
    let newAdditionalURL = "";
    let tempArray = url.split("?");
    let baseURL = tempArray[0];
    let additionalURL = tempArray[1];
    let temp = "";
    if (additionalURL) {
        tempArray = additionalURL.split("&");
        for (let i = 0; i < tempArray.length; i++) {
            if (tempArray[i].split('=')[0] != param) {
                newAdditionalURL += temp + tempArray[i];
                temp = "&";
            }
        }
    }

    let rows_txt = temp + "" + param + "=" + paramVal;
    return baseURL + "?" + newAdditionalURL + rows_txt;
}
