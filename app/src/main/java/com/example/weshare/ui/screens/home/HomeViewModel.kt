package com.example.weshare.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.weshare.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {

var user = User()


}