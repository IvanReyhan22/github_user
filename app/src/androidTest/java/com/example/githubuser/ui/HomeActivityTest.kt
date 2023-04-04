package com.example.githubuser.ui

import android.view.KeyEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.githubuser.R
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.JVM)
class HomeActivityTest {

    private val query = "Dicoding"

    @Before
    fun setup() {
        ActivityScenario.launch(HomeActivity::class.java)
    }

    @Test
    fun assertSearch() {
        onView(withId(R.id.search)).perform(click())

        onView(withId(androidx.appcompat.R.id.search_src_text))
            .check(matches(isDisplayed()))
            .perform(typeText(query))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))

        onView(withId(R.id.rvUser)).check(matches(hasMinimumChildCount(2)))

        onView(withId(R.id.rvUser))
            .check(matches(hasDescendant(withId(R.id.user_name))))

    }

    @Test
    fun assertFollowUser() {
        Thread.sleep(2000)

        onView(withId(R.id.rvUser)).check(matches(hasMinimumChildCount(1)))

        onView(withId(R.id.rvUser)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.btn_follow)).perform(click())
    }

    @Test
    fun assertUnfollowUser() {
        onView(withId(R.id.favorite)).perform(click())
        Thread.sleep(500)

        onView(withId(R.id.rvUser)).check(matches(hasMinimumChildCount(1)))

        onView(withId(R.id.rvUser)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.btn_follow)).perform(click())
    }
}