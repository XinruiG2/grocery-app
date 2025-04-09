package com.neu.mobileapplicationdevelopment202430.model

data class UserEntity(
    val username: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user_id: Int?
)

data class SignupResponse(
    val message: String,
    val user_id: Int?
)

data class FullUserEntity(
    val user_id: Int,
    val username: String,
    val password: String,
    val fridge_items: List<FridgeEntity>,
    val grocery_list: List<FridgeEntity>
)
