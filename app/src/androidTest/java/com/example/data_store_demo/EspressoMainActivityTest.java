package com.example.data_store_demo;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.contrib.RecyclerViewActions;
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
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

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
        int rvOldCount = activityTestRule.getActivity().recyclerView.getChildCount();
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
                viRvStudent.check(new RecyclerViewItemCountAssertion(rvOldCount+1));
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
    public void EditStudent() throws InterruptedException {
        onView(withId(R.id.search_text)).perform(ViewActions.closeSoftKeyboard());
        ViewAction itemViewAction = actionOnItemView(withId(R.id.edit_student), click());
        viRvStudent.perform(RecyclerViewActions.actionOnItemAtPosition(0, itemViewAction));

        onView(withText("Edit Student")).check(matches(isDisplayed()));
        onView(withText("SAVE")).check(matches(isDisplayed()));

        int randomMSSV = new Random().nextInt((99999999-10000000)+1)+10000000;
        String MSSV = Integer.toString(randomMSSV);
        viMSSV.perform(clearText()).perform(typeText(MSSV));

        onView(withText("SAVE")).perform(click());

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
    public void DeleteStudent() throws InterruptedException {

        int rvOldCount = activityTestRule.getActivity().recyclerView.getChildCount();
        onView(withId(R.id.search_text)).perform(ViewActions.closeSoftKeyboard());
        ViewAction itemViewAction = actionOnItemView(withId(R.id.delete_student), click());
        if(rvOldCount!=0)
            viRvStudent.perform(RecyclerViewActions.actionOnItemAtPosition(0, itemViewAction)).check(new RecyclerViewItemCountAssertion(rvOldCount-1));



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
    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }
}
