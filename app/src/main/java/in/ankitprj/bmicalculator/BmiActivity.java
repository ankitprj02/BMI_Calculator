package in.ankitprj.bmicalculator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class BmiActivity extends AppCompatActivity {

    android.widget.Button mrecalculatebmi;
    TextView mbmidisplay, mbmicategory, mgender;
    Intent intent;
    ImageView imageView,share;
    String mbmi;
    float intbmi;
    String height;
    String weight;
    float intheight, intweight;
    RelativeLayout mbackground;
    LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\"></font>"));
        getSupportActionBar().setTitle("Result");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E1D1D"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);


        mrecalculatebmi = findViewById(R.id.recalculatebmi);
        intent = getIntent();
        mbmidisplay = findViewById(R.id.bmidisplay);
        mbmicategory = findViewById(R.id.bmicategory);
        mgender = findViewById(R.id.genderdisplay);
        mbackground = findViewById(R.id.contentlayout);
        imageView = findViewById(R.id.imageview);
        linearLayout = findViewById(R.id.canvas);
        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");
        intheight = Float.parseFloat(height);
        intweight = Float.parseFloat(weight);
        intheight = intheight / 100;
        intbmi = intweight / (intheight * intheight);
        mbmi = Float.toString(intbmi);
        share = findViewById(R.id.share);


        if (intbmi < 16) {
            mbmicategory.setText("Severe Thinness");
            mbackground.setBackgroundColor(Color.RED);
            imageView.setImageResource(R.drawable.crosss);
        } else if (intbmi < 16.9 && intbmi > 16) {
            mbmicategory.setText("Moderate Thinness");
            mbackground.setBackgroundColor(Color.RED);
            imageView.setImageResource(R.drawable.warning);
        } else if (intbmi < 18.4 && intbmi > 17) {
            mbmicategory.setText("Mild Thinness");
            mbackground.setBackgroundColor(Color.RED);
            imageView.setImageResource(R.drawable.crosss);
        } else if (intbmi < 25 && intbmi > 18.4) {
            mbmicategory.setText("Normal");
            //mbackground.setBackground(Color.YELLOW);
            imageView.setImageResource(R.drawable.ok);
        } else if (intbmi < 29.4 && intbmi > 25) {
            mbmicategory.setText("Over Weight");
            mbackground.setBackgroundColor(Color.RED);
            imageView.setImageResource(R.drawable.warning);
        } else {
            mbmicategory.setText("Obese Class I");
            mbackground.setBackgroundColor(Color.RED);
            imageView.setImageResource(R.drawable.warning);

        }

        mgender.setText(intent.getStringExtra("gender"));
        mbmidisplay.setText(mbmi);


        mrecalculatebmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BmiActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });





        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareLinearLayout();
            }
        });



    }

    private Bitmap captureLinearLayoutAsBitmap(LinearLayout linearLayout) {
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(linearLayout.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());
        linearLayout.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getDrawingCache());
        linearLayout.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void shareLinearLayout() {
        Bitmap bitmap = captureLinearLayoutAsBitmap(linearLayout);
        Uri imageUri = saveBitmapToTemporaryFile(bitmap);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        String playStoreLink = "https://play.google.com/store/apps/details?id=in.ankitprj.bmicalculator";
        shareIntent.putExtra(Intent.EXTRA_TEXT,"*This is my BMI Report*\n" +
                        "Download This App to check your BMI...\n" + playStoreLink);


        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }


    private Uri saveBitmapToTemporaryFile(Bitmap bitmap) {
        try {
            File file = new File(getExternalCacheDir(), "temp_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Get the content URI using FileProvider
            Context context = getApplicationContext();
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }





}