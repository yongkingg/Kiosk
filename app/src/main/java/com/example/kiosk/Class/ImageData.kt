package com.example.kiosk.Class

import com.example.kiosk.R


class ImageData {
    var beverageImage = mutableListOf<Int>(
        R.drawable.americano,
        R.drawable.ainsupanner,
        R.drawable.coldbrew,
        R.drawable.caffeelattee,
        R.drawable.whitebeinna,
        R.drawable.coldbrewnitro,
        R.drawable.caffeemoca,
        R.drawable.cappuccino
    )

    var flatccinoImage = mutableListOf<Int>(
        R.drawable.coffeeflatccino,
        R.drawable.peachflatccino,
        R.drawable.strawberryyogurtflatccion,
        R.drawable.topinutflatccino,
        R.drawable.strawberryflatccino
    )

    var shakeAdeImage = mutableListOf<Int>(
        R.drawable.ade,
        R.drawable.jamongade,
        R.drawable.lemonade,
        R.drawable.originshake,
        R.drawable.oreoshake
    )

    var newMenuImage = mutableListOf<Int>(
        R.drawable.strawberrylattee,
        R.drawable.strawberrymilkflatccino,
        R.drawable.strawberrypeach,
        R.drawable.strawberrypeachhot
    )
    var hotMenuImage = mutableListOf<Int>(
        R.drawable.americano,
        R.drawable.ainsupanner,
        R.drawable.strawberrylattee,
        R.drawable.strawberrymilkflatccino,
        R.drawable.strawberrypeach
    )
    var image = mutableListOf<MutableList<Int>>(newMenuImage,beverageImage,flatccinoImage,shakeAdeImage,hotMenuImage)



}