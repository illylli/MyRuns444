package com.example.yuzhong.myruns3;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends Activity {

    private ImageView imageView;
    private EditText yourName;
    private EditText yourEmail;
    private EditText yourPhoneNumber;
    private RadioGroup yourGender;
    private EditText yourClass;
    private EditText yourMajor;
    private Uri mImageCaptureUri;
    private boolean isTakenFromCamera;
    private String filePath;

    private static final String TAG = "CS65";
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
    public static final int REQUEST_CODE_PICK_FROM_SD_CARD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        imageView = (ImageView) findViewById(R.id.profile_photo);
        yourName = (EditText) findViewById(R.id.your_name);
        yourEmail = (EditText) findViewById(R.id.your_email);
        yourPhoneNumber = (EditText) findViewById(R.id.your_phone_number);
        yourGender = (RadioGroup) findViewById(R.id.gender);
        yourClass = (EditText) findViewById(R.id.class_year);
        yourMajor = (EditText) findViewById(R.id.your_major);

        if(savedInstanceState != null){
            mImageCaptureUri = savedInstanceState.getParcelable(URI_INSTANCE_STATE_KEY);
        }
        loadSnap();
        loadProfile();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
    }

    public void onChangePhotoClicked(View view){
        // changing the profile image, show the dialog asking the user
        // to choose between taking a picture
        // Go to PhotoPickerDialogFragment for details.
        displayDialog(PhotoPickerDialogFragment.DIALOG_ID_PHOTO_PICKER);
    }

    // ******* Photo picker dialog related functions ************//

    public void displayDialog(int id) {
        DialogFragment fragment = PhotoPickerDialogFragment.newInstance(id);
        fragment.show(getFragmentManager(),
                getString(R.string.dialog_fragment_tag_photo_picker));
    }

    public void onPhotoPickerItemSelected(int item) {
        Intent intent;

        switch (item) {
            case PhotoPickerDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
                // Take photo from camera，
                // Construct an intent with action
                // MediaStore.ACTION_IMAGE_CAPTURE
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Construct temporary image path and name to save the taken
                // photo
                mImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "tmp_"
                        + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri);
                intent.putExtra("return-data", true);
                try {
                    // Start a camera capturing activity
                    // REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
                    // defined to identify the activity in onActivityResult()
                    // when it returns
                    startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                isTakenFromCamera = true;
                break;

            case PhotoPickerDialogFragment.ID_PHOTO_PICKER_FROM_SD_CARD:
                // Take photo from camera，
                // Construct an intent with action
                // MediaStore.ACTION_IMAGE_CAPTURE
                intent = new Intent(Intent.ACTION_PICK);
                // Construct temporary image path and name to save the taken
                // photo
//                mImageCaptureUri = Uri.fromFile(new File(Environment
//                        .getExternalStorageDirectory(), "tmp_"
//                        + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        mImageCaptureUri);
//                intent.putExtra("return-data", true);

//                intent.setType("image/*");
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                try {
                    // Start a camera capturing activity
                    // REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
                    // defined to identify the activity in onActivityResult()
                    // when it returns
                    startActivityForResult(intent,  REQUEST_CODE_PICK_FROM_SD_CARD);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                isTakenFromCamera = false;
                break;
            default:
                return;
        }
    }

    // Handle data after activity returns.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_TAKE_FROM_CAMERA:
                // Send image taken from camera for cropping
                beginCrop(mImageCaptureUri);
                break;

            case REQUEST_CODE_PICK_FROM_SD_CARD:
                // Send image taken from camera for cropping
                beginCrop(data.getData());
                break;

            case Crop.REQUEST_CROP: //We changed the RequestCode to the one being used by the library.
                // Update image view after image crop
                handleCrop(resultCode, data);

                // Delete temporary image taken by camera after crop.
                if (isTakenFromCamera) {
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists())
                        f.delete();
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void loadSnap() {
        // Load profile photo from internal storage
        try {
            FileInputStream fis = openFileInput(getString(R.string.photo_name));
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bmap);
            fis.close();
        } catch (IOException e) {
            // Default profile photo if no photo saved before.
            imageView.setImageResource(R.drawable.images);
        }
    }

    private void saveSnap() {
        // Commit all the changes into preference file
        // Save profile image into internal storage.
        imageView.buildDrawingCache();
        Bitmap bmap = imageView.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(
                    getString(R.string.photo_name), MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void loadProfile(){
        Log.d(TAG, "loadProfile");

        String key = getString(R.string.savefile);
        SharedPreferences mPrefs = getSharedPreferences(key, Context.MODE_PRIVATE);

        key = getString(R.string.your_name);
        String value = mPrefs.getString(key, "");
        yourName.setText(value);

        key = getString(R.string.your_email);
        value = mPrefs.getString(key, "");
        yourEmail.setText(value);

        key = getString(R.string.your_phone_number);
        value = mPrefs.getString(key, "");
        yourPhoneNumber.setText(value);

        key = getString(R.string.gender);
        int keyInt = mPrefs.getInt(key, -1);
        if(keyInt > -1){
            ((RadioButton) yourGender.getChildAt(keyInt)).setChecked(true);
        }

        key = getString(R.string.class_year);
        value = mPrefs.getString(key, "");
        yourClass.setText(value);

        key = getString(R.string.your_major);
        value = mPrefs.getString(key, "");
        yourMajor.setText(value);
    }

    public void onSaveClicked(View view){
        String key = getString(R.string.savefile);
        SharedPreferences mPrefs = getSharedPreferences(key, Context.MODE_PRIVATE);

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.clear();

        key = getString(R.string.your_name);
        mEditor.putString(key, yourName.getText().toString());

        key = getString(R.string.your_email);
        mEditor.putString(key, yourEmail.getText().toString());

        key = getString(R.string.your_phone_number);
        mEditor.putString(key, yourPhoneNumber.getText().toString());

        key = getString(R.string.gender);
        int keyInt = yourGender.indexOfChild((findViewById(yourGender.getCheckedRadioButtonId())));
        mEditor.putInt(key, keyInt);

        key = getString(R.string.class_year);
        mEditor.putString(key, yourClass.getText().toString());

        key = getString(R.string.your_major);
        mEditor.putString(key, yourMajor.getText().toString());

        mEditor.commit();

        saveSnap();

        Toast.makeText(getApplicationContext(), getString(R.string.save_message), Toast.LENGTH_SHORT).show();

        finish();
    }

    public void onCancelClicked(View v) {
        finish();
    }

    /** Method to start Crop activity using the library
     *	Earlier the code used to start a new intent to crop the image,
     *	but here the library is handling the creation of an Intent, so you don't
     * have to.
     *  **/
    private void beginCrop(Uri source) {
        filePath = getCacheDir() + "cropped" + System.currentTimeMillis();
        File file = new File(filePath);


        Uri destination = Uri.fromFile(file);

//        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            imageView.setImageURI(Crop.getOutput(result));
            File file = new File(filePath);
            if(file.exists()) {
                Log.d(TAG, "Delete: " + file.delete() +"again" + file.exists());
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
