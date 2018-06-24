$(function () {

    loadSensors();

    function loadSensors() {
        var template = Handlebars.compile($("#sensors-blank").html());
        $.getJSON("/api/sensors", function (value) {
            $("#sensors").html(template(value));
        });
    }
});
