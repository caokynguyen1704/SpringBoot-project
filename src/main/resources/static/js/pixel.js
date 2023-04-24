function readURL(input) {
    if (input.files && input.files[0]) {

        var reader = new FileReader();

        reader.onload = function(e) {
            $('.image-upload-wrap').hide();

            $('.file-upload-image').attr('src', e.target.result);
            $('.file-upload-content').show();

            $('.image-title').html(input.files[0].name);
        };

        reader.readAsDataURL(input.files[0]);

    } else {
        removeUpload();
    }
}

function removeUpload() {
    $('.file-upload-input').replaceWith($('.file-upload-input').clone());
    $('.file-upload-content').hide();
    $('.image-upload-wrap').show();
}
$('.image-upload-wrap').bind('dragover', function () {
    $('.image-upload-wrap').addClass('image-dropping');
});
$('.image-upload-wrap').bind('dragleave', function () {
    $('.image-upload-wrap').removeClass('image-dropping');
});
function chunkArray(array, chunkSize) {
    var chunks = [];
    for (var i = 0; i < array.length; i += chunkSize) {
        chunks.push(array.slice(i, i + chunkSize));
    }
    return chunks;
}
$(document).ready(function() {
    $('.submit-image').click(function() {
        $('.file-upload').hide();
        $('.loading').show();
        var file = $('input[name=file]')[0].files[0];
        var width = $('#width').val();
        var height = $('#height').val();
        var data = new FormData();
        data.append('file', file);
        data.append('width', width);
        data.append('height', height);
        $.ajax({
            url: '/getPixel',
            type: 'POST',
            data: data,
            processData: false,
            contentType: false,
            dataType: 'json',
            success: function(data) {
                $('.loading').hide();
                if (data['data']){
                    let arr=chunkArray(data['data'],data['width'])
                    console.log(arr)
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("Error")
            }
        });
    });
});

document.querySelectorAll('textarea').forEach(el => {
    el.style.height = el.setAttribute('style', 'height: ' + el.scrollHeight + 'px');
    el.classList.add('auto');
    el.addEventListener('input', e => {
        el.style.height = 'auto';
        el.style.height = (el.scrollHeight) + 'px';
    });
});