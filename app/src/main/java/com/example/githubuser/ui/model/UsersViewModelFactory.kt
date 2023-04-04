package com.example.githubuser.ui.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UsersRepository
import com.example.githubuser.di.Injection

class UsersViewModelFactory private constructor(private val usersRepository: UsersRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            return UsersViewModel(usersRepository) as T
        }

        throw IllegalArgumentException("Uknown ViewModel Class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: UsersViewModelFactory? = null
        fun getInstance(context: Context): UsersViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UsersViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}

