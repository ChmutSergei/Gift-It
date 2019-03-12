$('.comment').css('cursor', 'pointer');

function sendId(element) {
    var commentId = $(element).attr('name');
    var command = 'ACCEPT_COMMENT';
    $.ajax({
        type: 'POST',
        data: {command: command, commentId: commentId},
        url: contextUrl + '/ajax'
    }).done(function (data) {
        if (data == 'true') {
            $('#ok' + commentId)[0].style.color = "chartreuse";
            $('#d' + commentId)[0].style.display = "block";
            $('#i' + commentId).addClass('radio disabled');
        }
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}