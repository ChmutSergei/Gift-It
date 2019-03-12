$(document).ready(function () {
    oOpen();
    iClose();
    uClose();
    qClose();
    oAdd();
    $('#orders').click(function () {
        oOpen();
        iClose();
        uClose();
        qClose();
        oAdd();
        iRem();
        uRem();
        qRem();
    });
    $('#items').click(function () {
        oClose();
        iOpen();
        uClose();
        qClose();
        oRem();
        iAdd();
        uRem();
        qRem();
    });
    $('#users').click(function () {
        oClose();
        iClose();
        uOpen();
        qClose();
        oRem();
        iRem();
        uAdd();
        qRem();
    });
    $('#questions').click(function () {
        oClose();
        iClose();
        uClose();
        qOpen();
        oRem();
        iRem();
        uRem();
        qAdd();
    });
    $('#datetimepicker').datetimepicker({
        format: 'DD.MM.YYYY',
        locale: locale
    });
    $('#datetimepicker').data("DateTimePicker").maxDate(moment(new Date(), 'DD.MM.YYYY'));
});

$('.img-responsive').css( 'cursor', 'pointer' );
function oAdd() {
    $('#orders').addClass('active');
}

function oRem() {
    $('#orders').removeClass('active');
}

function iAdd() {
    $('#items').addClass('active');
}

function iRem() {
    $('#items').removeClass('active');
}

function uAdd() {
    $('#users').addClass('active');
}

function uRem() {
    $('#users').removeClass('active');
}

function qAdd() {
    $('#questions').addClass('active');
}

function qRem() {
    $('#questions').removeClass('active');
}

$('body').click(function () {
    $('#done')[0].style.display = "none";
});

function oClose() {
    $('.orders')[0].style.display = "none";
}

function oOpen() {
    $('.orders')[0].style.display = "block";
}

function iClose() {
    $('.items')[0].style.display = "none";
}

function iOpen() {
    $('.items')[0].style.display = "block";
}

function uClose() {
    $('.users')[0].style.display = "none";
}

function uOpen() {
    $('.users')[0].style.display = "block";
}

function qClose() {
    $('.questions')[0].style.display = "none";
}

function qOpen() {
    $('.questions')[0].style.display = "block";
}

function sendItemId(element) {
    var itemId = $(element).attr('name');
    var command = 'CHANGE_STATUS_ITEM';
    $.ajax({
        type: 'POST',
        data: {command: command, itemId: itemId},
        url: contextUrl + '/ajax'
    }).done(function (data) {
        if (data == 'true') {
            $('#done')[0].style.display = "block";
        }
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}