$(document).ready(function () {
    $(".myselect").click(function () {
        window.location.href = updateURLParameter(window.location.href, "page", $(this).attr("value"))
    });
    $(".sort-order").click(function () {
        window.location.href = updateURLParameter(window.location.href, "sort", $(this).attr("value"))
    });

    $(".filter-check").on('change', function (e) {
        let filterParams = [];
        $('input[type="checkbox"]').each(function (i) {
            if (this.checked) {

                filterParams.push($(this).attr("value"));
            }
        });
        window.location.href = updateURLParameter(window.location.href, "filter", filterParams.join(','))
    });


    $(".tableHead").click(function () {
        if (window.location.href.includes($(this).attr("value")) && window.location.href.includes('asc')) {
            window.location.href = updateURLParameter(window.location.href, "order", "desc")
        } else {
            let href = updateURLParameter(window.location.href, "order", "asc")
            window.location.href = updateURLParameter(href, "sort", $(this).attr("value"))
        }
    });

    if (window.location.href.includes('asc')) {
        $(".order").text("🠕");
    } else {
        $(".order").text("🠗");
    }

    $(".order").click(function () {

        if (window.location.href.includes('asc')) {
            $(this).attr("value", "desc")
        } else {
            $(this).attr("value", "asc")
        }


        window.location.href = updateURLParameter(window.location.href, "order", $(this).attr("value"))


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

