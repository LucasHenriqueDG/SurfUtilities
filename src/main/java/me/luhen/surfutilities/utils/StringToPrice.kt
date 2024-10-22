package me.luhen.surfutilities.utils

import me.luhen.surfutilities.SurfUtilities
import java.math.BigDecimal
import java.util.*


object StringToPrice {

    val plugin = SurfUtilities.instance

    fun extractNumbers(input: String?): MutableList<BigDecimal> {

        val newString = input?.lowercase(Locale.getDefault())
        val prices = mutableListOf<BigDecimal>()

        if (newString != null) {

            if(newString.contains("b") && newString.contains("s")){

                val tempStrings = newString.removePrefix("b").removeSuffix("s").split(":")


                tempStrings.forEach{

                    prices.add(it.toBigDecimal())

                }

            } else if(newString.contains("b")){

                val tempStrings = newString.removePrefix("b")

                prices.add(tempStrings.toBigDecimal())

            } else{

                val tempStrings = newString.removeSuffix("s")

                prices.add(tempStrings.toBigDecimal())

            }

        }

        return prices

    }

    fun checkTransactionType(string: String): String{

        return if(string.lowercase(Locale.getDefault()).contains("b") && string.lowercase(Locale.getDefault()).contains("s")){
            "both"
        } else if(string.lowercase(Locale.getDefault()).contains("b")){
            "buy"
        } else {
            "sell"
        }

    }

}

