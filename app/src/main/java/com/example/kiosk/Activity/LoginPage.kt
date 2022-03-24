package com.example.kiosk.Activity

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.Class.ThemeManager
import com.example.kiosk.DataBase.DataBase
import com.example.kiosk.DataBase.DataBaseControl
import com.example.kiosk.R
import com.example.kiosk.Retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginPage : AppCompatActivity() {
    lateinit var lang: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultMode()
        setContentView(R.layout.loginpagebody)

        val db = DataBase(this,"EDIYA.db",null,1)
        val readableDB = db.readableDatabase
        val writeableDB = db.writableDatabase
        var dbControl = DataBaseControl()

        var languageRadioGroup : RadioGroup = findViewById(R.id.languageRadioGroup)!!
        var brightModeRadioGroup : RadioGroup = findViewById(R.id.brightModeRadioGroup)!!
        var koreanSelectBtn : Button = findViewById<Button>(R.id.koreanSelectBtn)!!
        var engSelectBtn: Button = findViewById<Button>(R.id.EnglishSelectBtn)!!
        var lightMode : RadioButton = findViewById(R.id.whiteModeSelectBtn)!!
        var darkMode : RadioButton = findViewById(R.id.DarkModeSelectBtn)!!

        radioBtnCheck(languageRadioGroup,brightModeRadioGroup)
        initEvent(writeableDB,dbControl,koreanSelectBtn,engSelectBtn,lightMode,darkMode)

    }

    fun defaultMode() {
        if (MyApplication.prefs.myBrightMode == "dark") {
            ThemeManager.applyTheme(ThemeManager.ThemeMode.DARK)
        } else {
            ThemeManager.applyTheme(ThemeManager.ThemeMode.DEFAULT)
        }
        if (MyApplication.prefs.myLanguageMode == "ko") {
            setLanguage("ko")
            lang = "ko"
        } else if (MyApplication.prefs.myLanguageMode == "en") {
            setLanguage("en")
            lang = "en"
        }
    }

    fun radioBtnCheck(languageRadioGroup: RadioGroup, brightModeRadioGroup: RadioGroup) {
        Log.d("tag",MyApplication.prefs.myBrightMode)
        if (MyApplication.prefs.myBrightMode == "dark") {
            brightModeRadioGroup.check(R.id.DarkModeSelectBtn)
        } else {
            brightModeRadioGroup.check(R.id.whiteModeSelectBtn)
        }
        if (MyApplication.prefs.myLanguageMode == "ko") {
            languageRadioGroup.check(R.id.koreanSelectBtn)
            lang = "ko"
        } else if (MyApplication.prefs.myLanguageMode == "en") {
            languageRadioGroup.check(R.id.EnglishSelectBtn)
            lang = "en"
        }
    }

    fun initEvent(writeableDB : SQLiteDatabase, dbControl: DataBaseControl,koreanSelectBtn : Button, engSelectBtn: Button,lightMode:Button,darkMode:Button) {
        languageSelect(koreanSelectBtn,engSelectBtn)
        buttonEvent()
        loginEvent(writeableDB,dbControl)
        appMode(lightMode,darkMode)
    }

    fun languageSelect(koreanSelectBtn : Button, engSelectBtn: Button) {
        koreanSelectBtn!!.setOnClickListener{
            setLanguage("ko")
            MyApplication.prefs.myLanguageMode = "ko"
            lang = "ko"
        }
        engSelectBtn!!.setOnClickListener{
            setLanguage("en")
            MyApplication.prefs.myLanguageMode = "en"
            lang = "en"
        }
    }

    fun buttonEvent() {
        var signUpBtn : Button? = findViewById<Button>(R.id.signUpBtn)
        signUpBtn!!.setOnClickListener{
            var intent = Intent(this,SignUpPage::class.java)
            startActivity(intent)
        }
        var findIdBtn : Button? = findViewById<Button>(R.id.findId)
        findIdBtn!!.setOnClickListener {
            var intent = Intent(this,FindAccountPage::class.java)
            intent.putExtra("key","id")
            startActivity(intent)
        }
        var findPwBtn : Button? = findViewById<Button>(R.id.findPW)
        findPwBtn!!.setOnClickListener {
            var intent = Intent(this,FindAccountPage::class.java)
            intent.putExtra("key","password")
            startActivity(intent)
        }
    }

    fun setLanguage(lang:String) {
        val config = resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        config.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            createConfigurationContext(config)
        }
        resources.updateConfiguration(config,resources.displayMetrics)
    }

    fun loginEvent(writeableDB : SQLiteDatabase, dbControl: DataBaseControl) {
        var pwInputType = false
        var idEditText : EditText? = findViewById<EditText>(R.id.ideditText)
        var pwEditText : EditText? = findViewById<EditText>(R.id.pwEditText)
        var showPwBtn : Button? = findViewById<Button>(R.id.Pwshow)
        showPwBtn!!.setOnClickListener {
            if (pwInputType == false) {
                pwEditText!!.transformationMethod = null
                pwInputType = true
            } else if (pwInputType == true) {
                pwEditText!!.transformationMethod = PasswordTransformationMethod()
                pwInputType = false
            }
        }

        var loginBtn :Button? = findViewById<Button>(R.id.loginBtn)
        loginBtn!!.setOnClickListener{
            val retrofit = RetrofitClient.initRetrofit()
            val requestSignUpApi = retrofit.create(LoginAPI::class.java)
            requestSignUpApi.getLogin(idEditText!!.text.toString(),pwEditText!!.text.toString()).enqueue(object : Callback<LoginData> {
                override fun onFailure(call: Call<LoginData>, t: Throwable) {
                }
                override fun onResponse(call: Call<LoginData>, response: Response<LoginData>) {
                    if (response.body()!!.success == true) {
                        var intent = Intent(this@LoginPage,UserPage::class.java)
                        intent.putExtra("userKey",idEditText.text.toString())
                        intent.putExtra("lang",lang)
                        startActivity(intent)
                        Toast.makeText(this@LoginPage,R.string.loginSuccess,Toast.LENGTH_SHORT).show()
                    } else if (response.body()!!.success == false) {
                        Toast.makeText(this@LoginPage,R.string.loginFail,Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    fun appMode(lightMode: Button,darkMode: Button){
        lightMode!!.setOnClickListener{
            ThemeManager.applyTheme(ThemeManager.ThemeMode.DEFAULT)
            MyApplication.prefs.myBrightMode = "light"
        }
        darkMode!!.setOnClickListener{
            ThemeManager.applyTheme(ThemeManager.ThemeMode.DARK)
            MyApplication.prefs.myBrightMode = "dark"
        }
    }
}
