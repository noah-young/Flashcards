package edu.bloomu.nmy75228.flashcards;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * The data class manages the reading and writing of the Names and Desc for each card.
 * It is used in the MainActivity class to pull the existing card list and store new cards.
 */
public class Data {
    public static final String NAMES_DAT = "names.dat";
    public static final String DESC_DAT = "desc.dat";

    /**
     * This method writes a list of names and desc to their prospective data files
     * @param nameList The name list to be stored
     * @param descList The desc list to be stored
     * @param context The context of the activity
     */
    public static void writeData (ArrayList<CharSequence> nameList, ArrayList<CharSequence> descList, Context context) {
        try {
            FileOutputStream nameOutput = context.openFileOutput(NAMES_DAT, Context.MODE_PRIVATE);
            ObjectOutputStream nameOutputStream = new ObjectOutputStream(nameOutput);
            nameOutputStream.writeObject(nameList);
            nameOutputStream.close();

            FileOutputStream descOutput = context.openFileOutput(DESC_DAT, Context.MODE_PRIVATE);
            ObjectOutputStream descOutputStream = new ObjectOutputStream(descOutput);
            descOutputStream.writeObject(descList);
            descOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns the list of names in the NAMES_DAT file
     */
    public static ArrayList<CharSequence> readNameData(Context context) {
        ArrayList<CharSequence> namesList = null;
        try {
            FileInputStream inputStream = context.openFileInput(NAMES_DAT);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            namesList = (ArrayList<CharSequence>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            namesList = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e ) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return namesList;
    }

    /**
     * This method returns the list of desc in the DESC_DAT file
     */
    public static ArrayList<CharSequence> readDescData(Context context) {
        ArrayList<CharSequence> descList = null;
        try {
            FileInputStream inputStream = context.openFileInput(DESC_DAT);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            descList = (ArrayList<CharSequence>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            descList = new ArrayList<>();
            e.printStackTrace();
        } catch (IOException e ) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return descList;
    }
}
