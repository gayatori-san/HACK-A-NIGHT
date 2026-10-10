package com.example.data.supabase

import android.util.Log
import com.example.BuildConfig
import com.example.data.models.TransactionItem
import com.example.data.models.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object SupabaseService {
    private const val TAG = "SupabaseService"

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    fun isConfigured(): Boolean {
        return try {
            val url = getProjectUrl()
            val key = getApiKey()
            url.isNotBlank() &&
                    !url.contains("your-project") &&
                    url.startsWith("http") &&
                    key.isNotBlank() &&
                    !key.contains("your-anon-key")
        } catch (e: Exception) {
            false
        }
    }

    fun getProjectUrl(): String {
        return try {
            val raw = BuildConfig.SUPABASE_URL.trim()
            if (raw.isBlank() || raw.contains("your-project")) return ""

            // 1. If user pasted Supabase dashboard URL, e.g.:
            // https://supabase.com/dashboard/project/ztohgnzsyoprulffsrya/settings/api-keys
            val dashboardRegex = Regex("""supabase\.com/dashboard/project/([a-zA-Z0-9_-]+)""")
            val dashboardMatch = dashboardRegex.find(raw)
            if (dashboardMatch != null) {
                val projectRef = dashboardMatch.groupValues[1]
                return "https://$projectRef.supabase.co"
            }

            // 2. If URL contains .supabase.co
            val supabaseCoRegex = Regex("""https?://([a-zA-Z0-9_-]+)\.supabase\.co""")
            val coMatch = supabaseCoRegex.find(raw)
            if (coMatch != null) {
                val projectRef = coMatch.groupValues[1]
                return "https://$projectRef.supabase.co"
            }

            // 3. Raw project ID (e.g. 20 alphanumeric characters)
            if (raw.matches(Regex("^[a-zA-Z0-9_-]{15,30}$"))) {
                return "https://$raw.supabase.co"
            }

            // 4. Default URL cleanup
            if (raw.startsWith("http://") || raw.startsWith("https://")) {
                raw.trimEnd('/')
            } else {
                "https://${raw.trimEnd('/')}"
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun getApiKey(): String {
        return try {
            BuildConfig.SUPABASE_KEY.trim()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun fetchTransactions(): List<TransactionItem>? = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext null
        try {
            val projectUrl = getProjectUrl()
            if (projectUrl.isBlank()) return@withContext null
            val url = "$projectUrl/rest/v1/transactions?select=*&order=id.desc"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", getApiKey())
                .addHeader("Authorization", "Bearer ${getApiKey()}")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (response.isSuccessful && !body.isNullOrBlank()) {
                val trimmed = body.trim()
                if (trimmed.startsWith("[")) {
                    val array = JSONArray(trimmed)
                    val list = mutableListOf<TransactionItem>()
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        list.add(
                            TransactionItem(
                                id = obj.optLong("id", System.currentTimeMillis()),
                                name = obj.optString("name", "Unknown"),
                                amount = obj.optDouble("amount", 0.0),
                                cat = obj.optString("cat", "essential"),
                                date = obj.optString("date", "Recently"),
                                colorHex = obj.optLong("color_hex", 0xFF5E2BFF),
                                healthImpact = obj.optString("health_impact", "NEUTRAL"),
                                context = obj.optString("context", ""),
                                necessity = obj.optString("necessity", ""),
                                foodType = obj.optString("food_type", "")
                            )
                        )
                    }
                    return@withContext list
                } else if (trimmed.startsWith("{")) {
                    val obj = JSONObject(trimmed)
                    Log.w(TAG, "Supabase API returned object message: ${obj.optString("message", trimmed.take(100))}")
                    return@withContext null
                } else {
                    Log.w(TAG, "Supabase endpoint returned non-JSON response (HTTP ${response.code}, starts with: ${trimmed.take(60).replace("\n", " ")})")
                    return@withContext null
                }
            } else {
                Log.w(TAG, "fetchTransactions failed: HTTP ${response.code} body: ${body?.take(100)}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching transactions from Supabase: ${e.message}", e)
        }
        return@withContext null
    }

    suspend fun insertTransaction(txn: TransactionItem): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext false
        try {
            val projectUrl = getProjectUrl()
            if (projectUrl.isBlank()) return@withContext false
            val url = "$projectUrl/rest/v1/transactions"
            val json = JSONObject().apply {
                put("id", txn.id)
                put("name", txn.name)
                put("amount", txn.amount)
                put("cat", txn.cat)
                put("date", txn.date)
                put("color_hex", txn.colorHex)
                put("health_impact", txn.healthImpact)
                put("context", txn.context)
                put("necessity", txn.necessity)
                put("food_type", txn.foodType)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", getApiKey())
                .addHeader("Authorization", "Bearer ${getApiKey()}")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "resolution=merge-duplicates")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            if (response.isSuccessful) {
                return@withContext true
            } else {
                Log.w(TAG, "insertTransaction failed: HTTP ${response.code} body: ${responseBody?.take(150)}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting transaction to Supabase: ${e.message}", e)
        }
        return@withContext false
    }

    suspend fun deleteTransaction(txnId: Long): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext false
        try {
            val projectUrl = getProjectUrl()
            if (projectUrl.isBlank()) return@withContext false
            val url = "$projectUrl/rest/v1/transactions?id=eq.$txnId"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", getApiKey())
                .addHeader("Authorization", "Bearer ${getApiKey()}")
                .delete()
                .build()

            val response = client.newCall(request).execute()
            return@withContext response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting transaction from Supabase: ${e.message}", e)
        }
        return@withContext false
    }

    suspend fun fetchProfile(): JSONObject? = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext null
        try {
            val projectUrl = getProjectUrl()
            if (projectUrl.isBlank()) return@withContext null
            val url = "$projectUrl/rest/v1/user_profile?select=*&limit=1"
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", getApiKey())
                .addHeader("Authorization", "Bearer ${getApiKey()}")
                .addHeader("Accept", "application/json")
                .get()
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (response.isSuccessful && !body.isNullOrBlank()) {
                val trimmed = body.trim()
                if (trimmed.startsWith("[")) {
                    val array = JSONArray(trimmed)
                    if (array.length() > 0) {
                        return@withContext array.getJSONObject(0)
                    } else {
                        return@withContext null
                    }
                } else if (trimmed.startsWith("{")) {
                    val obj = JSONObject(trimmed)
                    if (!obj.has("message") && !obj.has("error")) {
                        return@withContext obj
                    } else {
                        Log.w(TAG, "Supabase profile message: ${obj.optString("message", trimmed.take(100))}")
                    }
                } else {
                    Log.w(TAG, "Supabase profile returned non-JSON response (HTTP ${response.code}, starts with: ${trimmed.take(60).replace("\n", " ")})")
                }
            } else {
                Log.w(TAG, "fetchProfile failed: HTTP ${response.code} body: ${body?.take(100)}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching profile from Supabase: ${e.message}", e)
        }
        return@withContext null
    }

    suspend fun upsertProfile(
        userData: UserData,
        xp: Int,
        proteinScore: Int,
        veggieLevel: Int,
        junkSaved: Int
    ): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext false
        try {
            val projectUrl = getProjectUrl()
            if (projectUrl.isBlank()) return@withContext false
            val url = "$projectUrl/rest/v1/user_profile"
            val json = JSONObject().apply {
                put("id", 1) // Single primary profile record
                put("allowance", userData.allowance)
                put("weakness", userData.weakness)
                put("goal", userData.goal)
                put("health_goal", userData.healthGoal)
                put("mode", userData.mode)
                put("xp", xp)
                put("protein_score", proteinScore)
                put("veggie_level", veggieLevel)
                put("junk_saved", junkSaved)
                put("is_behavioral_kyc_completed", userData.isBehavioralKycCompleted)
                put("resilience_score", userData.resilienceScore)
                put("behavioral_archetype", userData.behavioralArchetype)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = json.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", getApiKey())
                .addHeader("Authorization", "Bearer ${getApiKey()}")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "resolution=merge-duplicates")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            if (response.isSuccessful) {
                return@withContext true
            } else {
                Log.w(TAG, "upsertProfile failed: HTTP ${response.code} body: ${responseBody?.take(150)}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error upserting profile to Supabase: ${e.message}", e)
        }
        return@withContext false
    }
}
