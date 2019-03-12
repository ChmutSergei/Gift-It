$(function () {
    $('form').keydown(function (event) {
        if (event.keyCode == 13) {
            event.preventDefault();
            return false;
        }
    });
});

var password = document.getElementById("inputPassword")
    , confirmPassword = document.getElementById("confirmPassword");
function validatePassword(){
    if(password.value != confirmPassword.value) {
        confirmPassword.setCustomValidity(messageDontMatch);
    } else {
        confirmPassword.setCustomValidity('');
    }
}

password.onchange = validatePassword;
confirmPassword.onkeyup = validatePassword;

check.onclick = function () {
    var username = $('#login')[0].value;
    var submit = $('#check2')[0];
    var command = 'CHECK_USERNAME';
    $.ajax({
        type: 'POST',
        data: {command: command, username:username},
        url: contextUrl + '/ajax'
    }).done(function (data) {
        if (data != 'true') {
            submit.click();
            return;
        }
        $('#message')[0].style.display="block";
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
};

login.onclick = function () {
    $('#message')[0].style.display="none";
};