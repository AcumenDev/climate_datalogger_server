$(function () {

    formLogin("#login-form");

    function formLogin(formName) {
        $(formName).submit(function (event) {
            event.preventDefault();

            var $form = $(this),
                username = $form.find("input[name='username']").val(),
                password = $form.find("input[name='password']").val(),
                url = $form.attr("action");

            var posting = $.post(url, {username: username, password: password},

                function () {
                    window.location.replace("/lk/index.html");
                });

            posting.fail(function () {
                console.log("Ошибка авторизации")
            });
        });
    }
});