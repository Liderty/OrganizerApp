package com.liber.organizer

class Goal {
    var goalId = 0
    var goalContent = ""
    var goalStatus = 0
    var goalTaskId = 0

    constructor(goalContent: String, goalTaskId: Int) {
        this.goalContent = goalContent
        this.goalTaskId = goalTaskId
    }

    constructor() {}
}