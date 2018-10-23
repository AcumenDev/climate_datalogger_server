var dashboard = {

    init: function ($) {
        this.$ = $;


        this.iniDateTimePicker();
        this.from = this.$('#datetimepickerFrom');
        this.to = this.$('#datetimepickerTo');


        this.chartItems = [];
        this.loadSensors();
        this.loadDashboard();

        this.$("a[id='drawChart']").bind("click", this.renderChart);
    },

    iniDateTimePicker: function () {

        this.$('#datetimepickerFrom').datetimepicker({
            format: "DD.MM.YYYY HH:mm",
            viewDate: moment().add(-1, 'day'),
            locale: 'ru',
            allowInputToggle: true,
            sideBySide: true,
            date: moment().add(-1, 'day').locale("ru")//.format("DD.MM.YYYY HH:mm")
        });

        this.$('#datetimepickerTo').datetimepicker({
            format: "DD.MM.YYYY HH:mm",
            locale: 'ru',
            viewDate: moment(),
            // useCurrent: true,
            allowInputToggle: true,
            sideBySide: true,
            date: moment().locale("ru")//.format("DD.MM.YYYY HH:mm")

        });
        this.$('#datetimepickerFrom').val(moment().add(-1, 'day').locale("ru").format("DD.MM.YYYY HH:mm"));
        this.$('#datetimepickerTo').val(moment().locale("ru").format("DD.MM.YYYY HH:mm"));


        this.$("#datetimepickerFrom").on("change.datetimepicker", function (e) {
            $('#datetimepickerTo').datetimepicker('minDate', e.date);
        });
        this.$("#datetimepickerTo").on("change.datetimepicker", function (e) {
            $('#datetimepickerFrom').datetimepicker('maxDate', e.date);
        });

    },


    loadSensors: function () {
        var template = Handlebars.compile(this.$("#sensors-blank").html());
        var self = this;
        self.$.getJSON("/api/sensors", function (value) {
            self.$("#sensors").html(template(value));
        });
    },


// {"data":{"id":1,"name":"Зал","items":[{"id":2,"name":"температура в зале"},{"id":3,"name":"testo"}]}}

    loadDashboard: function () {
        var template = Handlebars.compile($("#charts-blank").html());
        var self = this;
        $.getJSON("/api/dashboard", function (data) {
            $("#charts").html(template(data));

            var sensors = data.data.items;
            for (var i = 0; i < sensors.length; i++) {

                var id = sensors[i].id;


                var dataChart = self.initChart();


                var idChart = "chart_div_" + sensors[i].id;

                var chart = new Chart($("#" + idChart)[0].getContext('2d'), dataChart);


                var item = {id: id, data: dataChart, chart: chart};

                self.chartItems.push(item);
            }
        })

    },


    initChart: function () {
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

    },
    renderChart: function () {


        var from = $('#datetimepickerFrom').datetimepicker('viewDate').format("x");
        var to = $('#datetimepickerTo').datetimepicker('viewDate').format("x");




        ///todo списик сенсоров и интервал времени



        $.ajax({
            method: "GET",
            url: "/api/readings",
            data: {sensor_id: detail.query.id, sensor_type: detail.query.type, from: from, to: to}
        })
            .done(function (response) {


                detail.conf.data.labels = [];
                detail.conf.data.datasets[0].data = [];
                // console.log("data есть " + response);
                // alert("Data Saved: " + msg);
                var values = response.data;
                for (var i = 0; i < values.length; ++i) {
                    var item = values[i];
                    //data.addRow([new Date(item.dateTime), item.value]);
                    detail.conf.data.labels.push(new Date(item.dateTime).toLocaleString("ru-RU"));
                    detail.conf.data.datasets.forEach(function (dataset) {
                        dataset.data.push(item.value);
                    });
                    detail.chart.update();
                }
            });
    }
};

$(function () {
    dashboard.init($);
});
