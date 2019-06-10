package dev.davidaschmid.BakingApp;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import static org.hamcrest.Matchers.anything;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    public static final String INGREDIENTS = "INGREDIENTS:  350.0 G Bittersweet chocolate (60-70% cacao), 226.0 G unsalted butter" +
        ", 300.0 G granulated sugar, 100.0 G light brown sugar, 5.0 UNIT large eggs, 1.0 TBLSP vanilla extract, 140.0 G all purpose flour, " +
        "40.0 G cocoa powder, 1.5 TSP salt, 350.0 G semisweet chocolate chips, ";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickGridViewItem_OpensIngredientsStepsActivity(){
        onView(withId(R.id.main_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.ingredients_tv)).check(matches(withText(INGREDIENTS)));
    }
}
