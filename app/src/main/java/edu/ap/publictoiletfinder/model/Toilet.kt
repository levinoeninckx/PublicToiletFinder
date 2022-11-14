package edu.ap.publictoiletfinder.model

data class Toilet(
    val id: Int,
    val street: String,
    val xCord: Int,
    val yCord: Int,
    val gender: String,
    val hasDiaperChanger: Boolean,
    val wheelChairFriendly: Boolean
    ){

}