package com.example.android.mockdpsdstudents4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "Thomas - MainActivity";

    ArrayList<DPSDStudent> fullDpsdsList = new ArrayList<>();

    ArrayList<DPSDStudent> filteredDpsdsList = new ArrayList<>();

    ListView listview;

    CustomListAdapter adapter;

    private DPSDPreferencesManager prefManager;

    boolean showOnlyShortlistedDpsds;
    boolean showOnlyDissertationPendingDpsds;

    boolean showFilteredList;


    String GOOGLE_SHEET_DATA_URL = "https://gsx2json.com/api?id=1cB7h58GO7h41jsWZBR_101jWAAeeeSresrCUNnq8SMA&sheet=Sheet1";
    RequestQueue queue;

    // 05/06/2012
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializePreferences();

        //prefManager.deleteAllPreferences();

        //initializeData();



        //adapter = new CustomListAdapter(this, R.layout.listview_row, fullDpsdsList);

        //listview.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_shortlist_menu_item:
                Toast.makeText(this, "item 1 clisked", Toast.LENGTH_LONG).show();
                break;
            case R.id.settings_menu_item:
                Toast.makeText(this, "item 2 clisked", Toast.LENGTH_LONG).show();
                Intent settings = new Intent(this, SettingsActivity.class);

                startActivity(settings);
                break;
            case R.id.about_menu_item:
                Toast.makeText(this, "item 3 clisked", Toast.LENGTH_LONG).show();
                break;
            default:
                // should never reach this

        }
        return super.onOptionsItemSelected(item);

    }


    private void initializeViews() {
        listview = findViewById(R.id.listview);
    }

    private void initializePreferences() {
        prefManager = DPSDPreferencesManager.instance(this);

    }

    private void initializeData() {

        Log.i(TAG, "initializeData: running");

        //Gson gson = new Gson();
        String dpsdListJson = prefManager.fetchValueString(DPSDPreferencesManager.ALL_DPSDS);
        Log.i(TAG, "initializeData: is null: "+(dpsdListJson==null));

        if (dpsdListJson == null) {
            // use hardcoded data
            //createStudentsHardcoded();

            // use google sheet data
            createStudentsFromGoogleSheet();
        }
        else {
            //SharedPreferences  prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            //String json = prefs.getString("MyPhotos", null);
            Type type = new TypeToken<ArrayList<DPSDStudent>>() {}.getType();
            fullDpsdsList = gson.fromJson(dpsdListJson, type);
            sortArrayList(fullDpsdsList);
        }

    }



    /**
     * Method that filters already loaded list of DPSDStudent objects, according to user preferences.
     *
     * Simple (but inefficient method) is via for-loop that searches whole list, and filters accordingly.
     *
     * More advanced methods, using add-on libraries or Java Predicates. See more:
     * - https://stackoverflow.com/questions/48539690/filter-custom-objects-in-array-list-or-list
     * - https://stackoverflow.com/questions/122105/how-to-filter-a-java-collection-based-on-predicate
     */
    private void filterListAccordingToPrefs() {

        showOnlyShortlistedDpsds = prefManager.fetchBoolean(DPSDPreferencesManager.SHOW_ONLY_SHORTLIST_SETTING_KEY);
        showOnlyDissertationPendingDpsds = prefManager.fetchBoolean(DPSDPreferencesManager.SHOW_ONLY_DISSERTATION_SETTING_KEY);

        Log.i(TAG, "filterListAccordingToPrefs: onlyshortisted is: "+showOnlyShortlistedDpsds+", and onlyDissertation is: "+showOnlyDissertationPendingDpsds);

        // todo this might not  be right - CHECK !!!
        if (showOnlyDissertationPendingDpsds && showOnlyShortlistedDpsds) {
            filteredDpsdsList = (ArrayList<DPSDStudent>) fullDpsdsList.stream().filter(DPSDStudent::isAllButDissertation).collect(Collectors.toList());
            filteredDpsdsList = (ArrayList<DPSDStudent>) filteredDpsdsList.stream().filter(DPSDStudent::isShortlisted).collect(Collectors.toList());
        }

        else if (showOnlyDissertationPendingDpsds) {

            filteredDpsdsList = (ArrayList<DPSDStudent>) fullDpsdsList.stream().filter(DPSDStudent::isAllButDissertation).collect(Collectors.toList());

            //fullDpsdsList = (ArrayList<DPSDStudent>) fullDpsdsList.stream().filter(DPSDStudent::isAllButDissertation).collect(Collectors.toList());
        }
        else if (showOnlyShortlistedDpsds) {

            filteredDpsdsList = (ArrayList<DPSDStudent>) fullDpsdsList.stream().filter(DPSDStudent::isShortlisted).collect(Collectors.toList());
            Log.i(TAG, "filterListAccordingToPrefs: only shortlisted, size is: "+filteredDpsdsList.size());
            //fullDpsdsList = (ArrayList<DPSDStudent>) fullDpsdsList.stream().filter(DPSDStudent::isShortlisted).collect(Collectors.toList());
        }

        // do nothing - leaving else only for reference
        else {
            //prefManager.deleteAllPreferences();
            //createStudentsHardcoded();


        }


       /* Log.i(TAG, "filterListAccordingToPrefs: adapter is NULL: "+(adapter == null));
        if (adapter != null) {
         //since shortlist AND dissertation settings are FALSE, re-show full-list
            if (showOnlyDissertationPendingDpsds || showOnlyShortlistedDpsds) {
                Log.i(TAG, "filterListAccordingToPrefs: inside filtered list sending, with size: "+filteredDpsdsList.size());
                adapter.setNewData(filteredDpsdsList);
                adapter.notifyDataSetChanged();
            }
            else {
                adapter.setNewData(fullDpsdsList);
                adapter.notifyDataSetChanged();
            }

            //saveLists();
        }*/

    }


    private void giveAdapterRightList() {



        showFilteredList = (showOnlyShortlistedDpsds || showOnlyDissertationPendingDpsds);

        Log.i(TAG, "giveAdapterRightList: runs, adapter is "+(adapter == null)+" and showFilteredList boolean is "+showFilteredList);


        if (adapter == null) {
            if (showFilteredList) {
                adapter = new CustomListAdapter(this, R.layout.listview_row, filteredDpsdsList);
            }
            else {
                adapter = new CustomListAdapter(this, R.layout.listview_row, fullDpsdsList);
            }
            listview.setAdapter(adapter);
            adapter.setFilterFlag(showFilteredList);
        }

        else {
            if (showFilteredList) {
                adapter.setNewData(filteredDpsdsList);
            }
            else {
                adapter.setNewData(fullDpsdsList);
            }
            adapter.setFilterFlag(showFilteredList);
            adapter.notifyDataSetChanged();
        }

        /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "item clicked on pos: "+position, Toast.LENGTH_LONG).show();
            }
        });*/


    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: running");
        initializeData();

        filterListAccordingToPrefs();

        giveAdapterRightList();

        super.onResume();
    }




    private void createStudentsHardcoded() {

        DPSDStudent stu1 = new DPSDStudent("Mitsos Papadimas 1",
                LocalDate.of(2017, 11, 6), 2,
                2016, true);
        fullDpsdsList.add(stu1);

        fullDpsdsList.add(new DPSDStudent("Maria Papadopoulou 2",
                LocalDate.of(2017, 12, 23), 1,
                2014, true));

        fullDpsdsList.add(new DPSDStudent("Makis Pakis 3",
                LocalDate.of(2017, 10, 15), 2,
                2017, false));

        DPSDStudent kogias = new DPSDStudent("Thomas Kogiassss 4", LocalDate.of(2017, 9, 6), 2,
                2019, false);

        kogias.setEmail("tkogias@aegean.gr");
        //kogias.setShortlisted(true);
        fullDpsdsList.add(kogias);

        fullDpsdsList.add(new DPSDStudent("Katerina Mitsou 5",
                LocalDate.of(2017, 3, 6), 1,
                2015, true));

        fullDpsdsList.add(new DPSDStudent("Petros Pappas 6",
                LocalDate.of(2017, 7, 16), 2,
                2017, false));

        fullDpsdsList.add(new DPSDStudent("Pikos Apikos 7",
                LocalDate.of(2021, 1, 11), 2,
                2017, false));

        fullDpsdsList.add(new DPSDStudent("Ntina Loizou 8",
                LocalDate.of(2020, 11, 18), 1,
                2016, false));

        fullDpsdsList.add(new DPSDStudent("Stathis Lappas 9",
                LocalDate.of(2016, 4, 20), 2,
                2016, false));

        fullDpsdsList.add(new DPSDStudent("Giannis Antetokounmpo 10",
                LocalDate.of(2020, 12, 6), 2,
                2016, true));

        fullDpsdsList.add(new DPSDStudent("Tracy Chapman 11",
                LocalDate.of(2017, 3, 6), 1,
                2015, true));

        fullDpsdsList.add(new DPSDStudent("Petros Pappas 12",
                LocalDate.of(2017, 7, 16), 2,
                2017, false));

        fullDpsdsList.add(new DPSDStudent("Roger Waters 13",
                LocalDate.of(2011, 1, 10), 2,
                2017, false));

        fullDpsdsList.add(new DPSDStudent("Ntina Loizou 14",
                LocalDate.of(2018, 5, 7), 1,
                2016, true));

        fullDpsdsList.add(new DPSDStudent("Gail Ann Dorsey 15",
                LocalDate.of(2011, 9, 27), 1,
                2016, false));

        fullDpsdsList.add(new DPSDStudent("Maria Titika 16",
                LocalDate.of(2017, 1, 7), 1,
                2016, true));

        fullDpsdsList.add(new DPSDStudent("Stathis Lappas 17",
                LocalDate.of(2016, 4, 20), 2,
                2016, false));

        fullDpsdsList.add(new DPSDStudent("Lila Pause 18",
                LocalDate.of(2020, 2, 7), 1,
                2016, false));


        Gson gson = new Gson();
        String json = gson.toJson(fullDpsdsList);
        prefManager.storeValueString(DPSDPreferencesManager.ALL_DPSDS, json);

        Log.i(TAG, "createStudentsHardcoded: Creating AND saving JSON'ed HARDCODED list, with size: "+ fullDpsdsList.size());
    }


    private void createStudentsFromGoogleSheet() {

        queue = Volley.newRequestQueue(this);

        googleSheetParse();

    }


    private void googleSheetParse() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                GOOGLE_SHEET_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray rows = jsonObject.getJSONArray("rows");

                    // delegate actual object creation to separate method to keep things cleaner ...
                    createAllStudents(rows);

                    //multiplyList();

                    //Log.d(TAG, "onResponse: student list size is: "+studentList.size());

                    //adapter = new StudentListAdapter(MainActivity.this, R.layout.listview_row, studentList);
                    //listView.setAdapter(adapter);

                    //efficientAdapter = new StudentListAdapterEfficient(MainActivity.this, R.layout.listview_row, studentList);
                    //listView.setAdapter(efficientAdapter);

                    adapter = new CustomListAdapter(MainActivity.this, R.layout.listview_row, fullDpsdsList);

                    listview.setAdapter(adapter);

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON exception: ","exception "+e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError caught: "+error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void createAllStudents(JSONArray rows) throws JSONException {

        for (int i=0; i<rows.length(); i++) {
            JSONObject row = rows.getJSONObject(i);
            int flag = row.getInt("flag");

            // we reached an empty row, break loop !!!
            if (flag == 99) {
                Log.d(TAG, "createAllStudentsList: *** break reached at i="+i);
                break;
            }
            if (flag == 0) {
                Log.d(TAG, "createAllStudentsList: *** continue reached at i="+i);
                continue;
            }

            String name = row.getString("name");

            String birthdate = row.getString("birthdate");
            String sex = row.getString("sex");
            int entryYear = row.getInt("entryYear");

            String abdStr = row.getString("abd");
            boolean abd;
            if (abdStr.equals("TRUE")) {
                abd = true;
            }
            else {
                abd = false;
            }

            // 1 for Female, 2 for Male
            if (sex.equals("F")) {
                fullDpsdsList.add(new DPSDStudent(name, LocalDate.parse(birthdate, formatter), 1, entryYear, abd));
            }
            else {
                fullDpsdsList.add(new DPSDStudent(name, LocalDate.parse(birthdate, formatter), 2, entryYear, abd));
            }

        } // for loop for student creation ends here...

        sortArrayList(fullDpsdsList);

        Gson gson = new Gson();
        String json = gson.toJson(fullDpsdsList);
        prefManager.storeValueString(DPSDPreferencesManager.ALL_DPSDS, json);

        Log.i(TAG, "createStudentsHardcoded: Creating AND saving JSON'ed HARDCODED list, with size: "+ fullDpsdsList.size());
    }



    private void sortArrayList(ArrayList<DPSDStudent> studentsToSort) {

        //String order = mPreferences.getString("order", "by_date");
        String order = prefManager.fetchValueString("order");

        if (order == null) {
            order = new String("by_id");
            Log.i(TAG, "sortArrayList: order key found: NULL");
        }

        else if (order.equals("by_surname")) {
            //billingList.sort(Comparator.comparing(a -> a.getEmail));
            studentsToSort.sort(Comparator.comparing(a -> a.findSurname()));
            Log.i(TAG, "sortArrayList: sorted by surname!");
            Toast.makeText(this, "Ordered by: SURNAME", Toast.LENGTH_SHORT).show();
        }
        else if (order.equals("by_sex")) {
            //billingList.sort(Comparator.comparing(a -> a.getEmail));
            studentsToSort.sort(Comparator.comparing(a -> a.getSex()));
            Log.i(TAG, "sortArrayList: sorted by sex!");
            Toast.makeText(this, "Ordered by: SEX", Toast.LENGTH_SHORT).show();
        }
        else if (order.equals("by_year_of_entry_asc")) {
            studentsToSort.sort(Comparator.comparing(a -> a.getYearOfEntry()));
            Log.i(TAG, "sortArrayList: sorted by year of entry !");
            Toast.makeText(this, "Ordered by: Year of Entry ASC", Toast.LENGTH_SHORT).show();
        }
        else if (order.equals("by_year_of_entry_desc")) {
            //todo fixz this
            studentsToSort.sort(Comparator.comparing(DPSDStudent::getYearOfEntry, Comparator.reverseOrder()));
            // Collections.sort(list, Collections.reverseOrder());
            Log.i(TAG, "sortArrayList: sorted by year of entry !");
            Toast.makeText(this, "Ordered by: Year of Entry DESC", Toast.LENGTH_SHORT).show();
        }
    }
}