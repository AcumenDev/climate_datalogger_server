$(function () {
    Handlebars.registerHelper("prettifyDate", function (timestamp) {
        if (timestamp != null) {
            return new Date(timestamp).toLocaleString("ru-RU")
        }
    });


    $.urlParam = function () {

        var queryDict = {};
        location.search.substr(1).split("&").forEach(function(item) {queryDict[item.split("=")[0]] = item.split("=")[1]});
return queryDict;

        /* var results = new RegExp('[\?&]' + name + '=([^]*)').exec(window.location.href);
         if (results == null) {
             return null;
         }
         else {
             return results[1] || 0;
         }*/
    }
});