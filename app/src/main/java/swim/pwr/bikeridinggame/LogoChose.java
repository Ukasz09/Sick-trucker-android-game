package swim.pwr.bikeridinggame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LogoChose extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_chose);
    }

    void onLogoClick1(View view) {
        this.onLogoClick("user_logo_1");
    }

    void onLogoClick2(View view) {
        this.onLogoClick("user_logo_2");
    }

    void onLogoClick3(View view) {
        this.onLogoClick("user_logo_3");
    }

    void onLogoClick4(View view) {
        this.onLogoClick("user_logo_4");
    }

    void onLogoClick5(View view) {
        this.onLogoClick("user_logo_5");
    }

    void onLogoClick6(View view) {
        this.onLogoClick("user_logo_6");
    }

    void onLogoClick7(View view) {
        this.onLogoClick("user_logo_7");
    }

    void onLogoClick8(View view) {
        this.onLogoClick("user_logo_1");
    }

    void onLogoClick9(View view) {
        this.onLogoClick("user_logo_1");
    }

    private void onLogoClick(String logoPath) {
        UserData.logoPath = logoPath;
        final Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }
}