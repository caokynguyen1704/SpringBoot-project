$(document).ready(function() {
    $('.reset-submit').click(function(event) {
        event.preventDefault();
        $(".mask").show();
        var password1 = $('input[name="password-reset"]').val();
        var password2 = $('input[name="password-repeat-reset"]').val();
        if (password1==password2){
            const url = new URL(window.location.href);
            const token = url.searchParams.get("token");
            console.log(token);
            var data = {
                "new_password": password1
            };
            $.ajax({
                url: "/api/v1/guest/reset-password/"+token,
                type: "POST",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(response) {
                    $(".mask").hide();
                    console.log(response);
                    $(".quentaikhoan").hide();
                    Swal.fire(
                        'Thành công',
                        'Đổi mật khẩu thành công vui lòng đăng nhập lại',
                        'success'
                    )
                    window.location.href="/"
                },
                error: function(error) {
                    Swal.fire(
                        'Lỗi',
                        error.responseJSON.message,
                        'error'
                    )
                    $(".mask").hide();
                    console.log(error);
                }
            });
        }else{
            Swal.fire(
                'Lỗi',"Password không giống nhau",
                'error'
            )
        }

    });
    $('.quentaikhoan-submit').click(function(event) {
        event.preventDefault();
        $(".mask").show();
        var email=$('input[name="email-quentaikhoan"]').val();
        var data = {
            "email":email
        };
        $.ajax({
            url: "/api/v1/guest/forget-password",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                $(".mask").hide();
                console.log(response);
                $(".dangky").hide();
                $(".dangnhap").show();
                $(".quentaikhoan").hide();
                Swal.fire(
                    'Thành công',
                    'Vui lòng kiểm tra email',
                    'success'
                )
            },
            error: function(error) {
                $(".mask").hide();
                console.log(error);
            }
        });
    });
    $('.dangky-submit').click(function(event) {
        event.preventDefault();
        $(".mask").show();
        var username = $('input[name="username-dangky"]').val();
        var password = $('input[name="password-dangky"]').val();
        var email=$('input[name="email-dangky"]').val();
        var data = {
            "username": username,
            "password": password,
            "email":email
        };
        $.ajax({
            url: "/api/v1/guest/signup",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                $(".mask").hide();
                console.log(response);
                $(".dangky").hide();
                $(".dangnhap").show();
                $(".quentaikhoan").hide();
                Swal.fire(
                    'Thành công',
                    'Đăng ký thành công vui lòng đăng nhập lại',
                    'success'
                )
            },
            error: function(error) {
                Swal.fire(
                    'Lỗi',
                    error.responseJSON.message,
                    'error'
                )
                $(".mask").hide();
                console.log(error);
            }
        });
    });
    $('.dangnhap-submit').click(function(event) {
        event.preventDefault();
        $(".mask").show();
        var username = $('input[name="username-login"]').val();
        var password = $('input[name="password-login"]').val();
        var data = {
            "username": username,
            "password": password
        };
        $.ajax({
            url: "/api/v1/guest/login",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                if (response.accessToken) {
                    localStorage.setItem('token', 'Bearer ' + response.accessToken);
                    $.ajaxSetup({
                        headers: {
                            'Authorization': 'Bearer ' + response.accessToken
                        }
                    });
                }
                $(".mask").hide();

                console.log(response);
                location.href="/"
            },
            error: function(error) {
                Swal.fire(
                    'Lỗi',
                    error.responseJSON.message,
                    'error'
                )
                $(".mask").hide();
                console.log(error);
            }
        });
    });
    $(".dangky-btn").click(function() {
        $(".dangky").show();
        $(".dangnhap").hide();
        $(".quentaikhoan").hide();
    });
    $(".dangnhap-btn").click(function() {
        $(".dangky").hide();
        $(".dangnhap").show();
        $(".quentaikhoan").hide();
    });
    $(".quentaikhoan-btn").click(function() {
        $(".dangky").hide();
        $(".dangnhap").hide();
        $(".quentaikhoan").show();
    });
});
