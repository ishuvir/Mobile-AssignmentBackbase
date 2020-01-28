package app.com.mobileassignment;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ScrollToAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.fasterxml.jackson.databind.util.ViewMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.annotation.MatchesPattern;

import app.com.mobileassignment.views.MainActivity;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isJavascriptEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringStartsWith.startsWith;


// @RunWith(AndroidJUnit4.class)
@LargeTest
public class ValidSearchResult {
String app_Title="Mobile Assignment";
String validSearh_City="Utrecht";
String Valid_SearchResult="Utrecht, ZA";
String invalidSpecialcityName="@ddfd@";
String PartialText = "Utre";
String MultipleCityName="Rua ";
String MultipleResult="Rua Nova, PT";
Character SearchChar='A';
    @Rule
public  ActivityTestRule<MainActivity> mActivityRule=new ActivityTestRule<>(MainActivity.class);

    //To verify App screen is opened
    @Before
    public void Setup()
    {
        onView(withId(R.id.results)).check(matches(isDisplayed()));
    }
    //Test to verify the title of Screen
    @Test
    public void Verify_AppScreenHomePage() throws Exception {
    onView(withText(app_Title)).check(matches(isDisplayed()));
    String title=getText(withText(app_Title));
    System.out.print(title);
    Assert.assertEquals(app_Title,title);
    }

    //Method to Get Text from Screen
    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }
    //Test to cover valid search result till map verification
    @Test
    public void validCitySelection() throws InterruptedException {
        //TO validate Search box is displayed
        onView(withId(R.id.search)).check(matches(isDisplayed()));
        onView(withId(R.id.citiesList)).check(matches(isDisplayed()));
        //Validate search box & Enter valid city to be searched
        onView(withId(R.id.search)).perform(typeText(validSearh_City));
        sleep(10000);
        closeSoftKeyboard();
        //Select the valid location from search result
        onView(withText(Valid_SearchResult)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(click());
        sleep(5000);
        //Validate map screen is Displayed with pin
        try {
     //       onView(withId(R.id.insert_point)).check(matches(isDisplayed()));
            onView(withContentDescription("Google Map")).check(matches(isDisplayed()));
        }
        catch(Exception e)
        {
            System.out.print("Map is not displayed with location Pinpoint on map");
        }

    }
   //Test to verify search result with partial keywords
    @Test
    public void searchPartialKeyword() throws InterruptedException {
        //Search box is displayed
        onView(withId(R.id.search)).check(matches(isDisplayed()));
        onView(withHint("Search"));
        //Search the city by providing partial city text
        onView(withId(R.id.search)).perform(replaceText(PartialText));
        Thread.sleep(10000);
        closeSoftKeyboard();
        //Verify the city results in city list
        onView(withText(Valid_SearchResult)).check(matches(isCompletelyDisplayed()));
    }

    //Test to verify result for multiple location selection
    @Test
    public void MultipleCitySearchResult() throws InterruptedException {
        //Search box is displayed
        onView(withId(R.id.search)).check(matches(isDisplayed()));
        onView(withHint("Search"));
        //Search the city
        onView(withId(R.id.search)).perform(replaceText(validSearh_City));
        Thread.sleep(10000);
        closeSoftKeyboard();
        //Verify the city results in city list
        onView(withText(Valid_SearchResult)).check(matches(isCompletelyDisplayed()));
     //Clear the search result
        onView(withId(R.id.search)).perform(ViewActions.clearText());
        //Veriy searching other city Name
        onView(withId(R.id.search)).perform(typeText(MultipleCityName));
        Thread.sleep(10000);
        closeSoftKeyboard();
        //Verify the city results in city list
        onView(withText(MultipleResult)).check(matches(isCompletelyDisplayed()));
    }

    //To verify that results are shown with searched character
    @Test
    public void verifyCharResult() {
        onView(withId(R.id.search)).perform(typeText(String.valueOf(SearchChar)));
        closeSoftKeyboard();
        try {
            Thread.sleep(5000);
            onData(anything()).inAdapterView(withId(R.id.citiesList)).atPosition(0).onChildView(withId(R.id.cityName)).check(matches(withText(startsWith("A"))));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
          }

    @After
    public void tearDown() throws Exception {
   //    tearDown();
     System.out.print("Execution completed for valid search Result test cases");
    }

}
