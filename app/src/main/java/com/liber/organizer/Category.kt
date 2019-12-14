package com.liber.organizer

class Category {
    var categoryId: Int = 0
    var categoryName: String = ""
    var categoryIcon: Int = R.drawable.business_contact

    constructor(categoryName: String) {
        this.categoryName = categoryName
    }

    constructor() {

    }
}