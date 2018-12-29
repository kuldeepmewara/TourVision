package mapps.com.tourvision;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Details extends FragmentActivity {
    SharedPreferences sp;

    static TextView startDateView,endDateView,destView;

    static boolean start=true;
    static String startDate,endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        sp=getSharedPreferences("details",MODE_PRIVATE);

        startDateView=findViewById(R.id.start_date);
        endDateView=findViewById(R.id.end_date);
        destView=findViewById(R.id.to);
    }

    public void setStartDate(View v){
        start=true;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setEndDate(View v){
        start=false;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String date=String.valueOf(day)+"-"+String.valueOf(month)+"-"+String.valueOf(year);
            if(start){
                startDate=date;
                startDateView.setText(date);
            }else{
                endDate=date;
                endDateView.setText(date);
            }
        }
    }

    public void submit(View v){
        if(startDate==null || endDate==null){
            Toast.makeText(getBaseContext(),"Start date and end date are mandatory.",Toast.LENGTH_SHORT).show();
            return;
        }

        String dest=destView.getText().toString().toLowerCase();

        ArrayList<String> dests=new ArrayList<>();
        dests.add("jodhpur");
        dests.add("jaipur");
        dests.add("udaipur");

        if(!dests.contains(dest)){
            Toast.makeText(getBaseContext(),"Invalid destination.",Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor=sp.edit();
        editor.putString("start_date",startDate);
        editor.putString("end_date",endDate);
        editor.putString("dest",dest);
        editor.commit();

        Intent intent=new Intent(this,Booking.class);
        startActivity(intent);
        finish();
    }


}