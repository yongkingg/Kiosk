package com.example.kiosk.Fragment

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kiosk.Activity.UserPage
import com.example.kiosk.DataBase.DataBase
import com.example.kiosk.DataBase.DataBaseControl
import com.example.kiosk.R
import com.example.kiosk.Retrofit.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistoryPageBody : Fragment() {
    lateinit var userId: String
    var orderHistory = arrayListOf<ArrayList<String>>()
    var beverageCount : Int = 0
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.orderhistorybody, container, false)

        userId = arguments?.getString("userId")!!
        val db = DataBase(context, "EDIYA.db", null, 1)
        val readableDB = db.readableDatabase
        val writeableDB = db.writableDatabase
        var dbControl = DataBaseControl()

        showHistory(view, userId, writeableDB, dbControl)
        return view
    }

    fun showHistory(view: View, userId: String, writableDB: SQLiteDatabase, dbControl: DataBaseControl) {
        var getHistory = dbControl.read(writableDB, "History", arrayListOf("id"), arrayListOf("$userId"))
        var parentLayout = view.findViewById<LinearLayout>(R.id.basketPageLayout)
        var layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        var basicOptionLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            4f
        )
        var optionParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
        var textParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        var blankParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        var lineParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4)
        lineParams.setMargins(10, 30, 10, 0)

        var headTextParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        headTextParams.setMargins(10, 20, 10, 0)

        val retrofit = RetrofitClient.initRetrofit()
        val getHistoryApi = retrofit.create(GetHistoryAPI::class.java)
        getHistoryApi.sendHistoryValue(userId).enqueue(object : Callback<GetHistory> {
            override fun onFailure(call: Call<GetHistory>, t: Throwable) {
            }
            override fun onResponse(call: Call<GetHistory>, response: Response<GetHistory>) {
                // set beverage count
                for (index in 0 until response.body()!!.data.size) {
                    var jsonObject = JSONObject(response.body()!!.data[index].toString())
                    var beverageValue = jsonObject.getJSONArray("order_list")
                    for (index in 0 until beverageValue.length()) {
                        var beverageObject = beverageValue.getJSONObject(index)
                        var beverageName = beverageObject.getString("name")
                        var beverageCount = beverageObject.getInt("count")
                        var beveragePrice = beverageObject.getInt("sum_price")
                        orderHistory.add(arrayListOf(beverageName,beverageCount.toString(),beveragePrice.toString()))
                    }
                    beverageCount += beverageValue.length()
                }
                Log.d("tag","$orderHistory")

                for (index in 0 until orderHistory.count()) {
                    var calToppingCost : Int = 0
                    var topping = getHistory[index][4].toList()

                    var mainLayout = LinearLayout(context)
                    mainLayout.orientation = LinearLayout.VERTICAL
                    layoutParams.setMargins(10, 0, 10, 10)
                    mainLayout.layoutParams = layoutParams
                    parentLayout.addView(mainLayout)

                    var menuLayout = LinearLayout(context)
                    menuLayout.layoutParams = basicOptionLayoutParams
                    menuLayout.orientation = LinearLayout.HORIZONTAL
                    mainLayout.addView(menuLayout)

                    var menuName = TextView(context)
                    menuName.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    menuName.setText(orderHistory[index][0] + " " + orderHistory[index][1] + "잔")
                    menuName.textSize = 20.0f
                    menuName.setTextColor(Color.BLACK)
                    menuName.setTypeface(resources.getFont(R.font.body))
                    menuLayout.addView(menuName)

                    var orderDate = TextView(context)
                    orderDate.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    orderDate.setText(getHistory[index][6])
                    orderDate.textSize = 15f
                    orderDate.gravity = Gravity.END
                    orderDate.setTypeface(resources.getFont(R.font.body))
                    menuLayout.addView(orderDate)

                    var basicOptionLayout = LinearLayout(context)
                    basicOptionLayout.layoutParams = basicOptionLayoutParams
                    basicOptionLayout.orientation = LinearLayout.HORIZONTAL
                    mainLayout.addView(basicOptionLayout)

                    var basicOption = TextView(context)
                    basicOption.layoutParams = optionParams
                    basicOption.setText(getHistory[index][3] + " | " + getHistory[index][5])
                    basicOption.textSize = 15f
                    basicOption.setTypeface(resources.getFont(R.font.body))
                    basicOptionLayout.addView(basicOption)

                    var blank = View(context)
                    blank.layoutParams = blankParams
                    basicOptionLayout.addView(blank)

                    Log.d("tag","$getHistory")
                    var costText = TextView(context)
                    costText.layoutParams = textParams
                    costText.setTypeface(resources.getFont(R.font.body))
//                    costText.setText((orderHistory[index][2].toInt()*(getHistory[index][7].toInt() + 500 * (topping[0].toString().toInt() + topping[1].toString().toInt() + topping[2].toString().toInt() + topping[3].toString().toInt()))).toString())
                    if (getHistory[index][5] == "REGULAR") {
                        costText.setText(orderHistory[index][2])
                    } else if (getHistory[index][5] == "LARGE") {
                        costText.setText((orderHistory[index][2].toInt() + 500).toString())
                    } else if (getHistory[index][5] == "MAX") {
                        costText.setText((orderHistory[index][2].toInt() + 1000).toString())
                    }
                    costText.gravity = Gravity.END
                    costText.textSize = 15f
                    basicOptionLayout.addView(costText)

                    var toppingLayout = LinearLayout(context)
                    toppingLayout.layoutParams = basicOptionLayoutParams
                    toppingLayout.orientation = LinearLayout.HORIZONTAL
                    mainLayout.addView(toppingLayout)

                    var toppingList = TextView(context)
                    toppingList.layoutParams = optionParams
                    if (topping[0].toString() == "0" && topping[0].toString() == "1" && topping[0].toString() == "2" && topping[0].toString() == "3"){
                        toppingList.setText("커스텀 선택 안함")
                    } else {
                        var sql: String = "토핑 내역\n"
                        if (topping[0].toString() != "0") {
                            sql += getString(R.string.topping_chocolate)+ " X " + topping[0] + " = ${topping[0].toString().toInt() * 500} \n"
                        }
                        if (topping[1].toString() != "0") {
                            sql += getString(R.string.topping_cream)+ " X " + topping[1] + " = ${topping[1].toString().toInt() * 500} \n"
                        }
                        if (topping[2].toString() != "0") {
                            sql += getString(R.string.topping_pull)+ " X " + topping[2] + " = ${topping[2].toString().toInt() * 500} \n"
                        }
                        if (topping[3].toString() != "0") {
                            sql += getString(R.string.topping_shirup)+ " X " + topping[3] + " = ${topping[3].toString().toInt() * 500} \n"
                        }
                        toppingList.setText(sql)
                    }
                    toppingList.textSize = 15f
                    toppingList.setTypeface(resources.getFont(R.font.body))
                    toppingLayout.addView(toppingList)

                    var toppingCost = TextView(context)
                    toppingCost.layoutParams = textParams
                    toppingCost.setTypeface(resources.getFont(R.font.body))
                    if (topping[0].toString() == "0" && topping[0].toString() == "1" && topping[0].toString() == "2" && topping[0].toString() == "3"){
                        toppingCost.setText("0")
                    } else {
                        if (topping[0].toString() != "0") {
                            calToppingCost += topping[0].toString().toInt() * 500 * getHistory[index][2].toInt()
                        }
                        if (topping[1].toString() != "0") {
                            calToppingCost += topping[1].toString().toInt() * 500 * getHistory[index][2].toInt()
                        }
                        if (topping[2].toString() != "0") {
                            calToppingCost += topping[2].toString().toInt() * 500 * getHistory[index][2].toInt()
                        }
                        if (topping[3].toString() != "0") {
                            calToppingCost += topping[3].toString().toInt() * 500 * getHistory[index][2].toInt()
                        }
                        toppingCost.setText("${calToppingCost}")
                    }
                    toppingCost.gravity = Gravity.END
                    toppingCost.textSize = 15f
                    toppingLayout.addView(toppingCost)

                    var totalCost = TextView(context)
                    totalCost.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                    totalCost.gravity = Gravity.END


                    if (getHistory[index][5] == "REGULAR") {
                        totalCost.setText((orderHistory[index][2].toInt() + calToppingCost).toString())
                    }  else if (getHistory[index][5] == "LARGE") {
                        totalCost.setText((orderHistory[index][2].toInt() + calToppingCost + 500).toString())
                    } else if (getHistory[index][5] == "MAX") {
                        totalCost.setText((orderHistory[index][2].toInt() + calToppingCost + 1000).toString())
                    }
                    totalCost.textSize = 20f
                    totalCost.setTextColor(Color.BLACK)
                    totalCost.setTypeface(resources.getFont(R.font.body))
                    mainLayout.addView(totalCost)

                    var line = View(context)
                    line.layoutParams = lineParams
                    line.setBackgroundColor(Color.BLACK)
                    mainLayout.addView(line)

                }
            }
        })
    }
}


