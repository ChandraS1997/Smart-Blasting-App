package com.smart_blasting_drilling.android.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.databinding.ActivityMediaBinding;
import com.smart_blasting_drilling.android.helper.ConnectivityReceiver;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;
import com.smart_blasting_drilling.android.room_database.entities.MediaUploadEntity;
import com.smart_blasting_drilling.android.ui.adapter.MediaAdapter;
import com.smart_blasting_drilling.android.ui.models.MediaDataModel;
import com.smart_blasting_drilling.android.utils.BitmapUtils;
import com.smart_blasting_drilling.android.utils.DateUtils;
import com.smart_blasting_drilling.android.utils.DonwloadFileManagerUtils;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;
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

public class Media3dActivity extends BaseActivity implements PickiTCallbacks, MediaAdapter.MediaDeleteListener {

    private final int PICK_IMAGE_CAMERA = 1;
    private final int PICK_VIDEO_CAMERA = 2;
    ActivityMediaBinding binding;
    MediaAdapter mediaAdapter;
    File file = null;
    String imgPath;
    MediaDataModel mediaDataModel;
    PickiT pickiT;
    ActivityResultLauncher<String> activityResultLauncher;
    String filename;
    String extension;
    String designId = "";
    MediaViewModel viewModel;
    File mediaFile;
    private Bitmap mResultsBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media);
        viewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        StatusBarUtils.statusBarColor(this, R.color._FFA722);
        binding.headerMedia.mediaTitle.setText(getString(R.string.media));

        if (isBundleIntentNotEmpty()) {
            designId = getIntent().getExtras().getString("blades_data");
        }
        AppDelegate.getInstance().setMediaDataModelList(null);

        binding.headerMedia.backImg.setOnClickListener(view -> {
            addMediaIntoDb();
            finish();
        });

        //getAllImageFromGallery();

        binding.clickhereTv.setOnClickListener(view -> selectImage());
        pickiT = new PickiT(this, this, this);

        if (!StringUtill.isEmpty(designId)) {
            if (appDatabase.mediaUploadDao().isExistItem(designId)) {
                String data = appDatabase.mediaUploadDao().getSingleItemEntity(designId).getData();
                List<MediaDataModel> imageList = new Gson().fromJson(data, new TypeToken<List<MediaDataModel>>() {
                }.getType());
                AppDelegate.getInstance().setMediaDataModelList(imageList);
            }
        }

        imageListBlank();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                pickiT.getPath(uri, Build.VERSION.SDK_INT);
            }
        });

        binding.addCameraFab.setOnClickListener(view -> selectImage());

    }

    @Override
    public void onBackPressed() {
        addMediaIntoDb();
        super.onBackPressed();
    }

    private void addMediaIntoDb() {
        if (!Constants.isListEmpty(AppDelegate.getInstance().getMediaDataModelList())) {
            String data = new Gson().toJson(AppDelegate.getInstance().getMediaDataModelList());
            if (appDatabase.mediaUploadDao().isExistItem(designId)) {
                appDatabase.mediaUploadDao().updateItem(designId, data);
            } else {
                MediaUploadEntity mediaUploadEntity = new MediaUploadEntity();
                mediaUploadEntity.setProjectId(designId);
                mediaUploadEntity.setData(data);
                appDatabase.mediaUploadDao().insertItem(mediaUploadEntity);
            }
        }
    }

    private void imageListBlank() {
        if (!Constants.isListEmpty(AppDelegate.getInstance().getMediaDataModelList())) {
            mediaAdapter = new MediaAdapter(this, AppDelegate.getInstance().getMediaDataModelList());
            binding.mediaRecycler.setAdapter(mediaAdapter);

            mediaAdapter.setUpListener(this);
        }
        if (Constants.isListEmpty(AppDelegate.getInstance().getMediaDataModelList())) {
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

    private void selectImage() {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.take_video), getString(R.string.choose_from), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_option));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.take_photo))) {
                String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(this, permission, null, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        openCamera(1);
                    }
                });
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.take_video))) {
                String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(this, permission, null, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        openVideo(2);
                    }
                });
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
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
            //takePictureIntent.putExtra(String.valueOf(MediaRecorder.), fileUri);
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
        try {
            String[] docSplit = imgPath.split("/");
            String[] fileName = docSplit[docSplit.length - 1].split("\\.");
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    , getString(R.string.app_name) + File.separator + String.format("%s_%s.%s", String.format("%s_%s", "SDB"
                    , System.currentTimeMillis()), fileName[0], fileName[1]));
            if (!file.exists()) {
                file.mkdir();
            }
            String type = "";
            if (extension.equalsIgnoreCase(".mp4")) {
                type = "video";
            } else {
                type = "image";
            }
            mediaDataModel = new MediaDataModel(fileName[0], imgPath, extension, type, false);
            if (ConnectivityReceiver.getInstance().isInternetAvailable()) {
                List<Response3DTable1DataModel> response3DTable1DataModelList = AppDelegate.getInstance().getResponse3DTable1DataModel();
                if (response3DTable1DataModelList.size() > 0) {
                    String blastCode = "";
                    if (!StringUtill.isEmpty(String.valueOf(response3DTable1DataModelList.get(0).getBimsId()))) {
                        blastCode = String.valueOf(response3DTable1DataModelList.get(0).getBimsId());
                    } else {
                        if (!Constants.isListEmpty(appDatabase.blastCodeDao().getAllEntityDataList())) {
                            BlastCodeEntity blastCodeEntity = appDatabase.blastCodeDao().getSingleItemEntityByDesignId(response3DTable1DataModelList.get(0).getDesignId());
                            if (blastCodeEntity != null)
                                blastCode = blastCodeEntity.getBlastCode();
                        }
                    }
                    uploadMediaApiCaller(contentUri, extension, blastCode, response3DTable1DataModelList.get(0).getDesignCode());
                }
            } else {
                mediaDataModel.setSynced(false);
                insertImageList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertMediaApiCaller(Uri imgPath, String extension, String blastCode, String blastNo) {
        mResultsBitmap = BitmapUtils.resamplePic(this, String.valueOf(imgPath));
        BitmapUtils.saveImage(this, mResultsBitmap, extension, imgPath);
        BitmapUtils.galleryAddPic(this, String.valueOf(imgPath));
        showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("BlastCode", blastCode);
        map.put("imagename", new File(String.valueOf(imgPath)).getName());//String.format("%s_%s", "SDB", System.currentTimeMillis()));
        map.put("imageurl", String.format("%s/%s", extension.equals(".mp4") ? "blastgallaryvideo" : "blastgallaryimages", new File(String.valueOf(imgPath)).getName()));// blastgallaryimages/file_name.jpg
        map.put("Blastno", blastNo);
        map.put("imagetype", "pre");
        map.put("media_clicked_date", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        map.put("media_upload_date", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        map.put("media_source", "");
        map.put("media_type", extension.equals(".mp4") ? "video" : "image");
        MainService.ImageVideoApiCaller(this, map).observe((LifecycleOwner) this, responsemedia -> {
            hideLoader();
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

                                    mediaDataModel.setSynced(true);
                                    insertImageList();
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
                }
            }
        });
    }

    public void VideoToGif(String uri) {
        try {
            Uri videoFileUri = Uri.parse(uri);

            MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
            mRetriever.setDataSource(uri);

            /*FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
            retriever.setDataSource(uri);*/
            List<Bitmap> rev = new ArrayList<Bitmap>();
            MediaPlayer mp = MediaPlayer.create(this, videoFileUri);
            int millis = mp.getDuration();
            System.out.println("starting point");
            for (int i = 100000; i <= millis * 1000; i += 100000 * 2) {
                Bitmap bitmap = mRetriever.getFrameAtTime(i, MediaMetadataRetriever.OPTION_CLOSEST);
                rev.add(bitmap);
            }
            Log.e("Bitmap : ", new Gson().toJson(rev));
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void uploadMediaApiCaller(Uri imgPath, String extension, String blastCode, String blastNo) {
        showLoader();
        mResultsBitmap = BitmapUtils.resamplePic(this, this.imgPath);
        /*if (extension.equalsIgnoreCase(".mp4"))
            VideoToGif(imgPath);*/
        BitmapUtils.saveImage(this, mResultsBitmap, extension, Uri.parse(this.imgPath));
        Map<String, RequestBody> map = new HashMap<>();
        map.put("mediatype", toRequestBody(extension.equals(".mp4") ? "Video" : "Image"));
        MultipartBody.Part fileData = null;
        if (extension.equalsIgnoreCase(".mp4")) {
            File file = new File(this.imgPath);
            RequestBody requestFile = RequestBody.create(file, MediaType.parse(extension.equalsIgnoreCase(".mp4 ") ? "video/mp4":"image/png"));
            fileData = MultipartBody.Part.createFormData("Media", file.getName(), requestFile);
            uploadMediaApiFun(fileData, imgPath, extension, blastCode, blastNo);
        } else {
            File compressedImgFile = Compressor.getDefault(this).compressToFile(new File(this.imgPath));
            RequestBody requestFile = RequestBody.create(compressedImgFile, MediaType.parse(extension.equalsIgnoreCase(".mp4 ") ? "video/mp4":"image/png"));
            fileData = MultipartBody.Part.createFormData("Media", new File(this.imgPath).getName(), requestFile);
            uploadMediaApiFun(fileData, imgPath, extension, blastCode, blastNo);
        }
    }

    private void uploadMediaApiFun(MultipartBody.Part fileData, Uri imgPath, String extension, String blastCode, String blastNo) {
        MainService.uploadApiCallerImage(this, extension.equals(".mp4") ? "Video" : "Image" ,fileData).observe(this, responseUpload -> {
            if (responseUpload == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                try {
                    JsonObject jsonObject = responseUpload.getAsJsonObject();
                    if (jsonObject != null) {
                        try {
                            if (responseUpload.getAsJsonObject().has("Message")) {
                                Toast.makeText(this, responseUpload.getAsJsonObject().get("Message").getAsString(), Toast.LENGTH_LONG).show();
                            }
                            insertMediaApiCaller(imgPath, extension, designId, blastNo);
                        } catch (Exception e) {
                            Log.e(NODATAFOUND, e.getMessage());
                        }

                    } else {
                        showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                    }
                } catch (Exception e) {
                    Log.e(NODATAFOUND, e.getMessage());
                }
            }
            hideLoader();
        });
    }

    private void insertImageList() {
        AppDelegate.getInstance().setSingleMediaIntoList(mediaDataModel);
        imageListBlank();
    }

    private void fileDownloadCode(String docType, @NonNull String url) {
        String[] docSplit = url.split("/");
        String[] fileName = docSplit[docSplit.length - 1].split("\\.");
        mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getString(R.string.app_name) + File.separator + String.format("%s_%s.%s", docType, fileName[0], fileName[1]));
        if (!mediaFile.exists()) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            Permissions.check(this, permissions, null, null, new PermissionHandler() {
                @Override
                public void onGranted() {
                    downloadMedia(docType, url, String.format("%s_%s.jpg", docType, fileName[0]));
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
            showToast("File successfully downloaded at " + mediaFile.getAbsolutePath());
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

    @Override
    public void selectMediaCallBack() {
        binding.headerMedia.deleteIcn.setVisibility(View.VISIBLE);
        binding.headerMedia.deleteIcn.setOnClickListener(view -> {
            Log.e("Selected Images : ", new Gson().toJson(mediaAdapter.getImageList()));
            List<MediaDataModel> mediaDataModelList = new ArrayList<>();
            for (MediaDataModel dataModel : mediaAdapter.getImageList()) {
                if (!dataModel.isSelection()) {
                    mediaDataModelList.add(dataModel);
                }
            }
            AppDelegate.getInstance().setMediaDataModelList(mediaDataModelList);
            imageListBlank();
        });
    }

    @Override
    public void clearMediaCallBack() {
        binding.headerMedia.deleteIcn.setVisibility(View.GONE);
    }
}