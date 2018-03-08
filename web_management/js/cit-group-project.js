$(document).ready(function(){


    $('#register-pwd').keyup(function() {
        var pswd = $(this).val();

        //validate the length
        if ( pswd.length < 8 ) {
            $('#length').removeClass('valid').addClass('invalid');
        } else {
            $('#length').removeClass('invalid').addClass('valid');
        }

        //validate letter
        if ( pswd.match(/[A-z]/) ) {
            $('#letter').removeClass('invalid').addClass('valid');
        } else {
            $('#letter').removeClass('valid').addClass('invalid');
        }

        //validate capital letter
        if ( pswd.match(/[A-Z]/) ) {
            $('#capital').removeClass('invalid').addClass('valid');
        } else {
            $('#capital').removeClass('valid').addClass('invalid');
        }

        //validate number
        if ( pswd.match(/\d/) ) {
            $('#number').removeClass('invalid').addClass('valid');
        } else {
            $('#number').removeClass('valid').addClass('invalid');
        }

        //validate space
        if ( pswd.match(/[^a-zA-Z0-9\-\/]/) ) {
            $('#space').removeClass('invalid').addClass('valid');
        } else {
            $('#space').removeClass('valid').addClass('invalid');
        }

    }).focus(function() {
        $('#pswd_info').show();
    }).blur(function() {
        $('#pswd_info').hide();
    });

    $('#register-rpwd').keyup(function() {
        var pswd = $(this).val();

        //validate the length
        if ( pswd.length < 8 ) {
            $('#length_2').removeClass('valid').addClass('invalid');
        } else {
            $('#length_2').removeClass('invalid').addClass('valid');
        }

        //validate letter
        if ( pswd.match(/[A-z]/) ) {
            $('#letter_2').removeClass('invalid').addClass('valid');
        } else {
            $('#letter_2').removeClass('valid').addClass('invalid');
        }

        //validate capital letter
        if ( pswd.match(/[A-Z]/) ) {
            $('#capital_2').removeClass('invalid').addClass('valid');
        } else {
            $('#capital_2').removeClass('valid').addClass('invalid');
        }

        //validate number
        if ( pswd.match(/\d/) ) {
            $('#number_2').removeClass('invalid').addClass('valid');
        } else {
            $('#number_2').removeClass('valid').addClass('invalid');
        }

        //validate space
        if ( pswd.match(/[^a-zA-Z0-9\-\/]/) ) {
            $('#space_2').removeClass('invalid').addClass('valid');
        } else {
            $('#space_2').removeClass('valid').addClass('invalid');
        }

    }).focus(function() {
        $('#pswd_info_2').show();
    }).blur(function() {
        $('#pswd_info_2').hide();
    });

    $("#reg").click(function() {
        $('#wrong_email').hide();
        $('#wrong_rpwd').hide();
        $('#wrong_pwd').hide();
        $('#register_feedback').hide();

        var email = $("#email").val();
        var pwd = $("#register-pwd").val();
        var rpwd = $("#register-rpwd").val();

        if(email.length>3){
            var pattern = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
            var pass = $.trim(email).match(pattern) ? true : false;
            if(!pass)
            {
                $('#wrong_email').show();
                return false;
            }
        }

        if(pwd!=rpwd)
        {
            $('#wrong_rpwd').show();
            return false;
        }

        if ( pwd.length < 8 || !pwd.match(/[A-z]/) || !pwd.match(/[A-Z]/) || !pwd.match(/\d/) || !pwd.match(/[^a-zA-Z0-9\-\/]/) ) {
            $('#wrong_pwd').show();
            return false;
        }
    });



});