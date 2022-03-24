package com.example.kiosk.Fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kiosk.R
import com.example.kiosk.Retrofit.*
import com.example.kiosk.changeFragment
import retrofit2.Call
import retrofit2.Response

class PayPageBody: Fragment() {
    var allMenuCost : Int? = 0
    var totalCost : Int = 0

    lateinit var signal : changeFragment
    lateinit var userId : String
    lateinit var basketMenu: ArrayList<ArrayList<String>>
    lateinit var beverageValue : BeverageValue
    lateinit var tempList : ArrayList<BeverageValue>
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signal = context as changeFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater.inflate(R.layout.paypagebody,container,false)
        var getBasketCount = arguments?.getString("count")
        userId = arguments?.getString("userId")!!
        basketMenu = ArrayList()
        tempList = arrayListOf()

        for (index in 0 until getBasketCount!!.toInt()) {
            var getMenu = arguments?.getStringArrayList("menu" + "$index")
            basketMenu.add(getMenu!!)
            beverageValue = BeverageValue(basketMenu[index][0],basketMenu[index][3].toInt(),basketMenu[index][2].toInt())
            totalCost += (basketMenu[index][2].toInt() * basketMenu[index][3].toInt()) + 500 * (basketMenu[index][7].toInt() + basketMenu[index][8].toInt() + basketMenu[index][9].toInt() + basketMenu[index][10].toInt())
            tempList.add(beverageValue)
        }
        showBasket(view,getBasketCount,basketMenu)
        initEvent(view,basketMenu[0][6])
        return view
    }

    fun showBasket(view:View,getBasketCount : String,basketMenu : ArrayList<ArrayList<String>>){
        var parentLayout = view.findViewById<LinearLayout>(R.id.payPageParentLayout)
        var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        var basicOptionLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,4f)
        var optionParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,2f)
        var textParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1f)
        var blankParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        var lineParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4)
        lineParams.setMargins(10,30,10,0)

        var headTextParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT)
        headTextParams.setMargins(10,20,10,0)

        var headText = TextView(context)
        headText.setText("주문메뉴/혜택")
        headText.setTypeface(resources.getFont(R.font.head))
        headText.setTextColor(Color.BLACK)
        headText.layoutParams = headTextParams
        headText.textSize = 20.0f
        parentLayout.addView(headText)

        for (index in 0 until getBasketCount.toInt()) {
            var topping : Int = 0
            var mainLayout = LinearLayout(context)
            mainLayout.orientation = LinearLayout.VERTICAL
            layoutParams.setMargins(10,0,10,10)
            mainLayout.layoutParams = layoutParams
            parentLayout.addView(mainLayout)

            var menuName = TextView(context)
            menuName.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            menuName.setText(basketMenu[index][0] + " " + basketMenu[index][3] + "잔")
            menuName.textSize = 20.0f
            menuName.setTextColor(Color.BLACK)
            menuName.setTypeface(resources.getFont(R.font.body))
            mainLayout.addView(menuName)

            var basicOptionLayout = LinearLayout(context)
            basicOptionLayout.layoutParams = basicOptionLayoutParams
            basicOptionLayout.orientation = LinearLayout.HORIZONTAL
            mainLayout.addView(basicOptionLayout)

            var basicOption = TextView(context)
            basicOption.layoutParams = optionParams
            basicOption.setText(basketMenu[index][4] + " | " + basketMenu[index][5]  + " | " + basketMenu[index][6])
            basicOption.textSize = 15f
            basicOption.setTypeface(resources.getFont(R.font.body))
            basicOptionLayout.addView(basicOption)

            var blank = View(context)
            blank.layoutParams = blankParams
            basicOptionLayout.addView(blank)

            var costText = TextView(context)
            costText.layoutParams = textParams
            costText.setTypeface(resources.getFont(R.font.body))
            costText.setText(basketMenu[index][2])
            costText.gravity = Gravity.END
            costText.textSize = 15f
            basicOptionLayout.addView(costText)

            var toppingLayout = LinearLayout(context)
            toppingLayout.layoutParams = basicOptionLayoutParams
            toppingLayout.orientation = LinearLayout.HORIZONTAL
            mainLayout.addView(toppingLayout)

            var toppingList = TextView(context)
            toppingList.layoutParams = optionParams
            if (basketMenu[index][7] == "0" && basketMenu[index][8] == "0" && basketMenu[index][9] =="0" && basketMenu[index][10] == "0") {
                toppingList.setText("커스텀 선택 안함")
            } else {
                var sql : String = "토핑 내역\n"
                if (basketMenu[index][7].toInt() != 0) {
                    sql += "초콜릿칩 X " + basketMenu[index][7]+" = ${basketMenu[index][7].toInt()*500} \n"
                }
                if (basketMenu[index][8].toInt() != 0) {
                    sql += "휘핑크림 X " + basketMenu[index][8]+" = ${basketMenu[index][8].toInt()*500} \n"
                }
                if (basketMenu[index][9].toInt() != 0) {
                    sql += "타피오카 펄 X " + basketMenu[index][9]+" = ${basketMenu[index][9].toInt()*500} \n"
                }
                if (basketMenu[index][10].toInt() != 0) {
                    sql += "시럽 X " + basketMenu[index][10]+" = ${basketMenu[index][10].toInt()*500} \n"
                }
                toppingList.setText(sql)
            }

            toppingList.textSize = 15f
            toppingList.setTypeface(resources.getFont(R.font.body))
            toppingLayout.addView(toppingList)

            var toppingCost = TextView(context)
            toppingCost.layoutParams = textParams
            toppingCost.setTypeface(resources.getFont(R.font.body))
            if (basketMenu[index][7] == "0" && basketMenu[index][8] == "0" && basketMenu[index][9] =="0" && basketMenu[index][10] == "0") {
                toppingCost.setText("0")
            } else {
                if (basketMenu[index][7].toInt() != 0) {
                    topping += basketMenu[index][7].toInt() * 500 * basketMenu[index][3].toInt()
                }
                if (basketMenu[index][8].toInt() != 0) {
                    topping += basketMenu[index][8].toInt() * 500 * basketMenu[index][3].toInt()
                }
                if (basketMenu[index][9].toInt() != 0) {
                    topping += basketMenu[index][9].toInt() * 500 * basketMenu[index][3].toInt()
                }
                if (basketMenu[index][10].toInt() != 0) {
                    topping += basketMenu[index][10].toInt() * 500 * basketMenu[index][3].toInt()
                }
                toppingCost.setText("${topping}")
            }
            toppingCost.gravity = Gravity.END
            toppingCost.textSize = 15f
            toppingLayout.addView(toppingCost)

            var totalCost = TextView(context)
            totalCost.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            totalCost.gravity = Gravity.END
            totalCost.setText((basketMenu[index][2].toInt() + topping).toString())
            totalCost.textSize = 20f
            totalCost.setTextColor(Color.BLACK)
            totalCost.setTypeface(resources.getFont(R.font.body))
            mainLayout.addView(totalCost)


            var line = View(context)
            line.layoutParams = lineParams
            line.setBackgroundColor(Color.BLACK)
            mainLayout.addView(line)
            allMenuCost = allMenuCost!! +basketMenu[index][2].toInt() + topping
        }
        var allCost = TextView(context)
        var allCostParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        allCostParams.setMargins(0,0,10,0)
        allCost.layoutParams = allCostParams
        allCost.gravity = Gravity.END
        allCost.setText("총 금액 : " + allMenuCost.toString())
        allCost.textSize = 25f
        allCost.setTextColor(Color.BLACK)
        allCost.setTypeface(resources.getFont(R.font.head))
        parentLayout.addView(allCost)

        var totalCostTextView = view.findViewById<TextView>(R.id.totalCostTextView)
        totalCostTextView.setText(allMenuCost.toString() + "원")
    }
    fun initEvent(view: View,takeType : String){
        var payment : String? = null
        var isReceipt : Boolean = false
        var isPay : Boolean = false

        var paymentView = view.findViewById<TextView>(R.id.payment)

        var ediyaPay : Button? = view.findViewById<Button>(R.id.ediyaPay)
        var kakaoPay : Button? = view.findViewById<Button>(R.id.kakaoPay)
        var creditCard : Button? = view.findViewById<Button>(R.id.creditCard)

        ediyaPay!!.setOnClickListener{
            payment = "이디야페이"
            paymentView.setText(payment)
            ediyaPay.setBackgroundResource(R.drawable.accountbtn)
            kakaoPay!!.setBackgroundResource(R.drawable.mainbtn)
            creditCard!!.setBackgroundResource(R.drawable.mainbtn)
        }
        kakaoPay!!.setOnClickListener{
            payment = "카카오페이"
            paymentView.setText(payment)
            ediyaPay.setBackgroundResource(R.drawable.mainbtn)
            kakaoPay!!.setBackgroundResource(R.drawable.accountbtn)
            creditCard!!.setBackgroundResource(R.drawable.mainbtn)
        }
        creditCard!!.setOnClickListener{
            payment = "신용카드"
            paymentView.setText(payment)
            ediyaPay.setBackgroundResource(R.drawable.mainbtn)
            kakaoPay!!.setBackgroundResource(R.drawable.mainbtn)
            creditCard!!.setBackgroundResource(R.drawable.accountbtn)
        }


        var receiptBox : CheckBox = view.findViewById<CheckBox>(R.id.receiptBox)
        receiptBox.setOnClickListener{
            isReceipt = isReceipt == false
        }

        var payBox : CheckBox = view.findViewById<CheckBox>(R.id.payBox)
        payBox.setOnClickListener{
            isPay = isPay == false
        }

        var payBtn: Button? = view.findViewById<Button>(R.id.payBtn)
        payBtn!!.setOnClickListener{
            if (payment == null || isPay == false) {
                var sql = ""
                var popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
                var alertdialog = AlertDialog.Builder(context).create()
                var popupBackBtn = popupView.findViewById<Button>(R.id.backBtn)
                var popupText = popupView.findViewById<TextView>(R.id.popuptext)
                if (payment == null && isPay == false) {
                    sql = "결제수단을 선택하고, 주문/결제에 동의해주세요"
                } else if (isPay == false) {
                    sql = "주문/결제에 동의해주세요"
                } else if (payment == null) {
                    sql = "결제수단을 선택해주세요"
                }
                popupText.setText(sql)
                popupBackBtn!!.setOnClickListener {
                    alertdialog.hide()
                }
                alertdialog.setView(popupView)
                alertdialog.show()
                alertdialog.window!!.setLayout(600, 400)

            } else {
                signal.signal(allMenuCost!!,payment!!,isReceipt, takeType)

                val retrofit = RetrofitClient.initRetrofit()
                var data = HistoryValue(userId,tempList,totalCost)
                val requestPutHistoryAPI = retrofit.create(PutHistoryAPI::class.java)
                requestPutHistoryAPI.sendHistoryValue(data).enqueue(object : retrofit2.Callback<PutHistory> {
                    override fun onFailure(call: Call<PutHistory>, t: Throwable) {
                    }
                    override fun onResponse(call: Call<PutHistory>, response: Response<PutHistory>) {
                    }
                })
            }
        }
        var cancelOrder : Button? = view.findViewById<Button>(R.id.cancelOrder)
        cancelOrder!!.setOnClickListener{
            requireActivity().finish()
        }
    }
}

