package h.ideas4life

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import h.ideas4life.data.remote.repository.IdeaRepository
import h.ideas4life.data.remote.services.FirestoreService
import h.ideas4life.ui.navigation.Routes
import h.ideas4life.ui.theme.Ideas4LifeTheme
import h.ideas4life.ui.view.HomeScreen
import h.ideas4life.ui.view.IdeaScreen
import h.ideas4life.ui.view.NewIdeaScreen
import h.ideas4life.viewmodel.IdeaViewModel

class MainActivity : ComponentActivity() {
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val firestoreService = FirestoreService()
        val repository = IdeaRepository(firestoreService)
        val viewModel = IdeaViewModel(repository)
        permissionManager = PermissionManager(this)

        if (!permissionManager.hasAudioPermission()) {
            permissionManager.requestAudioPermission(AUDIO_REQUEST_CODE)
        }
        enableEdgeToEdge()
        setContent {
            Ideas4LifeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.Home){
                    composable(Routes.Home) {
                        HomeScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    fun testApp(){

        Firebase.firestore.collection("test_connection")
            .add(mapOf("mensaje" to "Conectado", "timestamp" to FieldValue.serverTimestamp()))
            .addOnSuccessListener {
                Log.d("FIREBASE", "✅ Conexión correcta con Firestore")
            }
            .addOnFailureListener {
                Log.e("FIREBASE", "❌ Error al conectar Firestore", it)
            }


        Firebase.firestore.collection("test_connection")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val timestamp = document.getTimestamp("timestamp")
                    Log.d("FIREBASE_READ", "ID: ${document.id}, timestamp: $timestamp")
                }
            }
            .addOnFailureListener {
                Log.e("FIREBASE_READ", "Error al leer documentos", it)
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == AUDIO_REQUEST_CODE) {
            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                Toast.makeText(this, "Se requiere permiso de micrófono", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val AUDIO_REQUEST_CODE = 100
    }
    }
