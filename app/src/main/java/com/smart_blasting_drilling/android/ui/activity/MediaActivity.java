package com.smart_blasting_drilling.android.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.databinding.ActivityMediaBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.ui.adapter.MediaAdapter;
import com.smart_blasting_drilling.android.utils.BitmapUtils;
import com.smart_blasting_drilling.android.utils.DateUtils;
import com.smart_blasting_drilling.android.utils.DonwloadFileManagerUtils;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.viewmodel.MediaViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MediaActivity extends BaseActivity implements PickiTCallbacks {

    private final int PICK_IMAGE_CAMERA = 1;
    private final int PICK_VIDEO_CAMERA = 2;
    ActivityMediaBinding binding;
    MediaAdapter mediaAdapter;
    File file = null;
    String imgPath;
    PickiT pickiT;
    ActivityResultLauncher<String> activityResultLauncher;
    String filename;
    Bitmap bitmap;
    String extension;
    ResponseBladesRetrieveData bladesRetrieveData;
    private Bitmap mResultsBitmap;

    MediaViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media);

        viewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        StatusBarUtils.statusBarColor(this, R.color._FFA722);
        binding.headerMedia.mediaTitle.setText(getString(R.string.media));

        if (isBundleIntentNotEmpty()) {
            bladesRetrieveData = (ResponseBladesRetrieveData) getIntent().getExtras().getSerializable("blades_data");
        }

        binding.headerMedia.backImg.setOnClickListener(view -> finish());

        getAllImageFromGallery();

        binding.clickhereTv.setOnClickListener(view -> selectImage());
        pickiT = new PickiT(this, this, this);

        imageListBlank();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                pickiT.getPath(uri, Build.VERSION.SDK_INT);
            }
        });

        binding.addCameraFab.setOnClickListener(view -> selectImage());

    }

    private void imageListBlank() {
        if (!Constants.isListEmpty(AppDelegate.getInstance().getImgList())) {
            mediaAdapter = new MediaAdapter(this, AppDelegate.getInstance().getImgList());
            binding.mediaRecycler.setAdapter(mediaAdapter);
        }
        if (Constants.isListEmpty(AppDelegate.getInstance().getImgList())) {
            binding.noimageTV.setVisibility(View.VISIBLE);
            binding.clickhereTv.setVisibility(View.VISIBLE);
            binding.mediaRecycler.setVisibility(View.GONE);
            binding.addCameraFab.setVisibility(View.GONE);
        } else {
            binding.noimageTV.setVisibility(View.GONE);
            binding.clickhereTv.setVisibility(View.GONE);
            binding.mediaRecycler.setVisibility(View.VISIBLE);
            binding.addCameraFab.setVisibility(View.VISIBLE);
        }
    }

    private void getAllImageFromGallery() {
        try {
            List<String> fileList = new ArrayList<>();
            String[] projection = new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, "");
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                fileList.add(cursor.getString(dataColumnIndex));
            }

            Cursor videoCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, "");
            for (int i = 0; i < videoCursor.getCount(); i++) {
                videoCursor.moveToPosition(i);
                int dataColumnIndex = videoCursor.getColumnIndex(MediaStore.Video.Media.DATA);
                fileList.add(videoCursor.getString(dataColumnIndex));
            }

            Log.e("Image List :- ", new Gson().toJson(fileList));
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void selectImage() {
        final CharSequence[] options = {getString(R.string.take_photo),getString(R.string.take_video),getString(R.string.choose_from), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_option));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.take_photo))) {
                if (checkPermission()) {
                    openCamera(1);
                } else {
                    requestCameraPermission(PICK_IMAGE_CAMERA);
                }
                dialog.dismiss();
            }
            else if (options[item].equals(getString(R.string.take_video))) {
                if (checkPermission()) {
                    openVideo(2);
                } else {
                    requestCameraPermission(PICK_VIDEO_CAMERA);
                }
                dialog.dismiss();
            }
            else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                dialog.dismiss();
                String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(this, permission, null, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        activityResultLauncher.launch("image/*;video/*");
                    }
                });
            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void requestCameraPermission(int openCameraType) {
        String[] permissions = {Manifest.permission.CAMERA};
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle(this.getString(R.string.permission))
                .setSettingsDialogTitle(this.getString(R.string.camera_permission));
        Permissions.check(this, permissions, this.getString(R.string.please_provide_camera), options, new PermissionHandler() {
            @Override
            public void onGranted() {
                if (openCameraType == 1) {
                    openCamera(1);
                } else if (openCameraType == 2) {
                    openVideo(2);
                }
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Snackbar.make(binding.getRoot(), getString(R.string.permission_denied), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void openCamera(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            file = createImageFile();

        } catch (IOException ex) {
            Toast.makeText(this, getResources().getString(R.string.start_camera_failed), Toast.LENGTH_LONG).show();
        }
        if (file != null) {
            imgPath = file.getAbsolutePath();
            Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".App.fileprovider", file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            viewModel.setFileUri(fileUri);
            viewModel.setFilePath(imgPath);
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    public void openVideo(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {
            file = createVideoFile();
        } catch (IOException ex) {
            Toast.makeText(this, getResources().getString(R.string.start_camera_failed), Toast.LENGTH_LONG).show();
        }
        if (file != null) {
            imgPath = file.getAbsolutePath();
            Uri fileUri = FileProvider.getUriForFile(this, this.getPackageName() + ".App.fileprovider", file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            takePictureIntent.putExtra(String.valueOf(MediaRecorder.OutputFormat.MPEG_4), fileUri);
            viewModel.setFileUri(fileUri);
            viewModel.setFilePath(imgPath);
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        if (wasSuccessful) {
            imgPath = path;
            file = new File(path);
            filename = file.getName();
            this.extension = imgPath.substring(imgPath.lastIndexOf("."));
            AddImageRecycler(Uri.parse(imgPath));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_CAMERA) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = viewModel.getFileUri();
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
            imgPath = viewModel.getFilePath();
            this.extension = imgPath.substring(imgPath.lastIndexOf("."));
            AddImageRecycler(contentUri);
        }
        if (requestCode == PICK_VIDEO_CAMERA) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = viewModel.getFileUri();
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
            imgPath = viewModel.getFilePath();
            this.extension = imgPath.substring(imgPath.lastIndexOf("."));
            AddImageRecycler(contentUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AddImageRecycler(Uri contentUri) {
        String[] docSplit = imgPath.split("/");
        String[] fileName = docSplit[docSplit.length - 1].split("\\.");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                ,getString(R.string.app_name)+File.separator+String.format("%s_%s.%s", String.format("%s_%s", "SDB"
                , System.currentTimeMillis()), fileName[0], fileName[1]));
        if (!file.exists()) {
            file.mkdir();
        }
        insertMediaApiCaller(contentUri, extension, bladesRetrieveData.getDesignId(), "5");
    }

    private void insertMediaApiCaller(Uri imgPath, String extension, String blastCode, String blastNo) {

        mResultsBitmap = BitmapUtils.resamplePic(this, String.valueOf(imgPath));
        BitmapUtils.saveImage(this, mResultsBitmap,extension,imgPath);
        showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("BlastCode", blastCode);
        map.put("imagename", String.format("%s_%s", "SDB", System.currentTimeMillis()));
        map.put("imageurl", String.valueOf(imgPath));
        map.put("Blastno", blastNo);
        map.put("imagetype", "pre");
        map.put("media_clicked_date", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        map.put("media_upload_date", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        map.put("media_source", "");
        map.put("media_type", extension.equals(".MP4") ? "video" : "image");

        MainService.ImageVideoApiCaller(this, map).observe((LifecycleOwner) this, responsemedia -> {
            if (responsemedia == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (responsemedia != null) {
                    try {
                        JsonObject jsonObject = responsemedia.getAsJsonObject();
                        if (jsonObject != null) {
                            try {
                                if (responsemedia.getAsJsonObject().get("Response").getAsString().equals("Success")) {
                                    Toast.makeText(this, responsemedia.getAsJsonObject().get("ReturnObject").getAsString(), Toast.LENGTH_LONG).show();
                                    insertImageList();
                                    uploadMediaApiCaller(extension);
                                } else {
                                    Toast.makeText(this, "Error In Insertion", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        } else {
                            showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                    hideLoader();
                }
            }
            hideLoader();
        });
    }

    private void uploadMediaApiCaller(String extension) {
        mResultsBitmap = BitmapUtils.resamplePic(this, imgPath);
//        BitmapUtils.saveImage(this, mResultsBitmap,extension, imgPath);
        showLoader();
        Map<String, RequestBody> map = new HashMap<>();
        map.put("mediatype", toRequestBody(extension.equals(".MP4") ? "Video" : "Image"));
        MultipartBody.Part fileData = null;
        if (imgPath != null) {
            File compressedImgFile = Compressor.getDefault(this).compressToFile(file);

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("Media","MicrosoftTeams-image.png",
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    compressedImgFile))
                    .build();

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImgFile);
            fileData = MultipartBody.Part.createFormData("Media", file.getName(), body);
        }

        MainService.uploadApiCallerImage(this, map, fileData).observe(this, responseupload -> {
            if (responseupload == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (responseupload != null) {
                    try {
                        JsonObject jsonObject = responseupload.getAsJsonObject();
                        if (jsonObject != null) {
                            try {
                                if (responseupload.getAsJsonObject().has("Message")) {
                                    Toast.makeText(this,

                                                    responseupload.getAsJsonObject().get("Message").getAsString(),
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            } catch (Exception e) {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        } else {
                            showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                    hideLoader();
                }
            }
            hideLoader();
        });


    }

    private void insertImageList() {
        AppDelegate.getInstance().setSingleImageIntoList(imgPath);
        imageListBlank();
    }


    File mediaFile;

    private void fileDownloadCode(String docType, String url) {
        String[] docSplit = url.split("/");
        String[] fileName = docSplit[docSplit.length - 1].split("\\.");
        mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),getString(R.string.app_name)+File.separator+String.format("%s_%s.%s", docType, fileName[0], fileName[1]));
        if (!mediaFile.exists()) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            Permissions.check(this, permissions, null, null, new PermissionHandler() {
                @Override
                public void onGranted() {
                    downloadMedia(docType,url, String.format("%s_%s.jpg", docType, fileName[0]));
                }
            });
        } else {
            showToast("File already exist");
        }
    }

    private void downloadMedia(String docType, String url, String fileName) {
        if (checkDir()) {
            long refId = downloadFile(url, fileName, mediaFile);
            Log.e(String.valueOf(refId), url);
            showToast("File successfully downloaded at "+mediaFile.getAbsolutePath());
        }
    }

    public boolean checkDir() {
        File dirName = new File(Environment.DIRECTORY_DOWNLOADS, getString(R.string.app_name));
        if (!dirName.exists()) {
            createDir();
        }
        return true;
    }

    public void createDir() {
        try {
            File dir = new File(Environment.DIRECTORY_DOWNLOADS, getString(R.string.app_name));
            Log.e("msg", "createFolder: " + dir.exists());
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception ex) {
            Log.e("error", "creating file error: ", ex);
        }

    }

    private long downloadFile(String url, String fileName, File filePath) {
        Uri uri = Uri.parse(url);
        DonwloadFileManagerUtils downloadHelper = new DonwloadFileManagerUtils(this);
        return downloadHelper.downloadData(uri, fileName, filePath);
    }

}