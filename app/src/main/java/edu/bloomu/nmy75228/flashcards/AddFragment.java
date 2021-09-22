package edu.bloomu.nmy75228.flashcards;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/**
 * The Add Fragment manages the creation of new flash cards and send the data to the Main Activity
 */
public class AddFragment extends Fragment {

    private EditText cardName;
    private EditText cardDesc;
    private Button createButton;
    private addFragmentListener listener;

    /**
     * Interface to communicate new cards to the parent activity
     */
    public interface addFragmentListener {
        void newCardInputSent(CharSequence n, CharSequence d);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        cardName = view.findViewById(R.id.enter_name);
        cardDesc = view.findViewById(R.id.enter_desc);
        createButton = view.findViewById(R.id.create_button);

        // Sets the click listener for the create button
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence newCardName = cardName.getText();
                CharSequence newCardDesc = cardDesc.getText();
                if(newCardName.toString().trim().equals("") || newCardDesc.toString().trim().equals("")) {
                    // Alert dialog to prompt the user to enter non-blank characters
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Oops");
                    alertDialog.setMessage("Please enter at least one" +
                            " non-blank character for both the Card name and Card description");
                    alertDialog.show();
                } else {
                    listener.newCardInputSent(newCardName, newCardDesc);
                }
            }
        });

        return view;
    }

    /**
     * onAttach and onDetach methods are required in order to send the User inputted data
     * back to the main activity. They are responsible for attaching and detaching the
     * addFragmentListener interface to it's parent activity for communication.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof addFragmentListener) {
            listener = (addFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement addFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
