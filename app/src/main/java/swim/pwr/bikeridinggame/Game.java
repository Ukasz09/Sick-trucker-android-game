package swim.pwr.bikeridinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        System.out.println(UserData.logoPath + "," + UserData.nick);
    }
}