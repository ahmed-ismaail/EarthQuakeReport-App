package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    List<Earthquake> mEarthquakes;

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context,0,earthquakes);

        mEarthquakes = earthquakes;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        TextView MagnitudeView = listItemView.findViewById(R.id.magnitude);
        MagnitudeView.setText(formatMagnitude(currentEarthquake.getMagnitude()));

        GradientDrawable magnitudeCircle = (GradientDrawable) MagnitudeView.getBackground();
        magnitudeCircle.setColor(getMagnitudeColor(Double.valueOf(currentEarthquake.getMagnitude())));

        String[]fullLocation = split(currentEarthquake.getLocation());

        TextView offsetView = listItemView.findViewById(R.id.offset);
        offsetView.setText(fullLocation[0] + " of");

        TextView placeView = listItemView.findViewById(R.id.place);
        placeView.setText(fullLocation[1]);

        Date date = new Date(currentEarthquake.getTime());

        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(formatDate(date));

        TextView timeView = listItemView.findViewById(R.id.time);
        timeView.setText(formatTime(date));

        return listItemView;
    }

    private String formatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h: mm a");
        return  dateFormat.format(date);
    }

    private String formatDate(Date date ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM DD, yyyy");
        return dateFormat.format(date);
    }

    private String [] split(String string) {
        String [] array = {};
        if(string.contains("of")) {
            array = string.split("of");
            return array;
        } else {
            array = new String[]{"Near", string};
            return array;
        }
    }

    private int getMagnitudeColor(double magnitudeValue){

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitudeValue);

        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }

    private String formatMagnitude(double magnitude) {

        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);

    }
}
