$(function () {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    if (dd < 10) {
        dd = '0' + dd
    }

    if (mm < 10) {
        mm = '0' + mm
    }

    today = dd + '.' + mm + '.' + yyyy;
    // initialization datetimepicker7 and datetimepicker8
    $("#datetimepicker1").datetimepicker({
        format: 'DD.MM.YYYY'
    });
    $('#datetimepicker1').data("DateTimePicker").minDate(moment(today, 'DD.MM.YYYY'));

    $("#datetimepicker2").datetimepicker({
        format: 'DD.MM.YYYY',
        useCurrent: false

    });

    $("#datetimepicker1").on("dp.change", function (e) {
        $('#datetimepicker2').data("DateTimePicker").minDate(e.date);
    });
    $("#datetimepicker2").on("dp.change", function (e) {
        $('#datetimepicker1').data("DateTimePicker").maxDate(e.date);
    });
});
$(function () {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    if (dd < 10) {
        dd = '0' + dd
    }

    if (mm < 10) {
        mm = '0' + mm
    }

    today = dd + '.' + mm + '.' + yyyy;
    // initialization datetimepicker7 and datetimepicker8
    $("#datetimepicker7").datetimepicker({
        format: 'DD.MM.YYYY'
    });
    $('#datetimepicker7').data("DateTimePicker").minDate(moment(today, 'DD.MM.YYYY'));

    $("#datetimepicker8").datetimepicker({
        format: 'DD.MM.YYYY',
        useCurrent: false

    });

    $("#datetimepicker7").on("dp.change", function (e) {
        $('#datetimepicker8').data("DateTimePicker").minDate(e.date);
    });
    $("#datetimepicker8").on("dp.change", function (e) {
        $('#datetimepicker7').data("DateTimePicker").maxDate(e.date);
    });
});


