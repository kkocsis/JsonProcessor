function connect() {
    var socket = new SockJS('/messages-websocket');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/message', function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message.id + "</td><td>" + message.createDate + "</td><td>" + JSON.stringify(message.message) + "</td></tr>");
}

$(function () {
    connect();
});
