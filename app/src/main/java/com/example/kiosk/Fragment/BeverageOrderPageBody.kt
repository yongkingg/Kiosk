package com.example.kiosk.Fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Dimension
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kiosk.Activity.BeverageOrderPage
import com.example.kiosk.DataInterface
import com.example.kiosk.Class.ImageData
import com.example.kiosk.R
import com.example.kiosk.Retrofit.*
import okhttp3.Response
import org.json.JSONObject
import javax.security.auth.callback.Callback

class BeverageOrderPageBody: Fragment() {
    lateinit var orderBeverage: DataInterface
    lateinit var lang : String
    lateinit var category : String
    var categoryList = arrayOf(R.array.newMenu, R.array.coffeeList, R.array.flatccino, R.array.shakeade)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        orderBeverage = context as DataInterface
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        var view: View = inflater.inflate(R.layout.categorybody, container, false)
        var getlang = arguments?.getString("lang")!!
        if (getlang == "ko") {
            lang = "kr"
        }
        var parentLayout = view.findViewById<LinearLayout>(R.id.mainLayout)
        setCategory(view, parentLayout)

        return view
    }

    fun setCategory(view: View, layout: LinearLayout) {
        var mainLayout = view.findViewById<LinearLayout>(R.id.mainHorizontalScrollView)
        var buttonParams = LinearLayout.LayoutParams(200, 100)

        var retrofit = RetrofitClient.initRetrofit()
        var getCategoryApi = retrofit.create(GetCategoryAPI::class.java)
        getCategoryApi.sendLang(lang).enqueue(object : retrofit2.Callback<GetCategory> {
            override fun onFailure(call: retrofit2.Call<GetCategory>, t: Throwable) {
            }
            override fun onResponse(call: retrofit2.Call<GetCategory>, response: retrofit2.Response<GetCategory>) {
                var id = arrayListOf<Int>(R.id.coffee,R.id.beverage)
                category = response.body()!!.data[0].category_name
                for (index in 0 until response.body()!!.data.count()) {
                    var button = Button(context)
                    button.layoutParams = buttonParams
                    button.setText(response.body()!!.data[index].category_name)
                    button.id = id[index]
                    button.setTypeface(resources.getFont(R.font.body))
                    button.setBackgroundResource(R.drawable.categorybtn)
                    mainLayout.addView(button)
                }

                var coffee = view.findViewById<Button>(R.id.coffee)
                fetchMenu(view,response.body()!!.data[0].category_name,layout)
                var beverage = view.findViewById<Button>(R.id.beverage)

                coffee.setBackgroundResource(R.drawable.accountbtn)
                coffee.setOnClickListener {
                    coffee.setBackgroundResource(R.drawable.accountbtn)
                    beverage.setBackgroundResource(R.drawable.categorybtn)
                    if (category != null) {
                        layout.removeAllViews()
                    }
                    category = response.body()!!.data[0].category_name
                    fetchMenu(view, category!!, layout)
                }
                beverage.setOnClickListener {
                    coffee.setBackgroundResource(R.drawable.categorybtn)
                    beverage.setBackgroundResource(R.drawable.accountbtn)
                    if (category != null) {
                        layout.removeAllViews()
                    }
                    category = response.body()!!.data[1].category_name
                    fetchMenu(view, category!!, layout)
                }
            }
        })
    }

    fun fetchMenu(view: View, category: String, parentLayout: LinearLayout) {
        var retrofit = RetrofitClient.initRetrofit()
        var getMenu = retrofit.create(GetMenuAPI::class.java)
        getMenu.sendCategoryValue(category,lang).enqueue(object : retrofit2.Callback<GetMenu> {
            override fun onFailure(call: retrofit2.Call<GetMenu>, t: Throwable) {
            }
            override fun onResponse(call: retrofit2.Call<GetMenu>, response: retrofit2.Response<GetMenu>) {
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                val imageParams = LinearLayout.LayoutParams(250, 250)
                imageParams.setMargins(10, 10, 0, 0)

                for (index in 0 until response.body()!!.data.size) {
                    var linearLayout = LinearLayout(context)
                    linearLayout.orientation = LinearLayout.HORIZONTAL
                    linearLayout.layoutParams = layoutParams
                    parentLayout.addView(linearLayout)

                    var beverageImage = ImageView(context)
                    beverageImage.id = index
                    beverageImage.layoutParams = imageParams
                    linearLayout.addView(beverageImage)

                    var textLayout = LinearLayout(context)
                    textLayout.orientation = LinearLayout.VERTICAL
                    linearLayout.layoutParams = layoutParams
                    linearLayout.addView(textLayout)

                    var beverageName = TextView(context)
                    beverageName.setText(response.body()!!.data[index].menu_name)
                    beverageName.setTextSize(Dimension.SP, 20.0f)
                    beverageName.setTextColor(Color.BLACK)
                    beverageName.setPadding(10, 50, 0, 0)
                    textLayout.addView(beverageName)

                    var beverageCost = TextView(context)
                    beverageCost.setText(response.body()!!.data[index].menu_price.toString())
                    beverageCost.setTextSize(Dimension.SP, 20.0f)
                    beverageCost.setTextColor(Color.BLACK)
                    beverageCost.setPadding(10, 50, 0, 0)
                    textLayout.addView(beverageCost)
                }


                for (index in 0 until response.body()!!.data.size) {
                    var menuImage = view.findViewById<ImageView>(index)
                    Glide.with(context!!).load("http://52.79.157.214:3000/" + response.body()!!.data[index].menu_image).into(menuImage)
                }
                initEvent(view,category)
            }
        })
    }

        fun initEvent(view: View, category: String) {
            var retrofit = RetrofitClient.initRetrofit()
            var getMenu = retrofit.create(GetMenuAPI::class.java)
            getMenu.sendCategoryValue(category,lang).enqueue(object : retrofit2.Callback<GetMenu> {
                override fun onFailure(call: retrofit2.Call<GetMenu>, t: Throwable) {
                }
                override fun onResponse(call: retrofit2.Call<GetMenu>, response: retrofit2.Response<GetMenu>) {
                    var menu = response.body()!!.data
                    for (index in 0 until menu.count()) {
                        var beverageBtn : ImageView? = view.findViewById(index)
                        beverageBtn!!.setOnClickListener{
                            var beverageOrderPage = context as BeverageOrderPage
                            beverageOrderPage.beverageSet(index,category,lang)
                        }
                    }
                }
            })
        }
    }




