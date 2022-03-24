package com.example.kiosk.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.kiosk.Activity.SignUpPage
import com.example.kiosk.DataBase.DataBase
import com.example.kiosk.DataBase.DataBaseControl
import com.example.kiosk.R
import com.example.kiosk.Retrofit.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpPageBody : Fragment() {
    lateinit var db : DataBase
    val retrofit = RetrofitClient.initRetrofit()
    var isIDChanged : Boolean = false
    var exp = Regex("^[ㄱ-ㅎㅏ-ㅣ가-힣]+$")
    var canSignUp: ArrayList<Boolean> = arrayListOf(false, false, false, false, false) // id, pw, email,name, num,

    lateinit var idValue : String
    lateinit var pwValue : String
    lateinit var name : String
    lateinit var email : String
    lateinit var domain : String
    lateinit var number : String
    lateinit var middle_number_value : String
    lateinit var end_number_value : String

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var view : View = inflater.inflate(R.layout.signuppagebody,container,false)

        val db = DataBase(context,"EDIYA.db",null,1)
        val readableDB = db.readableDatabase
        val writeableDB = db.writableDatabase
        var dbControl = DataBaseControl()

        fetchEmailSpinner(view)
        fetchNumberSpinner(view)
        initEvent(view, writeableDB,dbControl)
        return view
    }

    fun fetchEmailSpinner(view:View) {
        val spinner : Spinner = view.findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(requireContext(),R.array.email,android.R.layout.simple_spinner_item). also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                domain = spinner.selectedItem.toString()
            }
        }
    }

    fun fetchNumberSpinner(view:View) {
        val spinner : Spinner = view.findViewById(R.id.numberSpinner)
        ArrayAdapter.createFromResource(requireContext(),R.array.number,android.R.layout.simple_spinner_item). also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                number = spinner.selectedItem.toString()
            }
        }
    }

    fun initEvent(view:View, writeableDB : SQLiteDatabase, dbControl: DataBaseControl) {
        idEvent(view)
        pwEvent(view,writeableDB,dbControl)
        nameEvent(view,writeableDB,dbControl)
        emailEvent(view,writeableDB,dbControl)
        numEvent(view,writeableDB, dbControl)
        signUpBtnEvent(view,writeableDB, dbControl)
    }

    fun idEvent(view:View) {
        var inputId: EditText = view.findViewById<EditText>(R.id.inputID)
        inputId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isIDChanged = true
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        var checkID: Button = view.findViewById<Button>(R.id.checkIDBtn)
        checkID.setOnClickListener {
            var popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
            var alertdialog = AlertDialog.Builder(context).create()
            var popupBackBtn = popupView.findViewById<Button>(R.id.backBtn)
            var popupText = popupView.findViewById<TextView>(R.id.popuptext)

            if (inputId.text.count() == 0) {
                popupText.setText(R.string.inputID)
                popupBackBtn!!.setOnClickListener {
                alertdialog.hide()
                canSignUp[0] = false
                }
            } else {
                val checkID = retrofit.create(AccountCheckAPI::class.java)
                checkID.accountCheck(inputId.text.toString()).enqueue(object : Callback<AccountCheckData> {
                    override fun onFailure(call: Call<AccountCheckData>, t: Throwable) {
                    }
                    override fun onResponse(call: Call<AccountCheckData>, response: Response<AccountCheckData>) {
                        if (response.body()!!.success == true) {
                            idValue = inputId.text.toString()
                            popupText.setText(R.string.usableId)
                            popupBackBtn!!.setOnClickListener {
                                alertdialog.hide()
                                canSignUp[0] = true
                                isIDChanged = false
                            }
                        } else if (response.body()!!.success == false) {
                            popupText.setText(R.string.unUsableId)
                            popupBackBtn!!.setOnClickListener {
                                alertdialog.hide()
                                canSignUp[0] = false
                            }
                        }
                    }
                })
            }
            alertdialog.setView(popupView)
            alertdialog.show()
            alertdialog.window!!.setLayout(600, 400)
        }
    }

    fun pwEvent(view:View, writeableDB : SQLiteDatabase, dbControl: DataBaseControl) {
        var isPassword : TextView = view.findViewById(R.id.passwordAlert)
        isPassword.setText(R.string.pwGuide)
        var checkPassword : TextView = view.findViewById(R.id.passwordCheck)
        checkPassword.setText(R.string.pwCheckMessage)

        var checkPw : EditText = view.findViewById<EditText>(R.id.checkPW)
        var inputPw: EditText = view.findViewById<EditText>(R.id.inputPW)
        inputPw.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                pwValue = ""
                if (inputPw.equals(exp)) {
                    isPassword.setText(R.string.unUsablePw)
                    inputPw.setTextColor(Color.RED)
                } else if (inputPw.text.count() < 5) {
                    isPassword.setText(R.string.pwCount)
                    inputPw.setTextColor(Color.RED)
                    isPassword.setTextColor(Color.RED)
                } else {
                    isPassword.setText(R.string.usablePw)
                    inputPw.setTextColor(Color.BLACK)
                    isPassword.setTextColor(Color.BLACK)
                    pwValue = inputPw.text.toString()
                    if (checkPw.text.count() == 0) {
                        checkPassword.setText(R.string.pwCheckMessage)
                        checkPassword.setTextColor(Color.RED)
                    } else if (checkPw.text.toString() != pwValue) {
                        checkPassword.setText(R.string.unCorrectPw)
                        checkPassword.setTextColor(Color.RED)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //    입력이 끝날때
                if (inputPw.text.count() == 0) {
                    isPassword.setText(R.string.inputPw)
                    isPassword.setTextColor(Color.RED)
                }
            }
        })

        checkPw.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (checkPw.text.toString() != pwValue) {
                    checkPassword.setText(R.string.unCorrectPw)
                    checkPassword.setTextColor(Color.RED)
                    canSignUp[1] = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (checkPw.text.toString() == pwValue) {
                    checkPassword.setText(R.string.correctPw)
                    checkPassword.setTextColor(Color.BLACK)
                    canSignUp[1] = true
                }
            }
        })
    }

    fun emailEvent(view:View, writeableDB : SQLiteDatabase, dbControl: DataBaseControl) {
        var inputEmail: EditText = view.findViewById<EditText>(R.id.putEmail)
        inputEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = ""
                email = inputEmail.text.toString()
                canSignUp[2] = true
            }
        })
    }

    fun nameEvent(view:View,writeableDB: SQLiteDatabase,dbControl: DataBaseControl) {
        var putName : EditText = view.findViewById(R.id.putName)
        var filterAlphaNumber = InputFilter { source, start, end, dest, dstart, dend ->
            val ps: Pattern = Pattern.compile("^[a-zA-Z]+$")
            if (!ps.matcher(source).matches()) {
                ""
            } else null
        }
        putName.setFilters(arrayOf(filterAlphaNumber))
        putName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                name = ""
                name = putName.text.toString()
                canSignUp[3] = true
            }
        })
    }

    fun numEvent(view:View,writeableDB: SQLiteDatabase,dbControl: DataBaseControl) {
        var middle_number : EditText = view.findViewById(R.id.putNumber1)
        middle_number.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))
        var end_number : EditText = view.findViewById(R.id.putNumber2)
        end_number.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))
        middle_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    var str = middle_number.text.toString().trim()
                    var rt : Int = Integer.parseInt(str)
                    middle_number.setTextColor(Color.BLACK)
                    canSignUp[4] = true
                } catch (e : NumberFormatException) {
                    Toast.makeText(context,R.string.inputOnlyNumber,Toast.LENGTH_SHORT).show()
                    middle_number.setTextColor(Color.RED)
                    canSignUp[4] = false
                }
                middle_number_value = ""
                middle_number_value = middle_number.text.toString()
                if (middle_number.text.count() == 4) {
                    end_number.requestFocus()
                }

            }
        })

        end_number.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    var str = end_number.text.toString().trim()
                    var rt : Int = Integer.parseInt(str)
                    end_number.setTextColor(Color.BLACK)
                    canSignUp[4] = true
                } catch (e : NumberFormatException) {
                    Toast.makeText(context,R.string.inputOnlyNumber,Toast.LENGTH_SHORT).show()
                    end_number.setTextColor(Color.RED)
                    canSignUp[4] = false
                }
                end_number_value = ""
                end_number_value = end_number.text.toString()
            }
        })
    }

    fun signUpBtnEvent(view:View,writeableDB: SQLiteDatabase,dbControl: DataBaseControl) {
        var signupBtn : Button? = view.findViewById<Button>(R.id.signUp)
        signupBtn!!.setOnClickListener {
            if (canSignUp.contains(false)) {
                var input = arrayOf(getString(R.string.id), getString(R.string.pw),getString(R.string.email),getString(R.string.name), getString(R.string.number))
                Toast.makeText(context,"${input[canSignUp.indexOf(false)]}" + getString(R.string.checkMessage) + ".",Toast.LENGTH_SHORT).show()
            } else {
                number += middle_number_value + end_number_value
                Toast.makeText(context,getString(R.string.successSignup),Toast.LENGTH_SHORT).show()
                // GSon
                var data = AccountValue(idValue,pwValue,name,number)
                val requestSignUpApi = retrofit.create(SignUpAPI::class.java)
                requestSignUpApi.signUp(data).enqueue(object : Callback<SignUpData> {
                    override fun onFailure(call: Call<SignUpData>, t: Throwable) {
                    }
                    override fun onResponse(call: Call<SignUpData>, response: Response<SignUpData>) {
                        Log.d("result",response.body()!!.message)
                        Log.d("result",response.body()!!.success.toString())
                    }
                })
                //

                var signUpPage = context as SignUpPage
                signUpPage.exitActivity()

            }
        }
    }
}