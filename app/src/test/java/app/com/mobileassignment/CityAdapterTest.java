package app.com.mobileassignment;

import android.content.Context;
import android.os.Build;
import android.widget.Filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import app.com.mobileassignment.model.City;
import app.com.mobileassignment.utils.JsonMapper;
import app.com.mobileassignment.views.adapters.CityAdapter;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", sdk = Build.VERSION_CODES.KITKAT)
public class CityAdapterTest {

    CityAdapter adapter;
    Context context;
    List<City> cityList;
    JsonMapper jsonMapper;


    @Before
    public void setUp(){
        context = RuntimeEnvironment.application;
        cityList = new ArrayList<>();
        jsonMapper = new JsonMapper();
        populateCityList();
    }



    @After
    public void tearDown(){
        cityList.clear();
    }


    @Test
    public void testNoEmptyList(){
        assertEquals(209557, cityList.size());

    }

    @Test
    public void testAdapterNullList() throws Exception {
        cityList.clear();
        adapter = new CityAdapter(context, cityList);
        assertEquals(0, adapter.getCount());
    }

    @Test
    public void testAdapterFilterabilityWithOneChar(){

        adapter = new CityAdapter(context, cityList);
        adapter.getFilter().filter("A", new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int i) {
                assertEquals(11207, adapter.getCount());
            }
        });
    }

    @Test
    public void testAdapterFilterabilityWithMalaga(){

        adapter = new CityAdapter(context, cityList);
        adapter.getFilter().filter("Malaga", new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int i) {
                assertEquals(5, adapter.getCount());
                for(City city: adapter.getCityList()){
                    assertTrue(city.getName().startsWith("Malaga"));
                }
            }
        });
    }

    @Test
    public void testAdapterFilterabilityWithHilversum(){

        adapter = new CityAdapter(context, cityList);
        adapter.getFilter().filter("Hilversum", new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int i) {
                assertEquals(1, adapter.getCount());
                for(City city: adapter.getCityList()){
                    assertTrue(city.getName().startsWith("Hilversum"));
                }
            }
        });
    }

    @Test
    public void testAdapterFilterabiltiyWithUnkownCity(){

        adapter = new CityAdapter(context, cityList);
        adapter.getFilter().filter("ACityWithNoName", new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int i) {
                assertEquals(0, adapter.getCount());
            }
        });
    }




    private void populateCityList() {
        cityList = jsonMapper.getCityListFromRawFile(context, R.raw.cities);
    }


}
