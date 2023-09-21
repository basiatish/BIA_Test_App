package com.basiatish.biatestapp.ui.login.di

import com.basiatish.biatestapp.di.FragmentScope
import com.basiatish.biatestapp.ui.login.LoginFragment
import com.basiatish.biatestapp.ui.login.LoginSmsFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    fun inject(fragment: LoginFragment)
    fun inject(fragment: LoginSmsFragment)

}