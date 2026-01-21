package com.example.shopenest.homescreen.view

import com.example.shopenest.model.ResponseDraftOrderForRequestCreate



sealed class DraftOrderUiState {

    object Idle : DraftOrderUiState()
    object Loading : DraftOrderUiState()
    data class Success(val data: ResponseDraftOrderForRequestCreate) : DraftOrderUiState()
    data class Error(val message: String) : DraftOrderUiState()

}
