package com.example.weathermaps.model

data class PostModel(
    var usernamePost: String = "",
    var titlePost: String = "",
    var contentPost: String = "",
    var locationPost: String = "",
    var datePost: String = "",
    var imagePost: List<String>? = null,
)