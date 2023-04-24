
$(document).ready(function() {

    if (document.cookie
        .split('; ')
        .find(row => row.startsWith('Authorization='))){
        const token=document.cookie
            .split('; ')
            .find(row => row.startsWith('Authorization='))
            .split('=')[1];

        if ((token)) {
            console.log(token)
            $("body").show()
            if(location.pathname=='/login'){
                window.location.href = "/";
            }
        }else{
            if(location.pathname=='/login'){
            }else{
                window.location.href = "/login";
            }

        }
    }else{
        if(location.pathname=='/login'){
        }else{
            window.location.href = "/login";
        }
    }

});
