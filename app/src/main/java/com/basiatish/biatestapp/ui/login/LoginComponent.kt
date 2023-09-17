package com.basiatish.biatestapp.ui.login

import com.basiatish.biatestapp.di.FragmentScope
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