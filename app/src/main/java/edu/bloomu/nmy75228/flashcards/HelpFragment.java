package edu.bloomu.nmy75228.flashcards;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * The Help Fragment creates a static FAQ list.
 */
public class HelpFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView faqList;
    private ArrayList<String> front = new ArrayList<>();
    private ArrayList<String> back = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_fragment, container, false);
        faqList = view.findViewById(R.id.faq_list);

        front.add("What is Flashcards about?");
        front.add("How do I create a new set?");
        front.add("How do I Delete a set?");
        front.add("How do I view the answer for a flash card?");
        front.add("Is Flashcards free to use?");
        front.add("Who created Flashcards?");

        back.add("Flashcards is an easy to use app that lets you create and study flash" +
                " cards for any and all of your classes!");
        back.add("Create a new card by going to the + tab on the bottom navigation bar. " +
                "Then enter a card name and description and hit the 'CREATE' button");
        back.add("To delete a card, simply tap the trash can icon in the bottom right corner of" +
                " the screen. Then select the card(s) you would like to delete.");
        back.add("To view an answer, simply tap the flash card.");
        back.add("Yes! Flashcards is completely free to use.");
        back.add("Flashcards was created by Noah Young.");

        adapter = new ArrayAdapter<>(getContext(), R.layout.custom_text_view, front);
        faqList.setAdapter(adapter);
        faqList.setOnItemClickListener(this);

        return view;
    }

    /**
     * Detects clicks made by the user on the faq cards.
     *
     * @param adapterView The adapterView of the click
     * @param view Takes in the view of the clicked faq card
     * @param i The position of the selected view
     * @param id The id of the item clicked
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long id) {
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
                if(((TextView) view).getText().toString().equals(front.get(i))) {
                    ((TextView) view).setText(back.get(i));
                } else {
                    ((TextView) view).setText(front.get(i));
                }
                animatorTwo.start();
            }
        });
        animatorOne.start();
    }
}
