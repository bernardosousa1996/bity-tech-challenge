package com.example.bity.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class TransactionListDataModel(
    val status: String,
    val message: String,
    val result: List<TransactionDataModel>
)

@Parcelize
data class TransactionDataModel(
    val timeStamp: String,
    val hash: String,
    val to: String,
    val value: String,
) : Parcelable