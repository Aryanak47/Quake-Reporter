package com.aryanapps.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustompageAdapter extends ArrayAdapter<Earthquakereport> {

private Context context;

    public CustompageAdapter(@NonNull Context context, @NonNull List<Earthquakereport> objects) {
        super(context, 0, objects);
        this.context=context;

                                                                                                 }

    static class viewHolder {
        TextView magtextView;
        TextView placetextView;
        TextView datetextView;
        TextView timetextView;
        TextView offsetLocation;
                            }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.activity_custompage_adapter,parent,false);
            viewHolder holder=new viewHolder();
            holder.magtextView=convertView.findViewById(R.id.mag);
            holder.placetextView=convertView.findViewById(R.id.primary_location);
            holder.datetextView=convertView.findViewById(R.id.date);
            holder.timetextView=convertView.findViewById(R.id.time);
            holder.offsetLocation=convertView.findViewById(R.id.location_offset);
            convertView.setTag(holder);

                            }
        Earthquakereport earthquakereport=getItem(position);
        viewHolder holder=(viewHolder) convertView.getTag();

       double mag=earthquakereport.getMag();
        holder.magtextView.setText(formatMagnitude(mag));
         //setting the color according to magnitude
        GradientDrawable magnitudeCircle = (GradientDrawable) holder.magtextView.getBackground();
         int color=getMagnitudeColor(mag);
         magnitudeCircle.setColor(color);

        String place=earthquakereport.getPlace();
          int index=place.indexOf("of");
          String offSetLocation;
          String primaryLOcation;

          if(index!=-1){
              offSetLocation=place.substring(0,index+2);
              primaryLOcation=place.substring(index+2);
              holder.placetextView.setText(primaryLOcation);
              Log.d("Adapter",primaryLOcation);
              holder.offsetLocation.setText(offSetLocation);
              Log.d("Adapter",offSetLocation);

                     }
          else {
              String addNear="Near the ";
              String concatenate=addNear.concat(place);
              holder.placetextView.setText( place);
              holder.offsetLocation.setText(concatenate);

             }


        Date date1=new Date(earthquakereport.getDate());
        String formateddate=formatDate(date1);
        holder.datetextView.setText(formateddate);

        String formatedTime=formatTime(date1);
        holder.timetextView.setText(formatedTime);

        return convertView;
    }
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
                                               }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
                                               }

     private String formatMagnitude(double magnitude){
         DecimalFormat format=new DecimalFormat("0.0");
         return format.format(magnitude);


     }

private int getMagnitudeColor(double mag){
        int magnitudeResourceid;
        int magnitudeFloor=(int) Math.floor(mag);

           switch (magnitudeFloor){
               case 0:
               case 1:
                   magnitudeResourceid=R.color.magnitude1;
               break;

               case 2:
                   magnitudeResourceid = R.color.magnitude2;
                   break;
               case 3:
                   magnitudeResourceid = R.color.magnitude3;
                   break;
               case 4:
                   magnitudeResourceid = R.color.magnitude4;
                   break;
               case 5:
                   magnitudeResourceid = R.color.magnitude5;
                   break;
               case 6:
                   magnitudeResourceid = R.color.magnitude6;
                   break;
               case 7:
                   magnitudeResourceid = R.color.magnitude7;
                   break;
               case 8:
                   magnitudeResourceid = R.color.magnitude8;
                   break;
               case 9:
                   magnitudeResourceid = R.color.magnitude9;
                   break;
               default:
                   magnitudeResourceid = R.color.magnitude10plus;
                   break;


    }
                return ContextCompat.getColor(getContext(),magnitudeResourceid);




}




}

