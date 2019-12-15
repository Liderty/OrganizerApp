package com.liber.organizer

import java.io.Serializable

class Category : Serializable {
    var categoryId: Int = 0
    var categoryName: String = ""
    var categoryIcon: Int = R.drawable.business_contact

    constructor(categoryName: String) {
        this.categoryName = categoryName
    }

    constructor() {

    }
}