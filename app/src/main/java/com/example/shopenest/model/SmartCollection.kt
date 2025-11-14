package com.example.shopenest.model


//you should write all properties as same in write api call .name class is not necessary
data class SmartCollection (

   val id: Long,
    val handle: String ="",
    val title: String ="",

    val updated_at: String ="",

    val body_html: String = "",

    val published_at: String="",

    val sort_order: String =  "",

  //  val templateSuffix: Any?,
    //val disjunctive: Boolean,
   // val rules: List<Rule>,

    val published_scope: String = "",

    val admin_graphql_api_id: String = "",
    val image: ImageBrand,


        )



