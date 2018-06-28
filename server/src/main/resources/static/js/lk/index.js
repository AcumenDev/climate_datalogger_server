$(function () {

    loadSensors();
    loadDashboard();
    function loadSensors() {
        var template = Handlebars.compile($("#sensors-blank").html());
        $.getJSON("/api/sensors", function (value) {
            $("#sensors").html(template(value));
        });
    }




   // {"data":{"id":1,"name":"Зал","items":[{"id":2,"name":"температура в зале"},{"id":3,"name":"testo"}]}}

    function loadDashboard() {
        var template = Handlebars.compile($("#charts-blank").html());
        $.getJSON("/api/dashboard",function (data) {
            $("#charts").html(template(data));
        })
    }
});
