$(function () {

    formLogin("create");

    function formLogin(formName) {
        $(formName).submit(function (event) {
            event.preventDefault();

            var data = {};

            var $form = $(this);
            data.name = $form.find("input[name='name']").val();
            data.type = $form.find("input[name='type']").val();
            data.num = $form.find("input[name='num']").val();
            data.description = $form.find("input[name='description']").val();
            data.state = $form.find("input[name='state']").val();
            url = $form.attr("action");

            var posting = $.post(url, data,

                function () {
                    window.location.replace("/lk/sensors/list.html");
                });

            posting.fail(function () {
                console.log("Ошибка авторизации")
            });
        });
    }
});