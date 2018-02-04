$(function () {

    formLogin("#create");

    function formLogin(formName) {
        $(formName).submit(function (event) {
            event.preventDefault();

            var data = {};

            var $form = $(this);
            data.name = $form.find("input[name='name']").val();
            data.type = $form.find("select[name='type']").val();
            data.num = $form.find("input[name='num']").val();
            data.description = $form.find("input[name='description']").val();
            data.state = $form.find("input[name='state']").prop("checked");
            url = $form.attr("action");

            $.ajax({
                type: "POST",
                url: url,
                // The key needs to match your method's input parameter (case-sensitive).
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    window.location.replace("/lk/sensors/list.html");
                },
                fail: function (errMsg) {
                    console.log("Ошибка запроса " + errMsg);
                }
            });
        });
    }
});