$(function () {
    Handlebars.registerHelper("prettifyDate", function (timestamp) {
        return new Date(timestamp).toLocaleString("ru-RU")
    });
});