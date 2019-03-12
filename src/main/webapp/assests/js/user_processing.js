$(document).ready(function () {
    $('#datetimepicker').datetimepicker({
        format: 'DD.MM.YYYY',
        locale: locale
    });
    $('#datetimepicker').data("DateTimePicker").minDate(moment(new Date(), 'DD.MM.YYYY'));
    if (done == 'true') {
        $('#done')[0].style.display = "block";
    }
});

function bChoice() {
    bOpen();
    uClose();
    rClose();
    dClose();
}

function uChoice() {
    bClose();
    uOpen();
    rClose();
    dClose();
}

function rChoice() {
    bClose();
    uClose();
    rOpen();
    dClose();
}

function dChoice() {
    bClose();
    uClose();
    rClose();
    dOpen();
}

function bOpen() {
    $('#block')[0].style.display = "block";
}

function bClose() {
    $('#block')[0].style.display = "none";
}

function uOpen() {
    $('#unlock')[0].style.display = "block";
}

function uClose() {
    $('#unlock')[0].style.display = "none";
}

function rOpen() {
    $('#role')[0].style.display = "block";
}

function rClose() {
    $('#role')[0].style.display = "none";
}

function dOpen() {
    $('#delete')[0].style.display = "block";
}

function dClose() {
    $('#delete')[0].style.display = "none";
}
