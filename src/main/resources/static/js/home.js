$(document).ready(function() {
    $('.logout-submit').click(function(event) {


        $.ajax({
            url: "/api/v1/user/logout",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {

                console.log(response);
                location.reload()

            },
            error: function(error) {
                console.log(error);
            }
        });
    });
    $('.profile-submit').click(function(event) {


        $.ajax({
            url: "/api/v1/user/profile",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {

                console.log(response);
            },
            error: function(error) {
                console.log(error);
            }
        });
    });
});
