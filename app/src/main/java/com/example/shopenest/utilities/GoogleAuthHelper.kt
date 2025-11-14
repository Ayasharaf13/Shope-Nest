package com.example.shopenest.utilities

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.shopenest.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

// GoogleAuthHelper.kt
class GoogleAuthHelper(
    private val context: Context,


    private val lifecycleOwner: LifecycleOwner,
    private val onSuccess: (FirebaseUser?) -> Unit,
    private val onError: (String) -> Unit,


) {

    private val auth = FirebaseAuth.getInstance()
    private  lateinit var credentialManager:CredentialManager
    private val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(context.getString(R.string.default_web_client_id))
        .setFilterByAuthorizedAccounts(false)
        .build()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id)) // Replace with your actual Web Client ID
        .requestEmail()
        .build()

    private val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    var googleSignInClient = GoogleSignIn.getClient(context, gso)



    suspend fun startGoogleSignIn() {
        credentialManager = CredentialManager.create(context)

        (lifecycleOwner as? LifecycleOwner)?.lifecycleScope?.launch {
            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request

                )
                handleSignIn(result.credential)
            } catch (e: Exception) {
                onError("Sign-in failed: ${e.localizedMessage}")
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential &&
            credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            onError("Credential is not of type Google ID.")
        }
    }



    private fun firebaseAuthWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser)

                } else {
                    onError("Firebase auth failed: ${task.exception?.message}")
                }
            }
    }
}
