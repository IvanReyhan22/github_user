package com.example.githubuser.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.githubuser.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(SettingActivity::class.java)
    }

    @Test
    fun checkSwitch() {
        onView(withId(R.id.switch_theme)).perform(click())
        onView(withId(R.id.switch_theme)).check(matches(isClickable()))
    }
}