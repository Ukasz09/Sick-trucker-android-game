package swim.pwr.bikeridinggame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class NickChoseActivity extends Activity {
    private static String nickRegex = "^[^0-9]\\w+$";
    private EditText nickText;
    private TextView incorrectNick;
    Pattern pattern = Pattern.compile(nickRegex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_chose);
        nickText = findViewById(R.id.editTextTextPersonName);
        incorrectNick = findViewById(R.id.incorrectNickError);
        incorrectNick.setVisibility(View.INVISIBLE);
    }

    public void validateNick(View view) {
        String nick = nickText.getText().toString();
        if (!nickIsValid(nick))
            incorrectNick.setVisibility(View.VISIBLE);
        else incorrectNick.setVisibility(View.INVISIBLE);
    }

    private boolean nickIsValid(String nick) {
        return pattern.matcher(nick).matches();
    }
}