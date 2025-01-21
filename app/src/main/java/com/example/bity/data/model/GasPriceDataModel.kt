package com.example.bity.data.model

import com.google.gson.annotations.SerializedName

data class GasPriceDataModel(
    val status: String,
    val message: String,
    val result: GasPriceResult
)

data class GasPriceResult(
    @SerializedName("SafeGasPrice") val safeGasPrice: String?,
    @SerializedName("ProposeGasPrice") val proposeGasPrice: String?,
    @SerializedName("FastGasPrice") val fastGasPrice: String?
)