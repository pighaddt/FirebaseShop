package com.angus.shop

data class item(var title : String, var price : Int, var imageUrl : String){
    constructor() : this("", 0, "")
}