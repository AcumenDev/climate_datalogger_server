$(function () {

    Handlebars.registerHelper("prettifyDate", function (timestamp) {
        return new Date(timestamp).toLocaleString("ru-RU")
    });


    loadSensors();


    google.charts.load('current', {
        packages: ['corechart', 'line']
    });
    google.charts.setOnLoadCallback(loadInfo);


    function loadInfo() {

        $.ajax({
            method: "GET",
            url: "/api/readings",
            data: {sensor_id: "2", sensor_type: 1}
        })
            .done(function (response) {
                // alert("Data Saved: " + msg);


                var data = new google.visualization.DataTable();
                data.addColumn('datetime', 'X');
                data.addColumn('number', 'T');
                /*data.addColumn('number', 'H');
                data.addColumn('number', 'Точка росы');*/
                var values = response.data;
                for (var i = 0; i < values.length; ++i) {
                    var item = values[i];
                    data.addRow([new Date(item.dateTime), item.value]);

                }


                /*         data.addRows([
                             [new Date(2017, 12, 21, 12, 30, 0), 18.59],
                             [new Date(2017, 12, 21, 12, 31, 0), 17.59],
                             [new Date(2017, 12, 21, 12, 32, 0), 16.59],
                             [new Date(2017, 12, 21, 12, 33, 0), 15.59],
                             [new Date(2017, 12, 21, 12, 34, 0), 14.59],
                             [new Date(2017, 12, 21, 12, 35, 0), 14.59]
                         ]);*/


                var options = {
                    hAxis: {
                        title: 'Время'
                    },
                    vAxis: {
                        title: 'Значение'
                    },
                    width: 1000,
                    height: 400
                };

                var chart = new google.visualization.LineChart(document.getElementById('chart_div'));

                chart.draw(data, options);

            });
    }


    function loadSensors() {
        var template = Handlebars.compile($("#sensors-blank").html());
        $.getJSON("/api/sensors", function (value) {
            $("#sensors").html(template(value));
        });
    }
});
