package com.safeneck.safeneck.Models

/**
 * Created by eka on 2017. 9. 25..
 */
class Alarm(var status: Int, var data: data) {
    companion object {
        class data(var token: String, var todayAlarm: String, var weekAlarm: String, var monthAlarm: String)
    }
}