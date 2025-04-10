package cat.copernic.frontend.auth_management.data.management

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt_token", null)

        val request = chain.request().newBuilder().apply {
            if (token != null) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(request)
    }
}
