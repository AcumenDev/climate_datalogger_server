$(function () {
    loadSensors();


    function loadSensors() {
        var template = Handlebars.compile($("#sensors-blank").html());
        $.getJSON("/api/sensors", function (value) {
            $("#sensors-list").html(template(value));
        }).fail(function () {
            console.log("Ошибка запроса");
        });
    }
});
