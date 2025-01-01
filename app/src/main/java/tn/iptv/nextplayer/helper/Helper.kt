import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {

    private const val PREF_NAME = "user_preferences"
    private const val KEY_IS_LOGIN = "is_login"
    private const val KEY_ACTIVATION_CODE = "activation_code"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLoginStatus(context: Context, isLoggedIn: Boolean, activationCode: String) {
        val sharedPreferences = getSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_LOGIN, isLoggedIn)
            putString(KEY_ACTIVATION_CODE, activationCode)
            apply()
        }
    }

    fun isUserLoggedIn(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean(KEY_IS_LOGIN, false)
    }

    fun getActivationCode(context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(KEY_ACTIVATION_CODE, "")
    }

    fun logoutUser(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_LOGIN, false)  // Clear login status
            apply()
        }
    }
}
