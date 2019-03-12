upload.onclick = function () {
    var data = new FormData($('#upload-form')[0]);
    $.ajax({
        type: 'POST',
        data: data,
        url: contextUrl + '/upload',
        cache: false,
        contentType: false,
        processData: false
    });
};