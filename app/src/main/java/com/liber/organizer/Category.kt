package com.liber.organizer

import java.io.Serializable

class Category : Serializable {
    var categoryId: Int = 0
    var categoryName: String = ""
    var categoryIcon: Int = R.drawable.multiple_stars

    constructor(categoryName: String, categoryIcon: Int) {
        this.categoryName = categoryName
        this.categoryIcon = categoryIcon
    }

    constructor() {

    }
}

