$(document).ready(function () {
    $('.itemtype').click(function () {
        addTypeForSearch($(this));
    });
    $('.itemprice').click(function () {
        addPriceForSearch($(this));
    });
    setClassOnPagination();
});

function setClassOnPagination() {
    var page = $('#p' + currentPage);
    var about = page.attr('about');
    page.addClass('active');
    var first = $('#p0').attr('about');
    var last = $('#p' + number).attr('about');
    if (about == first) {
        $('.previous').addClass('disabled');
        $('.previous').addClass('noLink');
    }
    if (about == last) {
        $('.next').addClass('disabled');
        $('.next').addClass('noLink');
    }
}
$('.img-responsive').css('cursor', 'pointer');
$('.forSend').css('cursor', 'pointer');

function addTypeForSearch(element) {
    var type = $(element).attr('name');
    var action = 'OFF';
    if ($(element).is(":checked")) {
        action = 'ON';
    }
    var command = 'SEARCH_FILTER';
    $.ajax({
        type: 'POST',
        data: {command: command, type: type, actionType: action},
        url: contextUrl + '/ajax'
    }).done(function (data) {
        $('#search').text(textButton + ' ' + data);
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}

function addPriceForSearch(element) {
    var price = $(element).attr('value');
    var command = 'SEARCH_FILTER';
    $.ajax({
        type: 'POST',
        data: {command: command, price: price},
        url: contextUrl + '/ajax'
    }).done(function (data) {
        $('#search').text(textButton + ' ' + data);
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}

function sendItemId(element) {
    var eId = $(element).attr('id');
    var itemId = $(element).attr('name');
    var cursor = "";
    if (eId == 'comment') {
        cursor = "#comments";
    }
    var command = 'SET_ITEM_ID';
    $.ajax({
        type: 'POST',
        data: {command: command, itemId: itemId},
        url: contextUrl + '/ajax'
    }).done(function (data) {
        if (eId == 'forAdmin') {
            location.href = contextUrl + '/controller?command=administration';
        } else {
            location.href = contextUrl + '/controller?command=preview_item' + cursor;
        }
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}

reset.onclick = function () {
    var command = 'RESET_FILTER';
    $.ajax({
        type: 'POST',
        data: {command: command},
        url: contextUrl + '/ajax'
    }).done(function () {
        location.href = contextUrl + '/controller?command=main';
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
};