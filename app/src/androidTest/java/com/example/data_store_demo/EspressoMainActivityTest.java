package com.example.data_store_demo;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.allOf;

import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoMainActivityTest {

    ViewInteraction viSearchView;
    ViewInteraction viBtnAdd;
    ViewInteraction viRvStudent;
    ViewInteraction viMSSV;
    ViewInteraction viEmail;
    ViewInteraction viName;
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void Setup(){
        viSearchView = onView(withId(R.id.search_text));
        viBtnAdd = onView(withId(R.id.add_student));
        viRvStudent = onView(withId(R.id.rv_student));
        viMSSV = onView(withId(R.id.mssv));
        viName = onView(withId(R.id.name));
        viEmail = onView(withId(R.id.email));
    }
    @Test
    public void initTest(){

        viSearchView.check(matches(isDisplayed()));
        viSearchView.check(isCompletelyLeftOf(withId(R.id.add_student)));
        viBtnAdd.check(matches(isDisplayed()));
        viRvStudent.check(matches(isDisplayed()));
    }

    @Test
    public void openAddNewStudentDialog(){
        viBtnAdd.perform(click());
        onView(withText("Add New Student")).check(matches(isDisplayed()));
        viMSSV.check(matches(isDisplayed()));
        viName.check(matches(isDisplayed()));
        viEmail.check(matches(isDisplayed()));
        onView(withId(R.id.spinner)).check((matches(isDisplayed())));
        onView(withText("ADD")).check(matches(isDisplayed()));
        onView(withText("CANCEL")).check(matches(isDisplayed()));

    }

    @Test
    public void addNewStudentWithInput() throws InterruptedException {
        NameGenerator nameGenerator = new NameGenerator();
        viBtnAdd.perform(click());
        viName.perform(ViewActions.typeText(nameGenerator.getName()));
        int randomMSSV = new Random().nextInt((99999999-10000000)+1)+10000000;
        String MSSV = Integer.toString(randomMSSV);
        viMSSV.perform(ViewActions.typeText(MSSV));
        viEmail.perform(ViewActions.typeText("abc@gmail.com"));
        onView(withText("ADD")).perform(click());

        onView(withText("Add New Student")).check(doesNotExist());
        int timeout = 0;
        while(timeout < 10000) {
            try {
                onView(withText(MSSV)).check(matches(isDisplayed()));
                break;
            } catch (Exception e) {
                Thread.sleep(1000L);
                timeout += 1000L;
            }
        }


    }

   @Test
    public void search() throws InterruptedException {
       viSearchView.perform(click());
       viSearchView.perform(typeText("19521853"));

      // onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText("19521953"));
       int timeout = 0;
        while(timeout < 10000) {
            try {
                onView(withText("19521853")).check(matches(isDisplayed()));
                break;
            } catch (Exception e) {
                Thread.sleep(1000L);
                timeout += 1000L;
            }
        }

   }
    @Test
    public void openEditStudent(){
       // viRvStudent.perform(RecyclerViewActions.actionOnItemAtPosition())
        viRvStudent.check(matches(isDisplayed()));
    }
}
