$(function () {


    loadSensorDatail($.urlParam("id"));

    var conf = init();

    var chart = new Chart($("#chart_div")[0].getContext('2d'), conf);


    function loadSensorDatail(sensorId) {
        var template = Handlebars.compile($("#sensors-detail").html());
        $.getJSON("/api/sensors/" + sensorId, function (value) {
            if (value.hasOwnProperty("data")) {
                // console.log("data есть " + value);
                $("#main").html(template(value.data));
                renderChart(value.data.id, value.data.type, conf, chart);
            } else {
                console.log("Ошибка запроса");
            }
        }).fail(function () {
            console.log("Ошибка запроса");
        });
    }


    function init() {
        return {
            type: 'line',
            data: {
                labels: [],
                datasets: [{
                    label: "Температура",
                     backgroundColor: 'rgb(255, 99, 132)',
                    borderColor: 'rgb(255, 99, 132)',
                    data: [
                        /* randomScalingFactor(),
                         randomScalingFactor(),
                         randomScalingFactor(),
                         randomScalingFactor(),
                         randomScalingFactor(),
                         randomScalingFactor(),
                         randomScalingFactor()*/
                    ],
                    fill: false
                }]
            },
            options: {
                responsive: true,
                title: {
                    display: true,
                    text: ''
                },
                tooltips: {
                    mode: 'index',
                    intersect: false
                },
                hover: {
                    mode: 'nearest',
                    intersect: true
                },
                scales: {
                    xAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Время'
                        }
                    }],
                    yAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Значение'
                        }
                    }]
                }
            }
        };

    }


    function renderChart(id, type, conf, chart) {

        $.ajax({
            method: "GET",
            url: "/api/readings",
            data: {sensor_id: id, sensor_type: type}
        })
            .done(function (response) {
                console.log("data есть " + response);
                // alert("Data Saved: " + msg);
                var values = response.data;
                for (var i = 0; i < values.length; ++i) {
                    var item = values[i];
                    //data.addRow([new Date(item.dateTime), item.value]);
                    conf.data.labels.push(new Date(item.dateTime).toLocaleString("ru-RU"));
                    conf.data.datasets.forEach(function (dataset) {
                        dataset.data.push(item.value);
                    });
                }
                chart.update();

            });
    }


});