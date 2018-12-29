package mapps.com.tourvision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Booking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
    }

    public void next(View v){
        Intent intent=new Intent(this,ScheduleActivity.class);
        startActivity(intent);
        finish();
    }
}
