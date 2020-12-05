package swim.pwr.bikeridinggame;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuStart extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_start);

        BottomNavigationView navigation = findViewById(R.id.mainNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new HomeFragment());
    }


    //TODO: separate to new class
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.homeMenuItem:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.rankingMenuItem: {
                    fragment = new RankingFragment();
                    loadFragment(fragment);
                    return true;
                }
                case R.id.instructionMenuItem: {
                    fragment = new InstructionFragment();
                    loadFragment(fragment);
                    return true;
                }
                case R.id.aboutMenuItem: {
                    fragment = new AboutFragment();
                    loadFragment(fragment);
                    return true;
                }
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}