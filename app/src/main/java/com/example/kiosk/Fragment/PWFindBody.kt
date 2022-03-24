package com.example.kiosk.Fragment

import android.app.AlertDialog
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
import com.example.kiosk.Activity.FindAccountPage
import com.example.kiosk.DataBase.DataBase
import com.example.kiosk.DataBase.DataBaseControl
import com.example.kiosk.R
import java.util.regex.Pattern

class PWFindBody : Fragment() {
    lateinit var number : String
    lateinit var middle_number_value : String
    lateinit var end_number_value : String
    lateinit var spinner: Spinner
    var numberBoolean : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater.inflate(R.layout.findpwpagebody,container,false)

        val db = DataBase(context,"EDIYA.db",null,1)
        val readableDB = db.readableDatabase
        val writeableDB = db.writableDatabase
        var dbControl = DataBaseControl()

        fetchNumberSpinner(view)
        initEvent(view,writeableDB,dbControl)
        return view
    }

    fun fetchNumberSpinner(view:View) {
        spinner  = view.findViewById(R.id.pwfindspinner)
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

    fun initEvent(view:View,writeableDB : SQLiteDatabase, dbControl : DataBaseControl){
        var idEdit : EditText = view.findViewById(R.id.idEdit)

        var middle_number : EditText = view.findViewById(R.id.pwfind_middlenumber_edit)
        middle_number.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))
        var end_number : EditText = view.findViewById(R.id.pwfind_endnumber_edit)
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
                    numberBoolean = true
                } catch (e : NumberFormatException) {
                    Toast.makeText(context,R.string.inputOnlyNumber,Toast.LENGTH_SHORT).show()
                    middle_number.setTextColor(Color.RED)
                    numberBoolean = false
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
                    numberBoolean = true
                } catch (e : NumberFormatException) {
                    Toast.makeText(context,R.string.inputOnlyNumber,Toast.LENGTH_SHORT).show()
                    end_number.setTextColor(Color.RED)
                    numberBoolean = false
                }
                end_number_value = ""
                end_number_value = end_number.text.toString()
            }
        })

        var findPw : Button? = view.findViewById(R.id.findPWBtn)
        findPw!!.setOnClickListener {
            if (idEdit.text.count() == 0 || middle_number.text.count() == 0 || end_number.text.count() == 0 || numberBoolean == false) {
                Toast.makeText(context,R.string.putAllInfo,Toast.LENGTH_SHORT).show()
            } else {
                number += middle_number_value + end_number_value
                var random = (10000..99999).random()
                var keyService = context as FindAccountPage
                keyService.pwFind(random)
                var popupView = getLayoutInflater().inflate(R.layout.keywindow, null)
                var alertdialog = AlertDialog.Builder(context).create()
                var popupBtn = popupView.findViewById<TextView>(R.id.keyButton)
                var popupEditText : EditText? = popupView.findViewById(R.id.keyEdit)
                var text : TextView = popupView.findViewById(R.id.textView5)
                text.setText(R.string.putKey)
                var showId : TextView = popupView.findViewById(R.id.showID)
                var filterAlphaNumber = InputFilter { source, start, end, dest, dstart, dend ->
                    val ps: Pattern = Pattern.compile("^[0-9]+$")
                    if (!ps.matcher(source).matches()) {
                        ""
                    } else null
                }
                var closeBtn : Button = popupView.findViewById(R.id.backBtn)
                closeBtn.setOnClickListener {
                    alertdialog.hide()
                    var head = context as FindAccountPage
                    head.hideNoti()
                }
                popupEditText!!.setFilters(arrayOf(filterAlphaNumber))
                popupBtn!!.setOnClickListener {
                    if (popupEditText!!.text.toString() != random.toString()) {
                        Toast.makeText(context,R.string.wrongKey,Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context,R.string.rightKey,Toast.LENGTH_SHORT).show()
                        var dataList = dbControl.read(writeableDB,"Info", arrayListOf("id","number"), arrayListOf("${idEdit.text}",number))
                        Log.d("tag","$dataList")
                        Log.d("tag","$idEdit.text")
                        if (dataList.count() == 0) {
                            showId.setText("No Value")
                            number = spinner.selectedItem.toString()
                        } else {
                            var getPw = dbControl.read(writeableDB,"Account", arrayListOf("id"), arrayListOf("${idEdit.text}"))
                            showId.setText(getPw[0][1])
                        }
                    }
                }
                alertdialog.setView(popupView)
                alertdialog.show()
                alertdialog.window!!.setLayout(700, 470)
            }
        }
    }
}