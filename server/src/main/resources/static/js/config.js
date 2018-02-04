$(function () {
    Handlebars.registerHelper("prettifyDate", function (timestamp) {
        if (timestamp != null) {
            return new Date(timestamp).toLocaleString("ru-RU")
        }
    });


    $.urlParam = function (name) {
        var results = new RegExp('[\?&]' + name + '=([^]*)').exec(window.location.href);
        if (results == null) {
            return null;
        }
        else {
            return results[1] || 0;
        }
    }
});