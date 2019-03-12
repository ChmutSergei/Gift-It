function sendMessage() {
    var form = $('#form')[0];
    if (form.checkValidity()) {
        var question = $('#question')[0].value;
        var command = 'ACCEPT_QUESTION';
        $.ajax({
            type: 'POST',
            data: {command: command, question: question},
            url: contextUrl + '/ajax'
        }).done(function (data) {
            if (data == 'true') {
                $('#message')[0].style.display = "block";
            } else {
                $('#ermessage')[0].style.display = "block";
            }
            $('#post')[0].style.display = "none";
        }).fail(function (data) {
            if (console && console.log) {
                console.log("Error data:", data);
            }
        })
    } else {
        form.reportValidity();
    }
}