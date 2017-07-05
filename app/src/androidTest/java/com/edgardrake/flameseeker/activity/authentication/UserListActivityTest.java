package com.edgardrake.flameseeker.activity.authentication;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.edgardrake.flameseeker.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Created by Edgar Drake on 05-Jul-17.
 */
@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    @Rule
    public ActivityTestRule<UserListActivity> rule =
        new ActivityTestRule<UserListActivity>(UserListActivity.class);

    @Test
    public void testAddUser() {
        onView(withId(R.id.add_user)).perform(click());
    }
}