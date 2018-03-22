package com.project.android.bakingapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Hadeer Khalifa on 3/20/2018.
 */

public class PickingRecipeTest {
    private final static String RECIPE_NAME = "Yellow Cake";
    private final static String STEP_NAME = "Combine dry ingredients";
    private final static String STEP_DESC = "RCombine the cake flour, 400 grams (2 cups) of sugar, baking powder, and 1 teaspoon of salt in the bowl of a stand mixer." +
            " Using the paddle attachment, beat at low speed until the dry ingredients are mixed together, about one minute";
    private final static String STEP_NUM_2 = "Step 2 of 12";
    private final static String STEP_NUM_1 = "Step 1 of 12";
    private final static int RECIPE_LIST_SCROLL_POSITION = 2;
    private final static int STEPS_WITH_MEDIA = 0;
    private final static int STEPS_WITHOUT_MEDIA = 1;
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private SimpleIdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = (SimpleIdlingResource) mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void testRecipeNameAtPosition() {


        onView(withId(R.id.RecipesRecyclerView))
                .perform(RecyclerViewActions
                        .scrollToPosition(RECIPE_LIST_SCROLL_POSITION));


        onView(withText(RECIPE_NAME))
                .check(matches(isDisplayed()));
    }


    @Test
    public void testClickRecipeAtPosition() throws Exception {


        onView(withId(R.id.RecipesRecyclerView))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));


        onView(withId(R.id.recipeIngredientsTxtView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.step_list))
                .perform(RecyclerViewActions
                        .scrollToPosition(STEPS_WITH_MEDIA));


        onView(withText(STEP_NAME))
                .check(matches(isDisplayed()));


        onView(withId(R.id.step_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(STEPS_WITH_MEDIA, click()));


        onView(withId(R.id.prev_step_img)).check(matches(isDisplayed()));
        onView(withId(R.id.next_step_img)).check(matches(isDisplayed()));
        onView(withId(R.id.step_desc_txtView)).check(matches(isDisplayed()));
        onView(withId(R.id.stepsCountTxtView)).check(matches(isDisplayed()));

        onView((withId(R.id.step_desc_txtView))).check(matches(withText(STEP_DESC)));
        onView((withId(R.id.stepsCountTxtView))).check(matches(withText(STEP_NUM_2)));

        onView(withId(R.id.exo_play)).check(matches(isDisplayed()));
        onView(withId(R.id.thumbnailImg)).check(matches(not(isDisplayed())));
    }


    @Test
    public void testClickStepAtPosition() throws Exception {

        onView(withId(R.id.RecipesRecyclerView))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));


        onView(withId(R.id.step_list))
                .perform(RecyclerViewActions
                        .scrollToPosition(STEPS_WITHOUT_MEDIA));

        onView(withId(R.id.step_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(STEPS_WITHOUT_MEDIA, click()));

        onView((withId(R.id.stepsCountTxtView)))
                .check(matches(withText(STEP_NUM_1)));

//        onView(withId(R.id.thumbnailImg))
//                .check(matches(isDisplayed()));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
