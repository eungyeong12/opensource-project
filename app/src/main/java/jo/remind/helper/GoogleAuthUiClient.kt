package jo.remind.helper

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import jo.remind.R
import jo.remind.data.model.User
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val signInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    suspend fun signInWithIntent(intent: Intent?): Boolean {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        return try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()

            val currentUser = auth.currentUser
            currentUser?.let {
                val user = User(uid = it.uid, email = it.email ?: "")
                firestore.collection("users").document(it.uid).set(user).await()
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun signOut() {
        auth.signOut()
        signInClient.signOut()
    }

    fun getSignedInUser() = auth.currentUser
}
