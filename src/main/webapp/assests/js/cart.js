function sendCartId(element) {
    var cartId = $(element).attr('about');
    var command = 'DELETE_FROM_CART';
    $.ajax({
        type: 'POST',
        data: {command: command, cartId: cartId},
        url: contextUrl + '/ajax'
    }).done(function () {
        location.href = contextUrl + '/controller?command=cart';
    }).fail(function (data) {
        if (console && console.log) {
            console.log("Error data:", data);
        }
    })
}