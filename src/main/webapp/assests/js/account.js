$(document).ready(function () {
    eOpen();
    sClose();
    cClose();
    qClose();
    eAdd();
    $('#edit').click(function () {
        eOpen();
        sClose();
        cClose();
        qClose();
        eAdd();
        sRem();
        cRem();
        qRem();
    });
    $('#story').click(function () {
        eClose();
        sOpen();
        cClose();
        qClose();
        eRem();
        sAdd();
        cRem();
        qRem();
    });
    $('#comments').click(function () {
        eClose();
        sClose();
        cOpen();
        qClose();
        eRem();
        sRem();
        cAdd();
        qRem();
    });
    $('#questions').click(function () {
        eClose();
        sClose();
        cClose();
        qOpen();
        eRem();
        sRem();
        cRem();
        qAdd();
    });
});

function eAdd() {
    $('#edit').addClass('active');
}

function eRem() {
    $('#edit').removeClass('active');
}

function sAdd() {
    $('#story').addClass('active');
}

function sRem() {
    $('#story').removeClass('active');
}

function cAdd() {
    $('#comments').addClass('active');
}

function cRem() {
    $('#comments').removeClass('active');
}

function qAdd() {
    $('#questions').addClass('active');
}

function qRem() {
    $('#questions').removeClass('active');
}

check.onclick = function () {
    var form = $('#form')[0];
    if (form.checkValidity()) {
        var phone = $('#phoneNumber')[0].value;
        var address = $('#address')[0].value;
        var command = 'UPDATE_USER_ADDRESS_PHONE';
        $.ajax({
            type: 'POST',
            data: {command: command, address: address, phone: phone},
            url: contextUrl + '/ajax'
        }).done(function (data) {
            var resp = JSON.parse(data);
            if (resp.phone != 'ERROR') {
                var newPhone = resp.phone;
                var newAddress = resp.address;
                $('#phoneArea').text(newPhone);
                $('#addressArea').text(newAddress);
            } else {
                $('#message')[0].style.display = "block";
            }
        }).fail(function (data) {
            if (console && console.log) {
                console.log("Error data:", data);
            }
        })

    } else {
        form.reportValidity();
    }
};
$('body').click(function () {
    $('#message')[0].style.display = "none";
});

function eClose() {
    $('.edit')[0].style.display = "none";
}

function eOpen() {
    $('.edit')[0].style.display = "block";
}

function sClose() {
    $('.story')[0].style.display = "none";
}

function sOpen() {
    $('.story')[0].style.display = "block";
}

function cClose() {
    $('.comments')[0].style.display = "none";
}

function cOpen() {
    $('.comments')[0].style.display = "block";
}

function qClose() {
    $('.questions')[0].style.display = "none";
}

function qOpen() {
    $('.questions')[0].style.display = "block";
}

function sendItemId(element) {
    var itemId = $(element).attr('name');
    var command = 'SET_ITEM_ID';
    $.ajax({
        type: 'POST',
        data: {command: command, itemId: itemId},
        url: contextUrl + '/ajax'
    }).done(function () {
        location.href = contextUrl + '/controller?command=preview_item';
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}

function sendId(element) {
    var commentId = $(element).attr('about');
    var command = 'DELETE_COMMENT';
    $.ajax({
        type: 'POST',
        data: {command: command, commentId: commentId},
        url: contextUrl + '/ajax'
    }).done(function (data) {
        if (data == 'true') {
            $('#d' + commentId)[0].style.display = "block";
        }
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}