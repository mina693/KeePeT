package com.example.b10715.final_pj;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.icu.text.TimeZoneFormat;
import android.icu.util.ULocale;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.widget.Toast.LENGTH_SHORT;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class PetSitterEditActivity extends AppCompatActivity {
    private static final String TAG = "SampleTimesSquareActivi";

    String ADD_PETSITTER = Config.URL + "pet_sitter_add.php";
    String EDIT_PETSITTER = Config.URL + "pet_sitter_edit.php";
    DrawerLayout mDrawerLayout;

    EditText edit_sitter_name, edit_sitter_phone_2, edit_sitter_phone_3, edit_sitter_year, edit_sitter_month, edit_sitter_price, edit_sitter_word;
    Spinner spn_sitter_phone_1, spn_sitter_adr, spn_sitter_adr_2, spn_sitter_pet, spn_sitter_pet_breed, spn_per_price;
    Button btn_choose_image, btn_sitter_age_1, btn_sitter_age_2, btn_sitter_age_3, btn_sitter_age_4, btn_sitter_age_5, btn_sitter_woman, btn_sitter_man, btn_register, btn_edit;
    private CalendarPickerView calendar;

    private static final int PICK_FROM_CAMERA = 1; //카메라 촬영으로 사진 가져오기
    private static final int PICK_FROM_ALBUM = 2; //앨범에서 사진 가져오기
    private static final int CROP_FROM_CAMERA = 3; //가져온 사진을 자르기 위한 변수
    private long lastTimeBackPressed;
    private ImageView sitter_img;
    private File image, cropFile;
    private Uri photoUri;
    private Boolean isEdit = false;
    private String id, user_email, sitter_image, name, age, sex, phone_1, phone_2, phone_3, adr, career_year, career_month, pet, price, word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_sitter_edit);
        TimeZone.getTimeZone("Asia/Seoul");

        user_email = LoginActivity.call_email;
        sitter_img = (ImageView) findViewById(R.id.img_pet_sitter);
        edit_sitter_name = (EditText) findViewById(R.id.edit_sitter_name);
        edit_sitter_phone_2 = (EditText) findViewById(R.id.edit_sitter_phone_2);
        edit_sitter_phone_3 = (EditText) findViewById(R.id.edit_sitter_phone_3);
        edit_sitter_year = (EditText) findViewById(R.id.edit_sitter_year);
        edit_sitter_month = (EditText) findViewById(R.id.edit_sitter_month);
        edit_sitter_price = (EditText) findViewById(R.id.edit_sitter_price);
        edit_sitter_word = (EditText) findViewById(R.id.edit_sitter_word);
        spn_sitter_phone_1 = (Spinner) findViewById(R.id.spn_sitter_phone_1);
        spn_sitter_adr = (Spinner) findViewById(R.id.spn_sitter_adr);
        spn_sitter_pet = (Spinner) findViewById(R.id.spn_sitter_pet);
        spn_sitter_pet_breed = (Spinner) findViewById(R.id.spn_sitter_pet_breed);
        btn_choose_image = (Button) findViewById(R.id.btn_choose_image);
        btn_sitter_age_1 = (Button) findViewById(R.id.btn_sitter_age_1);
        btn_sitter_age_2 = (Button) findViewById(R.id.btn_sitter_age_2);
        btn_sitter_age_3 = (Button) findViewById(R.id.btn_sitter_age_3);
        btn_sitter_age_4 = (Button) findViewById(R.id.btn_sitter_age_4);
        btn_sitter_age_5 = (Button) findViewById(R.id.btn_sitter_age_5);
        btn_sitter_woman = (Button) findViewById(R.id.btn_sitter_woman);
        btn_sitter_man = (Button) findViewById(R.id.btn_sitter_man);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_edit = (Button) findViewById(R.id.btn_edit);

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.init(lastYear.getTime(), nextYear.getTime())
                .inMode(SelectionMode.RANGE)
                .withSelectedDate(new Date());

        if (Config.user_img != null) {
            sitter_img.setImageURI(Uri.parse(Config.user_img));
            sitter_image = Config.user_img.toString();
        }

        Button[] btn_ages = new Button[]{btn_sitter_age_1, btn_sitter_age_2, btn_sitter_age_3, btn_sitter_age_4, btn_sitter_age_5};
        Button[] btn_sexs = new Button[]{btn_sitter_woman, btn_sitter_man};

        Intent showIntent = getIntent();
        isEdit = showIntent.getBooleanExtra("isEdit", isEdit);
        id = showIntent.getStringExtra("id");

        if (isEdit) {
            user_email = showIntent.getStringExtra("user_email");
            sitter_img.setImageURI(Uri.parse(showIntent.getStringExtra("sitter_image")));
            edit_sitter_name.setText(showIntent.getStringExtra("sitter_name"));
            edit_sitter_word.setText(showIntent.getStringExtra("sitter_word"));
            // spn_sitter_phone_1.setAdapter(showIntent.getStringExtra("sitter_price"));
            edit_sitter_phone_2.setText(showIntent.getStringExtra("sitter_phone_2"));
            edit_sitter_phone_3.setText(showIntent.getStringExtra("sitter_phone_3"));
            edit_sitter_year.setText(showIntent.getStringExtra("sitter_career_year"));
            edit_sitter_month.setText(showIntent.getStringExtra("sitter_career_month"));
            edit_sitter_price.setText(showIntent.getStringExtra("sitter_price"));
            btn_register.setVisibility(View.GONE);
            btn_edit.setVisibility(View.VISIBLE);

            String selectedage = showIntent.getStringExtra("sitter_age");
            String selectedsex = showIntent.getStringExtra("sitter_sex");

            for (int i = 0; i < btn_ages.length; i++) {
                if (selectedage.equals(btn_ages[i].getText().toString())) {
                    btn_ages[i].setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                    age=btn_ages[i].getText().toString();
                }
            }
            for (int i = 0; i < btn_sexs.length; i++) {
                if (selectedsex.equals(btn_sexs[i].getText().toString())) {
                    btn_sexs[i].setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                    sex=btn_sexs[i].getText().toString();
                }
            }
        }

        ArrayAdapter phoneAdapter = ArrayAdapter.createFromResource(this, R.array.phone, android.R.layout.simple_spinner_item);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_sitter_phone_1.setAdapter(phoneAdapter);

        ArrayAdapter adrAdapter = ArrayAdapter.createFromResource(this, R.array.adr, android.R.layout.simple_spinner_item);
        adrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_sitter_adr.setAdapter(adrAdapter);

        ArrayAdapter petAdapter = ArrayAdapter.createFromResource(this, R.array.pet_species, android.R.layout.simple_spinner_item);
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_sitter_pet.setAdapter(petAdapter);

        if (spn_sitter_pet.getSelectedItem().toString().equals("고양이")) {
            ArrayAdapter pet_2_Adapter_2 = ArrayAdapter.createFromResource(this, R.array.pet_breed_cat, android.R.layout.simple_spinner_item);
            pet_2_Adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_sitter_pet_breed.setAdapter(pet_2_Adapter_2);

        } else {
            ArrayAdapter pet_2_Adapter = ArrayAdapter.createFromResource(this, R.array.pet_breed_dog, android.R.layout.simple_spinner_item);
            pet_2_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_sitter_pet_breed.setAdapter(pet_2_Adapter);
        }

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Log.d(TAG, "Selected time in millis: " + calendar.getSelectedDate().getTime());
                String toast = "Selected: " + calendar.getSelectedDate().getMonth() + "월" + calendar.getSelectedDate().getDate() + "일";
                Toast.makeText(PetSitterEditActivity.this, toast, LENGTH_SHORT).show();
            }

            @Override
            public void onDateUnselected(Date date) {

            }

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_layout);
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);


    }

    ;


    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_image:

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAlbum();
                    }
                };

                new android.app.AlertDialog.Builder(this)
                        .setTitle("업로드할 이미지 선택")
                        .setNegativeButton("앨범선택", albumListener)
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("취소", cancelListener)
                        .show();
                break;

            case R.id.btn_sitter_age_1:
                age = btn_sitter_age_1.getText().toString();
                btn_sitter_age_5.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_4.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_3.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_2.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_1.setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                break;
            case R.id.btn_sitter_age_2:
                age = btn_sitter_age_2.getText().toString();
                btn_sitter_age_5.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_4.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_3.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_1.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_2.setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                break;
            case R.id.btn_sitter_age_3:
                age = btn_sitter_age_3.getText().toString();
                btn_sitter_age_5.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_4.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_2.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_1.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_3.setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                break;
            case R.id.btn_sitter_age_4:
                age = btn_sitter_age_4.getText().toString();
                btn_sitter_age_5.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_2.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_3.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_1.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_4.setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                break;
            case R.id.btn_sitter_age_5:
                age = btn_sitter_age_5.getText().toString();
                btn_sitter_age_2.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_4.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_3.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_1.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_age_5.setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                break;
            case R.id.btn_sitter_woman:
                sex = btn_sitter_woman.getText().toString();
                btn_sitter_man.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_woman.setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                break;
            case R.id.btn_sitter_man:
                sex = btn_sitter_man.getText().toString();
                btn_sitter_woman.setBackground(getResources().getDrawable(R.drawable.rounded));
                btn_sitter_man.setBackground(getResources().getDrawable(R.drawable.selected_rounded));
                break;
            case R.id.btn_register:
                name = edit_sitter_name.getText().toString();
                phone_1 = spn_sitter_phone_1.getSelectedItem().toString();
                phone_2 = edit_sitter_phone_2.getText().toString();
                phone_3 = edit_sitter_phone_3.getText().toString();
                adr = spn_sitter_adr.getSelectedItem().toString();
                career_year = edit_sitter_year.getText().toString();
                career_month = edit_sitter_month.getText().toString();
                pet = spn_sitter_pet.getSelectedItem().toString() + " (" + spn_sitter_pet_breed.getSelectedItem().toString() + ")";
                price = edit_sitter_price.getText().toString();
                word = edit_sitter_word.getText().toString();
                Log.i("============", career_month + " " + pet + " " + price + " " + word);
                insertToDatabase(user_email, sitter_image, name, age, sex, phone_1, phone_2, phone_3, adr, career_year, career_month, pet, price, word);
                Intent insertIntent = new Intent(PetSitterEditActivity.this, PetSitterActivity.class);
                insertIntent.putExtra("user_email", user_email);
                insertIntent.putExtra("sitter_image", sitter_image);
                insertIntent.putExtra("sitter_name", name);
                insertIntent.putExtra("sitter_age", age);
                insertIntent.putExtra("sitter_sex", sex);
                insertIntent.putExtra("sitter_phone_1", phone_1);
                insertIntent.putExtra("sitter_phone_2", phone_2);
                insertIntent.putExtra("sitter_phone_3", phone_3);
                insertIntent.putExtra("sitter_adr", adr);
                insertIntent.putExtra("sitter_career_year", career_year);
                insertIntent.putExtra("sitter_career_month", career_month);
                insertIntent.putExtra("sitter_pet", pet);
                insertIntent.putExtra("sitter_word", word);
                insertIntent.putExtra("sitter_price", price);
                startActivity(insertIntent);
                break;

            case R.id.btn_edit:

                name = edit_sitter_name.getText().toString();
                phone_1 = spn_sitter_phone_1.getSelectedItem().toString();
                phone_2 = edit_sitter_phone_2.getText().toString();
                phone_3 = edit_sitter_phone_3.getText().toString();
                adr = spn_sitter_adr.getSelectedItem().toString();
                career_year = edit_sitter_year.getText().toString();
                career_month = edit_sitter_month.getText().toString();
                pet = spn_sitter_pet.getSelectedItem().toString() + " ( " + spn_sitter_pet_breed.getSelectedItem().toString() + " )";
                price = edit_sitter_price.getText().toString();
                word = edit_sitter_word.getText().toString();

                editToDatabase(id, sitter_image, name, age, sex, phone_1, phone_2, phone_3, adr, career_year, career_month, pet, price, word);
                Intent editIntent = new Intent(PetSitterEditActivity.this, PetSitterActivity.class);
                editIntent.putExtra("user_email", user_email);
                editIntent.putExtra("sitter_image", sitter_image);
                editIntent.putExtra("sitter_name", name);
                editIntent.putExtra("sitter_age", age);
                editIntent.putExtra("sitter_sex", sex);
                editIntent.putExtra("sitter_phone_1", phone_1);
                editIntent.putExtra("sitter_phone_2", phone_2);
                editIntent.putExtra("sitter_phone_3", phone_3);
                editIntent.putExtra("sitter_adr", adr);
                editIntent.putExtra("sitter_career_year", career_year);
                editIntent.putExtra("sitter_career_month", career_month);
                editIntent.putExtra("sitter_pet", pet);
                editIntent.putExtra("sitter_word", word);
                editIntent.putExtra("sitter_price", price);
                startActivity(editIntent);
                break;
        }
    }

    /**
     * 카메라에서 사진 촬영
     */
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(PetSitterEditActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(PetSitterEditActivity.this,
                    "com.example.b10715.final_pj.provider", photoFile); //FileProvider의 경우 이전 포스트를 참고하세요.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "KeePeT" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/KeePeT/"); //KeePeT이라는 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        galleryRefresh(String.valueOf(image));

        return image;

    }

    // 갤러리 새로고침, ACTION_MEDIA_MOUNTED는 하나의 폴더, FILE은 하나의 파일을 새로 고침할 때 사용함
    private void galleryRefresh(String imgFullPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imgFullPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    public void cropImage() {
        this.grantUriPermission("com.android.camera", photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {

            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
/*            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);*/
            intent.putExtra("scale", true);
            File croppedFileName = null;

            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/KeePeT/");
            cropFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(PetSitterEditActivity.this,
                    "com.example.b10715.final_pj.provider", cropFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            grantUriPermission(res.activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

            startActivityForResult(i, CROP_FROM_CAMERA);


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {

            Toast.makeText(PetSitterEditActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == PICK_FROM_ALBUM) {

            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            cropImage();
            MediaScannerConnection.scanFile(PetSitterEditActivity.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });

        } else if (requestCode == CROP_FROM_CAMERA) {

            try {
                // bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출\.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축

                //여기서는 ImageView에 setImageBitmap을 활용하여 그림을 띄우기.
                sitter_img.setImageBitmap(thumbImage);
                //  cropFile.delete();
                sitter_image = cropFile.toString();

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }

        }
    }

    private void insertToDatabase(String user_email, String sitter_image, String name, String age, String sex, String phone_1, String phone_2, String phone_3, String adr, String career_year, String career_month, String pet, String price, String word) {
        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PetSitterEditActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String user_email = (String) params[0];
                    String sitter_image = (String) params[1];
                    String sitter_name = (String) params[2];
                    String sitter_age = (String) params[3];
                    String sitter_sex = (String) params[4];
                    String sitter_phone_1 = (String) params[5];
                    String sitter_phone_2 = (String) params[6];
                    String sitter_phone_3 = (String) params[7];
                    String sitter_adr = (String) params[8];
                    String sitter_career_year = (String) params[9];
                    String sitter_career_month = (String) params[10];
                    String sitter_pet = (String) params[11];
                    String sitter_price = (String) params[12];
                    String sitter_word = (String) params[13];

                    String data = URLEncoder.encode("user_email", "UTF-8") + "=" + URLEncoder.encode(user_email, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_image", "UTF-8") + "=" + URLEncoder.encode(sitter_image, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_name", "UTF-8") + "=" + URLEncoder.encode(sitter_name, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_age", "UTF-8") + "=" + URLEncoder.encode(sitter_age, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_sex", "UTF-8") + "=" + URLEncoder.encode(sitter_sex, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_phone_1", "UTF-8") + "=" + URLEncoder.encode(sitter_phone_1, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_phone_2", "UTF-8") + "=" + URLEncoder.encode(sitter_phone_2, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_phone_3", "UTF-8") + "=" + URLEncoder.encode(sitter_phone_3, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_adr", "UTF-8") + "=" + URLEncoder.encode(sitter_adr, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_career_year", "UTF-8") + "=" + URLEncoder.encode(sitter_career_year, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_career_month", "UTF-8") + "=" + URLEncoder.encode(sitter_career_month, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_pet", "UTF-8") + "=" + URLEncoder.encode(sitter_pet, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_price", "UTF-8") + "=" + URLEncoder.encode(sitter_price, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_word", "UTF-8") + "=" + URLEncoder.encode(sitter_word, "UTF-8");

                    URL url = new URL(ADD_PETSITTER);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        InsertData task = new InsertData();
        task.execute(user_email, sitter_image, name, age, sex, phone_1, phone_2, phone_3, adr, career_year, career_month, pet, price, word);
    }

    private void editToDatabase(String id, String sitter_image, String name, String age, String sex, String phone_1, String phone_2, String phone_3, String adr, String career_year, String career_month, String pet, String price, String word) {
        class EditData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PetSitterEditActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("실패", "onPostExecute");

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                Log.i("editToDatabase", "doInBackground");
                try {
                    String id = (String) params[0];
                    String sitter_image = (String) params[1];
                    String sitter_name = (String) params[2];
                    String sitter_age = (String) params[3];
                    String sitter_sex = (String) params[4];
                    String sitter_phone_1 = (String) params[5];
                    String sitter_phone_2 = (String) params[6];
                    String sitter_phone_3 = (String) params[7];
                    String sitter_adr = (String) params[8];
                    String sitter_career_year = (String) params[9];
                    String sitter_career_month = (String) params[10];
                    String sitter_pet = (String) params[11];
                    String sitter_price = (String) params[12];
                    String sitter_word = (String) params[13];

                    String link = EDIT_PETSITTER;

                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_image", "UTF-8") + "=" + URLEncoder.encode(sitter_image, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_name", "UTF-8") + "=" + URLEncoder.encode(sitter_name, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_age", "UTF-8") + "=" + URLEncoder.encode(sitter_age, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_sex", "UTF-8") + "=" + URLEncoder.encode(sitter_sex, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_phone_1", "UTF-8") + "=" + URLEncoder.encode(sitter_phone_1, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_phone_2", "UTF-8") + "=" + URLEncoder.encode(sitter_phone_2, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_phone_3", "UTF-8") + "=" + URLEncoder.encode(sitter_phone_3, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_adr", "UTF-8") + "=" + URLEncoder.encode(sitter_adr, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_career_year", "UTF-8") + "=" + URLEncoder.encode(sitter_career_year, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_career_month", "UTF-8") + "=" + URLEncoder.encode(sitter_career_month, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_pet", "UTF-8") + "=" + URLEncoder.encode(sitter_pet, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_price", "UTF-8") + "=" + URLEncoder.encode(sitter_price, "UTF-8");
                    data += "&" + URLEncoder.encode("sitter_word", "UTF-8") + "=" + URLEncoder.encode(sitter_word, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        EditData task = new EditData();
        task.execute(id, sitter_image, name, age, sex, phone_1, phone_2, phone_3, adr, career_year, career_month, pet, price, word);
    }


}


