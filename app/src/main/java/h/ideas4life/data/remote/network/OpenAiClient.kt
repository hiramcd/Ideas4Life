package h.ideas4life.data.remote.network


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import h.ideas4life.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
object OpenAiClient {
    private const val BASE_URL = "https://api.openai.com/v1/"
    private const val APIKEY = BuildConfig.OPENAI_API_KEY


    val api: OpenAiApi by lazy {
        val json = Json {
            ignoreUnknownKeys = true // <-- clave para que no truene si la respuesta tiene campos extra
            encodeDefaults = true
            prettyPrint = true       // opcional, útil para debug
        }

        val contentType = "application/json".toMediaType()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $APIKEY")
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
            .create(OpenAiApi::class.java)
    }
}