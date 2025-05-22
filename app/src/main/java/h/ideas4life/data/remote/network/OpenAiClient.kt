package h.ideas4life.data.remote.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object OpenAiClient {
    private const val BASE_URL = "https://api.openai.com/v1/"
    private const val API_KEY = "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"

    val api: OpenAiApi by lazy {
        val json = Json {
            ignoreUnknownKeys = true // <-- clave para que no truene si la respuesta tiene campos extra
            prettyPrint = true       // opcional, útil para debug
            isLenient = true         // opcional, más permisivo con el formato
        }

        val contentType = "application/json".toMediaType()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $API_KEY")
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