$(document).ready(function () {
    var count = 1;
    var countEl = document.getElementById("count");
    $('#moins').click(function () {
        if (count > 1) {
            count--;
            countEl.value = count;
        }
    });
    $('#plus').click(function () {
        count++;
        countEl.value = count;
    });
});

function sendItem() {
    var count = document.getElementById("count").value;
    var command = 'ADD_TO_CART';
    $.ajax({
        type: 'POST',
        data: {command: command, count: count},
        url: contextUrl + '/ajax'
    }).done(function () {
        location.href = contextUrl + '/controller?command=cart';
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}
function sendComment() {
    var form = $('#form')[0];
    var comment = document.getElementById("dcomment").value;
    if (form.checkValidity()) {
        var command = 'ADD_COMMENT';
        $.ajax({
            type: 'POST',
            data: {command: command, comment: comment},
            url: contextUrl + '/ajax'
        }).done(function (data) {
            if (data == 'true') {
                document.getElementById("success").style.display = "block";
                document.getElementById("input").style.display = "none";
            }
        }).fail(function (data) {
            if (console && console.log) {
                console.log("Error data:", data);
            }
        })
    } else {
        form.reportValidity();
    }
}
function addComment() {
    if (exist) {
        document.getElementById("input").style.display = "block";
    } else {
        document.getElementById("error").style.display = "block";
    }
}