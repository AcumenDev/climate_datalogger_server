var dashboard = {

    init: function ($) {
        this.$ = $;
        this.items = [];
        this.loadDashboard();

    },

    loadDashboard: function () {
        var template = Handlebars.compile($("#charts-blank").html());
        var self = this;
        $.getJSON("/api/dashboard", function (data) {
            $("#charts").html(template(data));
            var sensors = data.data.items;
            for (var i = 0; i < sensors.length; i++) {
                var id = sensors[i].id;
                self.items.push({sensorId: id, blockId: "dash_board_item_" + id});
            }
            self.refreshItems();
        })
    },

    refreshItems: function () {

        var sensors = [];
        for (var i = 0; i < dashboard.items.length; i++) {
            var item = dashboard.items[i];
            sensors.push(item.sensorId)
        }

        $.ajax({
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            method: "POST",
            url: "/api/dashboard/values/get",
            data: JSON.stringify({sensorIds: sensors})
        })
            .done(function (response) {
                //detail.conf.data.labels = [];
                //detail.conf.data.datasets[0].data = [];
                console.log("data есть " + response);
                console.log(response);
                // alert("Data Saved: " + msg);
            });



    }
};

$(function () {
    dashboard.init($);
});
