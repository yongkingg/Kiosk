package com.example.kiosk.Fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kiosk.Activity.BeverageSetPage
import com.example.kiosk.Class.ImageData
import com.example.kiosk.R
import com.example.kiosk.Retrofit.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class BeverageSetPageBody: Fragment() {
    var beverageCount: Int = 1
    var temperature: String? = null
    var size: String? = null
    var receive: String? = null
    var toppingList = arrayListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        var view: View = inflater.inflate(R.layout.beveragesetbody, container, false)
        var getMenuName = arguments?.getString("menu") // 지워도될듯
        var getCategory = arguments?.getString("category") // 지워도될듯
        var getLang = arguments?.getString("lang")

        var retrofit = RetrofitClient.initRetrofit()
        var getMenuData = retrofit.create(GetMenuAPI::class.java)
        getMenuData.sendCategoryValue(getCategory!!,getLang!!).enqueue(object : retrofit2.Callback<GetMenu> {
            override fun onFailure(call: Call<GetMenu>, t: Throwable) {
            }
            override fun onResponse(call: Call<GetMenu>, response: Response<GetMenu>) {
                var menu_name = response.body()!!.data[getMenuName!!.toInt()].menu_name
                var menu_cost = response.body()!!.data[getMenuName!!.toInt()].menu_price
                var menu_image = response.body()!!.data[getMenuName!!.toInt()].menu_image
                initEvent(view, menu_name, menu_cost, menu_image)

            }
        })
        return view
    }

    fun initEvent(view: View,menuName: String, menuCost : Int, menuImage:String) {
        setBeverageValue(view,menuName,menuCost,menuImage)
        buttonEvent(view,menuName,menuCost,menuImage)
    }

    fun setBeverageValue(view: View,menuName: String,menuCost: Int,menuImage: String){
        var nameView : TextView? = view.findViewById<TextView>(R.id.beverageName)
        var beverageCost: TextView? = view.findViewById<TextView>(R.id.beverageCost)
        var beverageImage: ImageView? = view.findViewById<ImageView>(R.id.beverageImage)

        Glide.with(this).load("http://52.79.157.214:3000$menuImage").override(200,200).into(beverageImage!!)
        nameView!!.setText(menuName)
        beverageCost!!.setText(menuCost.toString())
    }

    fun buttonEvent(view:View, menuName: String, menuCost: Int, menuImage: String) {
        var totalCost = 0
        var totalPay : TextView? = view.findViewById<TextView>(R.id.total)
        totalCost += menuCost
        totalPay!!.setText(menuCost.toString() + "원")
        var beverageCountView: TextView? = view.findViewById<TextView>(R.id.beverageCount)
        beverageCountView!!.setText(beverageCount.toString())
        var minusBtn: Button? = view.findViewById<Button>(R.id.minusBtn)
        minusBtn!!.setOnClickListener {
            if (beverageCount == 1) {
                var popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
                var alertdialog = AlertDialog.Builder(context).create()
                var popupBackBtn = popupView.findViewById<Button>(R.id.backBtn)
                popupBackBtn!!.setOnClickListener {
                    alertdialog.hide()
                }
                alertdialog.setView(popupView)
                alertdialog.show()
                alertdialog.window!!.setLayout(600, 400)
            } else {
                beverageCount -= 1
                beverageCountView!!.setText(beverageCount.toString())
            }
            totalCost -= menuCost
            totalPay!!.setText(totalCost.toString() + "원")
        }

        var plusBtn: Button? = view.findViewById<Button>(R.id.plusBtn)
        plusBtn!!.setOnClickListener {
            beverageCount += 1
            beverageCountView!!.setText(beverageCount.toString())
            totalCost += menuCost
            totalPay!!.setText(totalCost.toString() + "원")
        }

        var iceBtn: Button? = view.findViewById<Button>(R.id.icebeverageBtn)
        var hotBtn: Button? = view.findViewById<Button>(R.id.hotbeverageBtn)

        iceBtn!!.setOnClickListener {
            iceBtn.setBackgroundResource(R.drawable.accountbtn)
            hotBtn!!.setBackgroundResource(R.drawable.mainbtn)
            temperature = "ICE"
        }

        hotBtn!!.setOnClickListener {
            iceBtn.setBackgroundResource(R.drawable.mainbtn)
            hotBtn.setBackgroundResource(R.drawable.accountbtn)
            temperature = "HOT"
        }

        var regularBtn: Button? = view.findViewById<Button>(R.id.regularBtn)
        var largeBtn: Button? = view.findViewById<Button>(R.id.largeBtn)
        var maxBtn: Button? = view.findViewById<Button>(R.id.maxBtn)
        regularBtn!!.setOnClickListener {
            regularBtn.setBackgroundResource(R.drawable.accountbtn)
            largeBtn!!.setBackgroundResource(R.drawable.mainbtn)
            maxBtn!!.setBackgroundResource(R.drawable.mainbtn)
            if (size == "LARGE") {
                totalCost -= 500
            } else if (size == "MAX"){
                totalCost -= 1000
            } else if (size == null){
                totalCost += 0
            }
            totalPay.setText(totalCost.toString()+"원")
            size = "REGULAR"
        }
        largeBtn!!.setOnClickListener {
            regularBtn.setBackgroundResource(R.drawable.mainbtn)
            largeBtn!!.setBackgroundResource(R.drawable.accountbtn)
            maxBtn!!.setBackgroundResource(R.drawable.mainbtn)
            if (size == "REGULAR") {
                totalCost += 500
            } else if (size == "MAX"){
                totalCost -= 500
            } else if (size == null) {
                totalCost += 500
            }
            totalPay.setText(totalCost.toString()+"원")
            size = "LARGE"
        }
        maxBtn!!.setOnClickListener {
            regularBtn.setBackgroundResource(R.drawable.mainbtn)
            largeBtn!!.setBackgroundResource(R.drawable.mainbtn)
            maxBtn!!.setBackgroundResource(R.drawable.accountbtn)
            if (size == "LARGE") {
                totalCost += 500
            } else if (size == "REGULAR"){
                totalCost += 1000
            } else if (size == null) {
                totalCost += 1000
            }
            totalPay.setText(totalCost.toString()+"원")
            size = "MAX"
        }

        var takeOutBtn: Button? = view.findViewById<Button>(R.id.takeOutBtn)
        var hollBtn: Button? = view.findViewById<Button>(R.id.hollBtn)
        takeOutBtn!!.setOnClickListener {
            takeOutBtn.setBackgroundResource(R.drawable.accountbtn)
            hollBtn!!.setBackgroundResource(R.drawable.mainbtn)
            receive = "TAKE OUT"
        }
        hollBtn!!.setOnClickListener {
            takeOutBtn.setBackgroundResource(R.drawable.mainbtn)
            hollBtn.setBackgroundResource(R.drawable.accountbtn)
            receive = "DINE"
        }

        var toppingBtn: Button? = view.findViewById<Button>(R.id.toppingBtn)
        toppingBtn!!.setOnClickListener{
            var toppingPopupView = getLayoutInflater().inflate(R.layout.toppingdrawerlayout, null);
            var toppingDialog = AlertDialog.Builder(context).create()

            toppingDialog.setView(toppingPopupView)
            toppingDialog.show()
            toppingDialog.window!!.setLayout(750, 650)
            toppingEvent(toppingPopupView,toppingDialog,totalPay, totalCost)
        }

        var basketBtn: Button? = view.findViewById<Button>(R.id.payBtn)
        basketBtn!!.setOnClickListener {
            if (temperature == null || size == null || receive == null) {
                var popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
                var alertdialog = AlertDialog.Builder(context).create()
                var popupBackBtn = popupView.findViewById<Button>(R.id.backBtn)
                var popupText = popupView.findViewById<TextView>(R.id.popuptext)
                popupText.setText("필수 선택 요소를 선택해주세요.")
                popupBackBtn!!.setOnClickListener {
                    alertdialog.hide()
                }
                alertdialog.setView(popupView)
                alertdialog.show()
                alertdialog.window!!.setLayout(600, 400)
            } else {
                var popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
                var alertdialog = AlertDialog.Builder(context).create()
                var popupBackBtn = popupView.findViewById<Button>(R.id.backBtn)
                var popupText = popupView.findViewById<TextView>(R.id.popuptext)
                popupText.setText("장바구니에 음료가 담겼습니다.")
                popupBackBtn!!.setOnClickListener {
                    alertdialog.hide()
                    var beverageSetPage = activity as BeverageSetPage
                    if (toppingList.count() == 0){
                        var nullTopping = arrayListOf<Int>(0,0,0,0)
                        beverageSetPage.makeNotification(menuName,totalCost,beverageCount)
                        beverageSetPage.putData(menuName,menuImage,totalCost,beverageCount,temperature!!,size!!,receive!!,nullTopping)
                    } else {
                        beverageSetPage.makeNotification(menuName,totalCost + toppingList[0] * 500 + toppingList[1] * 500 + toppingList[2] * 500 + toppingList[3] * 500, beverageCount)
                        beverageSetPage.putData(menuName,menuImage,totalCost,beverageCount,temperature!!,size!!,receive!!,toppingList) // 지울것
                    }
                }
                alertdialog.setView(popupView)
                alertdialog.show()
                alertdialog.window!!.setLayout(600, 400)
            }
        }
    }

    fun toppingEvent(toppingPopupView: View, toppingDialog: Dialog, totalPay : TextView, totalCost: Int){
        var chocolateCount : Int = 0
        var creamCount: Int = 0
        var pullCount: Int = 0
        var shirupCount: Int = 0

        var chocolateText: TextView? = toppingPopupView.findViewById<TextView>(R.id.chocolateCount)
        var creamText: TextView? = toppingPopupView.findViewById<TextView>(R.id.creamCount)
        var pullText: TextView? = toppingPopupView.findViewById<TextView>(R.id.pullCount)
        var shirupText: TextView? = toppingPopupView.findViewById<TextView>(R.id.shirupCount)

        var chocolateMinus : Button? = toppingPopupView.findViewById<Button>(R.id.chocolateMinus)
        chocolateMinus!!.setOnClickListener{
            if (chocolateCount >= 1) {
                chocolateCount -= 1
                chocolateText!!.setText(chocolateCount.toString())
            }
        }

        var creamMinus : Button? = toppingPopupView.findViewById<Button>(R.id.creamMinus)
        creamMinus!!.setOnClickListener{
            if (creamCount >= 1) {
                creamCount -= 1
                creamText!!.setText(creamCount.toString())
            }
        }

        var pullMinus : Button? = toppingPopupView.findViewById<Button>(R.id.pullMinus)
        pullMinus!!.setOnClickListener{
            if (pullCount >= 1) {
                pullCount -= 1
                pullText!!.setText(pullCount.toString())
            }
        }
        var shirupMinus : Button? = toppingPopupView.findViewById<Button>(R.id.shirupMinus)
        shirupMinus!!.setOnClickListener{
            if (shirupCount >= 1) {
                shirupCount -= 1
                shirupText!!.setText(shirupCount.toString())
            }
        }

        var chocolatePlus : Button? = toppingPopupView.findViewById<Button>(R.id.chocolatePlus)
        chocolatePlus!!.setOnClickListener{
            chocolateCount += 1
            chocolateText!!.setText(chocolateCount.toString())
        }
        var creamPlus : Button? = toppingPopupView.findViewById<Button>(R.id.creamPlus)
        creamPlus!!.setOnClickListener{
            creamCount += 1
            creamText!!.setText(creamCount.toString())
        }
        var pullPlus : Button? = toppingPopupView.findViewById<Button>(R.id.pullPlus)
        pullPlus!!.setOnClickListener{
            pullCount += 1
            pullText!!.setText(pullCount.toString())
        }
        var shirupPlus : Button? = toppingPopupView.findViewById<Button>(R.id.shirupPlus)
        shirupPlus!!.setOnClickListener{
            shirupCount += 1
            shirupText!!.setText(shirupCount.toString())
        }

        var closeBtn : Button? = toppingPopupView.findViewById<Button>(R.id.close)
        closeBtn!!.setOnClickListener{
            toppingList.add(chocolateCount)
            toppingList.add(creamCount)
            toppingList.add(pullCount)
            toppingList.add(shirupCount)
            totalPay.setText((totalCost + (chocolateCount * 500) + (creamCount * 500) + (pullCount * 500) + (shirupCount * 500)).toString() + "원")
            toppingDialog.hide()
        }
        var backBtn : Button? = toppingPopupView.findViewById<Button>(R.id.back)
        backBtn!!.setOnClickListener{
            toppingDialog.hide()
        }
    }
}
