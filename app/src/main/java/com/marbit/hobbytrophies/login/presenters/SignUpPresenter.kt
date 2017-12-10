package com.marbit.hobbytrophies.login.presenters

import android.content.Context
import com.marbit.hobbytrophies.login.interactors.SignUpInteractor
import com.marbit.hobbytrophies.login.interfaces.SignUpActivityView

class SignUpPresenter(private var context: Context, private var signUpView: SignUpActivityView) {

    val interactor: SignUpInteractor = SignUpInteractor(context)

    fun loginGoogle(){
        signUpView.googleSignIn()
    }

}


