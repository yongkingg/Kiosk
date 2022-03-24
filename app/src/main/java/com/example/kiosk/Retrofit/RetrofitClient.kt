package com.example.kiosk.Retrofit

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body

import retrofit2.Call
import retrofit2.http.POST

object RetrofitClient {
    fun initRetrofit() : Retrofit {
        var url = "http://52.79.157.214:3000"
        var gson = Gson()
        val clientBuilder = OkHttpClient.Builder().build()

        var connection = Retrofit.Builder()
            .baseUrl(url).client(clientBuilder)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return connection
    }
}

// Login
data class LoginData(val message : String, val success : Boolean)

interface LoginAPI {
    @GET("/account/login")
    fun getLogin(@Query("id") id : String, @Query("pw") pw : String) : Call<LoginData>
}
//

// Account Check
data class AccountCheckData(val message: String, val success: Boolean)
interface AccountCheckAPI {
    @GET("/account/overlap")
    fun accountCheck(@Query("id") id : String) : Call<AccountCheckData>
}
//

// SignUp
data class SignUpData(val message: String, val success: Boolean)

// Gson ( HashMap 방법에서는 사용 X )
data class AccountValue(
    @SerializedName("id") var id : String? = null,
    @SerializedName("pw") var pw : String? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("contact") var contact : String? = null
)

interface SignUpAPI {
    @GET("/account")
    fun getAccount(@Query("id") id : String) : Call<SignUpData>

    @POST("/account")
    // Gson
    fun signUp(@Body requestData : AccountValue) : Call<SignUpData>
}
//

// getCategory
data class GetCategory(val message : String, val success : Boolean, val data: List<CategoryName>)
data class CategoryName(val category_name: String)
interface GetCategoryAPI {
    @GET("/category")
    fun sendLang(@Query("lang") lang : String) : Call<GetCategory>
}
//

// getMenu
data class GetMenu(val message: String, val success: Boolean, val data: List<MenuValue>)
data class MenuValue(
    val menu_name : String,
    val menu_price : Int,
    val menu_image : String
)
interface GetMenuAPI {
    @GET("/category/menu")
    fun sendCategoryValue(@Query("category_name") category_name : String, @Query("lang") lang : String) : Call<GetMenu>
}
//

// putHistory
data class PutHistory(val message: String, val success: Boolean)
data class HistoryValue(
    @SerializedName("id") var id : String? = null,
    @SerializedName("order_list") var order_list : List<BeverageValue>,
    @SerializedName("total_price") var total_price : Int? = null
)
data class BeverageValue(
    @SerializedName("name") var name : String? = null,
    @SerializedName("count") var count : Int? = null,
    @SerializedName("sum_price") var sum_price : Int? = null
)
interface PutHistoryAPI {
    @POST("/order")
    fun sendHistoryValue(@Body() requestData : HistoryValue) : Call<PutHistory>
}
//

// getHistory
data class GetHistory(val message: String, val success: Boolean, val data : List<Any>)

interface GetHistoryAPI {
    @GET("/order")
    fun sendHistoryValue(@Query("id") id : String) : Call<GetHistory>
}
//