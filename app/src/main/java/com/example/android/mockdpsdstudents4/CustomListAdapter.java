package com.example.android.mockdpsdstudents4;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomListAdapter extends ArrayAdapter {

    private static final String TAG = "Thomas - CustomListAdapter";

    private final Activity context;

    private ArrayList<DPSDStudent> listOfDpsdsToShow;

    private DPSDPreferencesManager prefManager;

    boolean showFilteredList;


    public CustomListAdapter(@NonNull Activity context, int resource, @NonNull ArrayList listOfDpsdsToShow) {
        super(context, resource, listOfDpsdsToShow);

        this.listOfDpsdsToShow = listOfDpsdsToShow;
        this.context = context;

        initializePreferences();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);

        if (showFilteredList) {
            rowView = getRowViewFilteredList(position, rowView, parent);
        }
        else {
            rowView = getRowViewFullList(position, rowView, parent);
        }
        return rowView;
    }

    private View getRowViewFullList(int position, @Nullable View rowView, @NonNull ViewGroup parent) {
        TextView txtName = rowView.findViewById(R.id.txt_name);
        TextView txtEmail = rowView.findViewById(R.id.txt_email);
        ImageView imgStudentIcon = rowView.findViewById(R.id.img_student);
        ImageButton imgButtonAddRemove = rowView.findViewById(R.id.img_button_add_remove);
        ImageButton imgButtonDissertation = rowView.findViewById(R.id.img_button_dissertation);

        txtName.setText(listOfDpsdsToShow.get(position).getName());
        txtEmail.setText(listOfDpsdsToShow.get(position).getEmail());
        imgStudentIcon.setImageResource(listOfDpsdsToShow.get(position).chooseIcon());

        if (listOfDpsdsToShow.get(position).isAllButDissertation()) {
            imgButtonDissertation.setImageResource(R.drawable.ic_dissertation);
        }
        if (listOfDpsdsToShow.get(position).isShortlisted()) {
            imgButtonAddRemove.setImageResource(R.drawable.ic_remove);
        }
        else {
            imgButtonAddRemove.setImageResource(R.drawable.ic_add);
            //imgButtonAddRemove.setBackgroundResource(ic_add);
        }

        imgButtonAddRemove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (listOfDpsdsToShow.get(position).isShortlisted()) {
                    Toast.makeText(context, "removing item at position: "+position, Toast.LENGTH_SHORT).show();
                    listOfDpsdsToShow.get(position).setShortlisted(false);
                    imgButtonAddRemove.setImageResource(R.drawable.ic_remove);

                }
                else {
                    Toast.makeText(context, "shortlisting item at position: "+position, Toast.LENGTH_SHORT).show();
                    imgButtonAddRemove.setImageResource(R.drawable.ic_add);
                    listOfDpsdsToShow.get(position).setShortlisted(true);

                }
                notifyDataSetChanged();

                Gson gson = new Gson();
                String json = gson.toJson(listOfDpsdsToShow);
                prefManager.storeValueString(DPSDPreferencesManager.ALL_DPSDS, json);

            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "test", Toast.LENGTH_LONG).show();

                // create Intent to open up DetailActivity
                Intent i = new Intent(context, DetailActivity.class);

                // first way - pass simply a string (the student's name)
                //i.putExtra("just a name", listOfDpsdsToShow.get(position).getName());
                //context.startActivity(i);

                // second way - JSON the whole student object, pass whole object.
                Gson gson = new Gson();
                String selectedStudentJSONed = gson.toJson(listOfDpsdsToShow.get(position));
                i.putExtra("the whole object", selectedStudentJSONed);
                context.startActivity(i);
            }
        });

        return rowView;
    }

    private View getRowViewFilteredList(int position, @Nullable View rowView, @NonNull ViewGroup parent) {

        TextView txtName = rowView.findViewById(R.id.txt_name);
        TextView txtEmail = rowView.findViewById(R.id.txt_email);
        ImageView imgStudentIcon = rowView.findViewById(R.id.img_student);
        ImageButton imgButtonAddRemove = rowView.findViewById(R.id.img_button_add_remove);
        ImageButton imgButtonDissertation = rowView.findViewById(R.id.img_button_dissertation);

        txtName.setText(listOfDpsdsToShow.get(position).getName());
        txtEmail.setText(listOfDpsdsToShow.get(position).getEmail());
        imgStudentIcon.setImageResource(listOfDpsdsToShow.get(position).chooseIcon());

        if (listOfDpsdsToShow.get(position).isAllButDissertation()) {
            imgButtonDissertation.setImageResource(R.drawable.ic_dissertation);
        }
        if (listOfDpsdsToShow.get(position).isShortlisted()) {
            imgButtonAddRemove.setImageResource(R.drawable.ic_shortlisted);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "test", Toast.LENGTH_LONG).show();

                // create Intent to open up DetailActivity
                Intent i = new Intent(context, DetailActivity.class);

                // first way - pass simply a string (the student's name)
                //i.putExtra("just a name", listOfDpsdsToShow.get(position).getName());
                //context.startActivity(i);

                // second way - JSON the whole student object, pass whole object.
                Gson gson = new Gson();
                String selectedStudentJSONed = gson.toJson(listOfDpsdsToShow.get(position));
                i.putExtra("the whole object", selectedStudentJSONed);
                context.startActivity(i);
            }
        });

        return rowView;

    }

    private void initializePreferences() {
        prefManager = DPSDPreferencesManager.instance();

    }

    public void setFilterFlag(boolean showFilteredList) {
        this.showFilteredList = showFilteredList;
    }

    public void setNewData(ArrayList<DPSDStudent> newData) {
        this.listOfDpsdsToShow.clear();
        this.listOfDpsdsToShow.addAll(newData);
        //this.clear();
    }
}
