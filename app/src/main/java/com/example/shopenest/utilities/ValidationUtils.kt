package com.example.shopenest.utilities

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateUserInput(
        context: Context,
        email: String,
        password: String,
        confirmPassword: String? = null,
        isSignUp: Boolean = false
    ): Boolean {
        return when {
            !isValidEmail(email) -> {
                Toast.makeText(context, "Invalid email format!", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(context, "Please enter password!", Toast.LENGTH_SHORT).show()
                false
            }
            isSignUp && confirmPassword.isNullOrEmpty() -> {
                Toast.makeText(context, "Please confirm password!", Toast.LENGTH_SHORT).show()
                false
            }
            isSignUp && confirmPassword != password -> {
                Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }





}
