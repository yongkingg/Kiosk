package com.example.kiosk.Fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kiosk.Activity.BasketPage
import com.example.kiosk.Activity.PayPage
import com.example.kiosk.R

class BasketPageBody: Fragment() {
    lateinit var userId : String
    var layout = mutableListOf<LinearLayout>()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        var view: View = inflater.inflate(R.layout.basketpagebody, container, false)

        var getBasketCount = arguments?.getString("basketCount")
        userId = arguments?.getString("userId")!!

        var basketMenu: ArrayList<ArrayList<String>> = ArrayList()
        for (index in 0 until getBasketCount!!.toInt()) {
            var getMenu = arguments?.getStringArrayList("menu" + "$index")
            basketMenu.add(getMenu!!)
        }
        printBasket(basketMenu, getBasketCount, view)
        deleteMenuEvent(getBasketCount, view)
        payEvent(basketMenu,view)
        return view
    }

    fun printBasket(basketMenu: ArrayList<ArrayList<String>>, count: String?, view: View) {
        var parentLayout = view.findViewById<LinearLayout>(R.id.basketPageLayout)
        var layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        var imageParams = LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.MATCH_PARENT)
        var textParams = LinearLayout.LayoutParams(410, LinearLayout.LayoutParams.WRAP_CONTENT)

        for (index in 0 until count!!.toInt()) {
            var linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            layoutParams.setMargins(10, 30, 10, 0)
            linearLayout.layoutParams = layoutParams
            parentLayout.addView(linearLayout)
            layout.add(linearLayout)

            var image = ImageView(context)
            image.setImageResource(R.drawable.beverage)
            image.layoutParams = imageParams
            Glide.with(requireContext()).load("http://52.79.157.214:3000${basketMenu[index][1]}").into(image)
            linearLayout.addView(image)

            var textLayout = LinearLayout(context)
            textLayout.orientation = LinearLayout.VERTICAL
            textLayout.layoutParams = textParams
            linearLayout.addView(textLayout)

            var name = TextView(context)
            textParams.setMargins(10, 0, 10, 0)
            name.layoutParams = textParams
            name.textSize = 30.0f
            name.setText(basketMenu!![index][0] + "\n X ${basketMenu!![index][3]}")
            name.setTypeface(resources.getFont(R.font.body))
            name.setTextColor(Color.BLACK)
            textLayout.addView(name)

            var temp = TextView(context)
            textParams.setMargins(10, 0, 10, 0)
            temp.layoutParams = textParams
            temp.textSize = 25.0f
            temp.setText(basketMenu!![index][4])
            temp.setTypeface(resources.getFont(R.font.body))
            temp.setTextColor(Color.BLACK)
            textLayout.addView(temp)

            var size = TextView(context)
            textParams.setMargins(10, 0, 10, 0)
            size.layoutParams = textParams
            size.setText(basketMenu[index][5])
            size.textSize = 25.0f
            size.setTypeface(resources.getFont(R.font.body))
            size.setTextColor(Color.BLACK)
            textLayout.addView(size)

            var button = ImageView(context)
            button.id = index
            button.setImageResource(R.drawable.close)
            button.layoutParams = LinearLayout.LayoutParams(100, 100).apply {
                gravity = Gravity.CENTER
            }
            linearLayout.addView(button)
        }
    }

    fun deleteMenuEvent(count: String?, view: View) {
        for (index in 0 until count!!.toInt()) {
            var parentLayout = view.findViewById<LinearLayout>(R.id.basketPageLayout)
            var deleteMenuBtn: ImageView? = view.findViewById<ImageView>(index)
            deleteMenuBtn!!.setOnClickListener {
                var basketPage = activity as BasketPage
                basketPage.deleteMenu(index)
                parentLayout.removeView(layout[index])
            }
        }
    }

    fun payEvent(basketMenu: ArrayList<ArrayList<String>> ,view: View) {
        var payStart: Button? = view.findViewById<Button>(R.id.payStartBtn)
        payStart!!.setOnClickListener {
            var basketPage = activity as BasketPage
            for (index in 0 until basketMenu.count()){
                basketPage.clearList()
            }

            var intent = Intent(context,PayPage::class.java)
            intent.putExtra("count",basketPage.myService.basket.count().toString())
            for (index in 0 until basketPage.myService.basket.count()){
                intent.putStringArrayListExtra("menu"+"$index",basketPage.myService.basket[index])
            }
            intent.putExtra("userId",userId)
            startActivity(intent)
        }
    }
}




