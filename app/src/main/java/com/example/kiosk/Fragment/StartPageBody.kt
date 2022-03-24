package com.example.kiosk.Fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kiosk.*
import com.example.kiosk.Class.ImageData

class StartPageBody: Fragment() {
    lateinit var orderBeverage : DataInterface
    lateinit var startOrder : ChangeActivitiy
    lateinit var image: ImageData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        startOrder = context as ChangeActivitiy
        orderBeverage = context as DataInterface
    }
    override fun onCreateView(inflater:LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view : View =  inflater.inflate(R.layout.startpagebody,container,false)
        var hotMenu = resources.getStringArray(R.array.hotMenu)
        var newMenu = resources.getStringArray(R.array.newMenu)
        fetchMenu(view,hotMenu,newMenu)
        initEvent(view)
        return view
    }

    fun fetchMenu(view:View,hotMenu : Array<String>,newMenu: Array<String>) {
        image = ImageData()

        var hotMenuParentLayout = view.findViewById<LinearLayout>(R.id.hotMenuLayout)
        var newMenuParentLayout = view.findViewById<LinearLayout>(R.id.newMenuLayout)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val imageParams = LinearLayout.LayoutParams(250, 250)

        for (index in 0 until hotMenu.count()){
            var hotMenuLinearLayout = LinearLayout(context)
            hotMenuLinearLayout.setOrientation(LinearLayout.VERTICAL)
            hotMenuLinearLayout.layoutParams = layoutParams
            hotMenuParentLayout.addView(hotMenuLinearLayout)

            var hotMenuImage = ImageView(context)
            Glide.with(this).load(image.image[4][index]).override(250,250).into(hotMenuImage)
            hotMenuImage.layoutParams = imageParams
            hotMenuLinearLayout.addView(hotMenuImage)

            var hotMenuCost = TextView(context)
            hotMenuCost.setText(hotMenu[index])
            hotMenuCost.setTextSize(Dimension.SP, 15.0f)
            hotMenuCost.setTextColor(Color.BLACK)
            hotMenuCost.gravity = Gravity.CENTER
            hotMenuCost.setPadding(10, 50, 0, 0)
            hotMenuLinearLayout.addView(hotMenuCost)
        }

        for (index in 0 until newMenu.count()){
            var newMenuLinearLayout = LinearLayout(context)
            newMenuLinearLayout.setOrientation(LinearLayout.VERTICAL)
            newMenuLinearLayout.layoutParams = layoutParams
            newMenuParentLayout.addView(newMenuLinearLayout)

            var newMenuImage = ImageView(context)
            Glide.with(this).load(image.image[0][index]).override(250,250).into(newMenuImage)
            newMenuImage.layoutParams = imageParams
            newMenuLinearLayout.addView(newMenuImage)

            var newCakeCost = TextView(context)
            newCakeCost.setText(newMenu[index])
            newCakeCost.gravity = Gravity.CENTER
            newCakeCost.setTextSize(Dimension.SP, 15.0f)
            newCakeCost.setTextColor(Color.BLACK)
            newCakeCost.setPadding(10, 50, 0, 0)
            newMenuLinearLayout.addView(newCakeCost)
        }
    }

    fun initEvent(view:View){
        var startOrderBtn = view.findViewById<Button>(R.id.beverageOrderBtn)
        startOrderBtn!!.setOnClickListener{
            startOrder.signal("1")
        }
    }
}