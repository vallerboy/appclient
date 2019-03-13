function textOnSearchInputChanged() {
    let textElement = document.getElementById("search");

    window.location.href = "http://5.135.218.27:9012/search/product/" + textElement.value;

    return false;
}

function setCursorAtEnd() {
    let textElement = document.getElementById("search");
    let value = textElement.value;

    textElement.value = "";
    textElement.value = value;
}

function sendAsyncEditedToServer(productId, input) {
    if (isNaN(input.value)) {
        return;
    }

    let request = new XMLHttpRequest();
    request.open("GET", "http://5.135.218.27:9012/transaction/product/change/" + productId + "/" + input.value, true);
    request.send();
}


function showConfirmOrderDialog(data) {
    bootbox.confirm({
        message: "Czy na pewno chcesz złożyć zamówienie?",
        buttons: {
            confirm: {
                label: 'Tak',
                className: 'btn-success'
            },
            cancel: {
                label: 'Nie',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if (result) {
                window.location.href = "/summary/makeOrder/" + data;
            }
        }
    });
}

function showConfirmClearOrder() {
    bootbox.confirm({
        message: "Czy na pewno chcesz WYCZYŚCIĆ zamówienie?",
        buttons: {
            confirm: {
                label: 'Tak',
                className: 'btn-success'
            },
            cancel: {
                label: 'Nie',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if (result)
                window.location.href = "/summary/clearOrder/";
        }
    });
}


function preloadSummaryData() {
    var sumNetto = 0;
    var sumBrutto = 0;
    var allQuantity = 0;


    $("#summaryTable tr").not(':first').each(function () {
        allQuantity += getnum($(this).find("td:eq(1)").find('input').val());

        sumNetto += getnum($(this).find("td:eq(2)").text()) * getnum($(this).find("td:eq(1)").find('input').val());
        sumBrutto += (1 + (getnum($(this).find("td:eq(3)").text()) / 100)) * (getnum($(this).find("td:eq(2)").text()) * getnum($(this).find("td:eq(1)").find('input').val()));

        function getnum(t) {
            return parseFloat(t);
        }
    });

    console.log('Netto: ' + sumNetto);
    console.log('Qua: ' + allQuantity);
    console.log('Brutto: ' + sumBrutto);


    $("#sumNetto").text(Math.round(sumNetto * 100) / 100);
    $("#sumBrutto").text(Math.round(sumBrutto * 100) / 100);
    $("#sumAll").text(Math.round(allQuantity * 100) / 100);

}

$(document).ready(function () {
    $('li.has-submenu > a').on('click', function (e) {
        e.preventDefault();
        var targetElem = $(this).closest('li.has-submenu').find('.submenu').first();
        if ($(targetElem).hasClass('active')) {
            $('.submenu').removeClass('active');
        } else {
            $('.submenu').removeClass('active');
            $(targetElem).toggleClass('active');
        }

        document.getElementById('categories').scrollTop = 0;
    });


    preloadSummaryData();
});


