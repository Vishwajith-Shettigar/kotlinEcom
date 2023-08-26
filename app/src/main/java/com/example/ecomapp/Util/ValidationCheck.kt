package com.example.ecomapp.Util

import android.util.Patterns
import java.util.regex.Pattern

fun validateEmail(email:String) :RegisterValidation{

if(email.isEmpty()){

    return RegisterValidation.Failure("Email cannot be empty");

}
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return RegisterValidation.Failure("Enter correct email");
    }

    return RegisterValidation.Success;

}

fun validatePassword(password:String) :RegisterValidation{

    if(password.isEmpty()){

        return RegisterValidation.Failure("Password cannot be empty");

    }
    if(password.length<6){
        return RegisterValidation.Failure("Enter strong password");
    }

    return RegisterValidation.Success;

}