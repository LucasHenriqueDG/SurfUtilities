package me.luhen.surfutilities.utils

import me.luhen.surfutilities.Main
import java.math.BigDecimal


object StringToPrice {

    val plugin = Main.instance

    fun extractNumbers(input: String?): Any? {

        val newString = input?.toLowerCase()
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

        if(string.toLowerCase().contains("b") && string.toLowerCase().contains("s")){
            return "both"
        } else if(string.toLowerCase().contains("b")){
            return "buy"
        } else {
            return "sell"
        }

    }


}

