package com.example.data_store_demo;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.allOf;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityRecyclerViewTest {

    ViewInteraction viRvStudent;
    ViewInteraction viMSSV;
    ViewInteraction viEmail;
    ViewInteraction viName;



    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void Setup(){

        viRvStudent = onView(withId(R.id.rv_student));
        viMSSV = onView(withId(R.id.mssv));
        viName = onView(withId(R.id.name));
        viEmail = onView(withId(R.id.email));
    }

    @Test
    public void recyclerViewTest(){
        onView(withId(R.id.search_text)).perform(ViewActions.closeSoftKeyboard());
        ViewAction itemViewAction = actionOnItemView(withId(R.id.edit_student), click());
        viRvStudent.perform(RecyclerViewActions.actionOnItemAtPosition(0, itemViewAction));

        onView(withText("Edit Student")).check(matches(isDisplayed()));
        onView(withText("SAVE")).check(matches(isDisplayed()));

        onView(withText("CANCEL")).perform(click());
        ViewAction itemViewAction2 = actionOnItemView(withId(R.id.delete_student), click());
        viRvStudent.perform(RecyclerViewActions.actionOnItemAtPosition(1, itemViewAction2));

        
        // Match the text in an item below the fold and check that it's displayed.

    }
    public static ViewAction actionOnItemView(Matcher<View> matcher, ViewAction action) {

        return new ViewAction() {

            @Override public String getDescription() {
                return String.format("performing ViewAction: %s on item matching: %s", action.getDescription(), StringDescription.asString(matcher));
            }

            @Override public Matcher<View> getConstraints() {
                return allOf(withParent(isAssignableFrom(RecyclerView.class)), isDisplayed());
            }

            @Override public void perform(UiController uiController, View view) {
                List<View> results = new ArrayList<>();
                for (View v : TreeIterables.breadthFirstViewTraversal(view)) {
                    if (matcher.matches(v)) results.add(v);
                }
                if (results.isEmpty()) {
                    throw new RuntimeException(String.format("No view found %s", StringDescription.asString(matcher)));
                } else if (results.size() > 1) {
                    throw new RuntimeException(String.format("Ambiguous views found %s", StringDescription.asString(matcher)));
                }
                action.perform(uiController, results.get(0));
            }
        };
    }
}

