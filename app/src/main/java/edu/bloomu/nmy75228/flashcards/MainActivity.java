package edu.bloomu.nmy75228.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * MainActivity class is used for the management of the BottomNavigationBar and the
 * three fragments that coincide with it. It also displays the list of User created
 * flash cards. It also contains a floating action button that handles.
 *
 * A flash card typically contains a question on the front of the card and an answer
 * on the reverse side. Throughout the application the question portion of the card is
 * denoted as the card's "name", while the answer portion of the card is denoted as the
 * card's "description" abbreviated to "desc".
 */
public class MainActivity extends AppCompatActivity implements AddFragment.addFragmentListener, AdapterView.OnItemClickListener {

    private ListView cardList;
    private ArrayList<CharSequence> names;
    private ArrayList<CharSequence> desc;
    private ArrayAdapter<CharSequence> nameAdapter;
    private ArrayAdapter<CharSequence> descAdapter;
    private boolean deleteMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteMode = false;

        cardList = findViewById(R.id.card_list);

        names = Data.readNameData(this);
        desc = Data.readDescData(this);

        nameAdapter = new ArrayAdapter<>(this, R.layout.custom_text_view, names);
        descAdapter = new ArrayAdapter<>(this, R.layout.custom_text_view, desc);
        cardList.setAdapter(nameAdapter);

        // Sets up the onClick listener for the Floating Action Button.
        // When clicked, the button turns yellow denoting that the user
        // is able to delete flash cards
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteMode) {
                    deleteMode = false;
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                } else {
                    deleteMode = true;
                    fab.setBackgroundTintList(getResources().getColorStateList(R.color.delete_yellow));
                }
            }
        });

        // Initializes the bottom navigation view and assigns the ItemSelected Listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);

        // Sets the initial fragment to the home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        cardList.setOnItemClickListener(this);
    }

    /**
     * This method created the listener for the Bottom Navigation View.
     * It's function is to detect which item in the Navigation has been selected by
     * the user and then to switch to that selected fragment.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment currentFragment = new HomeFragment();

            if (item.getItemId() == R.id.navigation_home) {
                currentFragment = new HomeFragment();
                cardList.setVisibility(View.VISIBLE);
            } else if (item.getItemId() == R.id.navigation_add) {
                currentFragment = new AddFragment();
                cardList.setVisibility(View.GONE);
            } else if (item.getItemId() == R.id.navigation_help) {
                currentFragment = new HelpFragment();
                cardList.setVisibility(View.GONE);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, currentFragment).commit();
            return true;
        }
    };

    /**
     * This method sends the user back to the home screen after the creation of a new flash card.
     */
    public void newCardCreated() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).getMenu().findItem(R.id.navigation_home).setChecked(true);
        cardList.setVisibility(View.VISIBLE);
    }

    /**
     * This method is communication between the activity and the interface of AddFragment.
     * @param n The name or "question" of the flash card.
     * @param d The description or "answer" of the flash card.
     */
    @Override
    public void newCardInputSent(CharSequence n, CharSequence d) {
        nameAdapter.add(n.toString());
        descAdapter.add(d.toString());
        Data.writeData(names, desc, this);
        newCardCreated();
    }

    /**
     * Detects clicks made by the user on the flash cards.
     *
     * @param adapterView The adapterView of the click
     * @param view Takes in the view of the clicked flash card
     * @param i The position of the selected view
     * @param id The id of the item clicked
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long id) {
        if (!deleteMode) {
            /**
             * Animation code derived from a StackOverflow card flip example by Cahid Enes Keleş
             * Link: https://stackoverflow.com/questions/46111262/card-flip-animation-in-android
             */
            final ObjectAnimator animatorOne = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
            final ObjectAnimator animatorTwo = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
            animatorOne.setInterpolator(new DecelerateInterpolator());
            animatorTwo.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorOne.setDuration(125);
            animatorTwo.setDuration(125);
            animatorOne.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    // If the text is equal to the name on click, set the new text to the desc.
                    // Otherwise if the text is equal to the desc, set the new text to the name.
                    if(((TextView) view).getText().toString().contentEquals(names.get(i))) {
                        ((TextView) view).setText(desc.get(i));
                    } else {
                        ((TextView) view).setText(names.get(i));
                    }
                    animatorTwo.start();
                }
            });
            animatorOne.start();
        } else {
            // If in delete mode, the selected card's name and desc is removed from their
            // prospective lists and their adapters are updates. The new lists are then written
            // to Data.
            names.remove(i);
            desc.remove(i);
            nameAdapter.notifyDataSetChanged();
            descAdapter.notifyDataSetChanged();
            Data.writeData(names, desc, MainActivity.this);
        }
    }
}
