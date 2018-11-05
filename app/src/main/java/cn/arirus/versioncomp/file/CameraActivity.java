package cn.arirus.versioncomp.file;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import cn.arirus.versioncomp.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static cn.arirus.versioncomp.MainActivity.ARIRUS;

public class CameraActivity extends AppCompatActivity {

  public static final int TAKE_PHOTO = 996;
  public static final int TAKE_CROP = 997;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    RxPermissions permission = new RxPermissions(this);

    Log.i(ARIRUS, "onCreate: "+getPackageName()+" "+getLocalClassName());

    mImageView = findViewById(R.id.img);

    findViewById(R.id.btn_share).setOnClickListener(v -> {
      outputFile = new File(getExternalFilesDir(null), "capture1.png");
      if (!outputFile.getParentFile().exists()) outputFile.getParentFile().mkdir();
      try {
        if (outputFile.exists()) outputFile.delete();
          Log.i(ARIRUS, "onCreate: exists");
          outputFile.createNewFile();

        FileWriter fileWriter = new FileWriter(outputFile,true);
        fileWriter.append("A App gen the file");
        fileWriter.flush();
        fileWriter.close();

        if (Objects.equals(getIntent().getAction(), "cn.arirus.SHARE")) {
          Intent intent = new Intent();
          Log.i(ARIRUS, "onCreate: "+FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", outputFile));
          intent.setData(
              FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", outputFile));
          intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|FLAG_GRANT_READ_URI_PERMISSION);
          setResult(RESULT_OK, intent);
          finish();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    findViewById(R.id.cam).setOnClickListener(
        v -> permission.request(Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> takeCam()));
  }

  ImageView mImageView;

  File outputFile;
  String mFilePath;
  Uri mUri;

  private void takeCam() throws ActivityNotFoundException {
    outputFile = new File(getExternalFilesDir(null), "capture.png");
    if (!outputFile.getParentFile().exists()) outputFile.getParentFile().mkdir();
    //if (outputFile.exists()) outputFile.delete();

    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    mFilePath = outputFile.getAbsolutePath();
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      mUri = Uri.fromFile(outputFile);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
    } else {
      mUri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", outputFile);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
      intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }

    startActivityForResult(intent, TAKE_PHOTO);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mFilePath != null) {
      outState.putString("cameraImagePath", mFilePath);
    }

    if (mUri != null) {
      outState.putString("cameraImageUri", mUri.toString());
    }
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (savedInstanceState.containsKey("cameraImagePath")) {
      mFilePath = (savedInstanceState.getString("cameraImagePath"));
      mImageView.setImageBitmap(getSmallBitmap(mFilePath));
    }
    if (savedInstanceState.containsKey("cameraImageUri")){
      mUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

      Intent intent = new Intent("com.android.camera.action.CROP");
      intent.setDataAndType(mUri,"image/png");
      intent.putExtra("crop", "true");
      intent.putExtra("aspectX", 1);
      intent.putExtra("aspectY", 1);
      //intent.putExtra("scale", true);
      //intent.putExtra("outputX", 128);
      //intent.putExtra("outputY", 128);
      //intent.putExtra("return-data", true);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

      startActivityForResult(intent,TAKE_CROP);
    }
    else if (requestCode == TAKE_CROP && resultCode == Activity.RESULT_OK){
      mImageView.setImageBitmap(getSmallBitmap(mFilePath));

    }
    //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
    //mImageView.setImageBitmap(bitmap);

    //File file = new File(getExternalFilesDir(null), "capture_small.png");
    //try {
    //  FileOutputStream out = new FileOutputStream(file);
    //  assert bitmap != null;
    //  bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
    //
    //  out.flush();
    //  out.close();
    //} catch (IOException e) {
    //  e.printStackTrace();
    //}
  }

  public static Bitmap getSmallBitmap(String filePath) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(filePath, options);
    // Calculate inSampleSize
    options.inSampleSize = 2;

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;

    return BitmapFactory.decodeFile(filePath, options);
  }
}
