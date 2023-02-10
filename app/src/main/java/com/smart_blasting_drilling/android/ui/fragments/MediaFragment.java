package com.smart_blasting_drilling.android.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.ui.adapter.MediaAdapter;
import com.smart_blasting_drilling.android.activity.AuthActivity;
import com.smart_blasting_drilling.android.activity.BaseActivity;
import com.smart_blasting_drilling.android.adapter.MediaAdapter;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.InsertMediaResponse;
import com.smart_blasting_drilling.android.api.apis.response.ResponseLoginData;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.MediaFragmentBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MediaFragment extends BaseFragment implements PickiTCallbacks {

    private final int PICK_IMAGE_CAMERA = 1;
    MediaFragmentBinding binding;
    MediaAdapter mediaAdapter;
    File file = null;
    String imgPath;
    PickiT pickiT;
    ActivityResultLauncher<String> activityResultLauncher;
    String filename;
    Bitmap bitmap;
     String extension;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.media_fragment, container, false);
            binding.clickhereTv.setOnClickListener(view -> selectImage());
            pickiT = new PickiT(mContext, this, getActivity());

            imageListBlank();

            activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    pickiT.getPath(uri, Build.VERSION.SDK_INT);
                }
            });

            binding.addCameraFab.setOnClickListener(view -> selectImage());

        }
        return binding.getRoot();
    }

    private void imageListBlank() {
        System.out.println("inside imageListBlank"+AppDelegate.getInstance().getImgList());

        if (!Constants.isListEmpty(AppDelegate.getInstance().getImgList()))
        {

           // imageList.addAll(AppDelegate.getInstance().getImgList());
            mediaAdapter = new MediaAdapter(mContext, AppDelegate.getInstance().getImgList());
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

    private void selectImage() {
        System.out.println("inside selectImage");
        final CharSequence[] options = {mContext.getString(R.string.take_photo), mContext.getString(R.string.choose_from), mContext.getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(mContext.getString(R.string.choose_option));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(mContext.getString(R.string.take_photo))) {
                if (checkPermission()) {
                    openCamera(1);
                } else {
                    requestCameraPermission();
                }
                dialog.dismiss();
            } else if (options[item].equals(mContext.getString(R.string.choose_from_gallery))) {
                dialog.dismiss();
                String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                Permissions.check(mContext, permission, null, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        String input = "image/*;video/*";
                        activityResultLauncher.launch(input);
                    }
                });
            } else if (options[item].equals(mContext.getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void requestCameraPermission() {
        System.out.println("inside requestCameraPermission");
        String[] permissions = {Manifest.permission.CAMERA};
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle(mContext.getString(R.string.permission))
                .setSettingsDialogTitle(mContext.getString(R.string.camera_permission));
        Permissions.check(getActivity(), permissions, mContext.getString(R.string.please_provide_camera), options, new PermissionHandler() {
            @Override
            public void onGranted() {
                openCamera(1);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Snackbar.make(binding.getRoot(), mContext.getString(R.string.permission_denied), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void openCamera(int requestCode) {
        System.out.println("inside openCamera");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            file = createImageFile();

        } catch (IOException ex) {
            Toast.makeText(requireActivity(), getResources().getString(R.string.start_camera_failed), Toast.LENGTH_LONG).show();
        }
        if (file != null) {

            imgPath = file.getAbsolutePath();
            Uri fileUri = FileProvider.getUriForFile(requireContext(), requireActivity().getPackageName() + ".App.fileprovider", file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, requestCode);
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
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
            /*if (new File(path).length() <= (2 * 1024 * 1024)) {
                if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".jpeg")) {

                }
            } else {
                showToast(mContext.getString(R.string.please_select_2mb_size));
            }*/
            imgPath = path;
            file = new File(path);
        //    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
           /* Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            System.out.println("inside insertImageList file"+file);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            requireActivity().sendBroadcast(mediaScanIntent);
            // saveImage();
            System.out.println("inside PickiTonCompleteListener");*/
            filename=file.getName();
            AddImageRecycler();
           // imageListBlank();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_CAMERA)
        {
            System.out.println("inside onActivityResult");
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            System.out.println("inside insertImageList file"+file);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            requireActivity().sendBroadcast(mediaScanIntent);
            // saveImage();
            AddImageRecycler();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void AddImageRecycler()
    {
        InsertMediaApiCALLER(imgPath);
    }

    private void InsertMediaApiCALLER(String imgPath)
    {
        showLoader();
        Map<String, Object> map=new HashMap<>();
        map.put("BlastCode","114239");
        map.put("imagename","myimage");
        map.put("imageurl",imgPath);
        map.put("Blastno","5");
        map.put("imagetype","pre");
        map.put("media_clicked_date","2023-01-15 13:57:00");
        map.put("media_upload_date","2023-01-15 13:57:00");
        map.put("media_source","");
        map.put("media_type","image");
        MainService.ImageVideoApiCaller(mContext, map).observe((LifecycleOwner) mContext, responsemedia -> {
            if (responsemedia == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (responsemedia != null) {

                    try {

                       JsonObject jsonObject = responsemedia.getAsJsonObject();
                        if (jsonObject != null) {

                            try {
                                if (responsemedia.getAsJsonObject().get("Response").getAsString().equals("Success"))
                                {
                                    Toast.makeText(mContext,responsemedia.getAsJsonObject().get("ReturnObject").getAsString(), Toast.LENGTH_LONG).show();
                                    insertImageList();
                                     extension = imgPath.substring(imgPath.lastIndexOf("."));
                                    if(extension.equals(".mp4"))
                                    {
                                        uploadeVideo();
                                    }
                                    else
                                    {
                                        uploadeImage();
                                    }

                                       /*  imageList.add(imgPath);
                                         AppDelegate.getInstance().setImgList(imageList);
                                      binding.mediaRecycler.setLayoutManager(new GridLayoutManager(mContext, 4));
                                   mediaAdapter = new MediaAdapter(mContext, imageList);
                                   binding.mediaRecycler.setAdapter(mediaAdapter);
                                   mediaAdapter.notifyDataSetChanged();*/
                                }
                                else
                                {
                                    Toast.makeText(mContext, "Error In Insertion", Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception e)
                            {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        }
                    else {
                            ((BaseActivity) requireActivity()).showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
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

    private void uploadeVideo() {
        showLoader();
        Map<String, RequestBody> map=new HashMap<>();
/*        map.put("options.mimeType","video/jpeg");
        map.put("mediaPath",imgPath);*/
        map.put("mediatype",toRequestBody("Video"));
      //  map.put("Media",toRequestBody(imgPath));


        MultipartBody.Part fileData = null;

        if (imgPath != null) {
            File compressedImgFile = Compressor.getDefault(mContext).compressToFile(file);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImgFile);
            fileData = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }



        MainService.uploadApiCallerVideo(mContext, map,fileData).observe((LifecycleOwner) mContext, responseupload -> {
            if (responseupload == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (responseupload != null) {

                    try {

                        JsonObject jsonObject = responseupload.getAsJsonObject();
                        if (jsonObject != null) {

                          /*  try
                            {
                                System.out.println("inside try");
                                   if (responseupload.getAsJsonObject().get("status").getAsBoolean())
                                    {
                                    Toast.makeText(mContext, responseupload.getAsJsonObject().get("Message").getAsString(), Toast.LENGTH_LONG).show();
                                  }
                                else
                                {
                                    Toast.makeText(mContext, R.string.error_insertion, Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println("inside catch"+e);
                                Log.e(NODATAFOUND, e.getMessage());
                            }*/

                            try {
                                System.out.println("inside try");
                                if (responseupload.getAsJsonObject().has("Message")) {
                                    Toast.makeText(mContext,
                                                    responseupload.getAsJsonObject().get("Message").getAsString(),
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                            catch (Exception e)
                            {
                                System.out.println("inside catch"+e);
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        }
                        else {
                            ((BaseActivity) requireActivity()).showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
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

    private void uploadeImage()
    {
        showLoader();
        Map<String, RequestBody> map = new HashMap<>();
        map.put("mediatype",toRequestBody("Image"));
        MultipartBody.Part fileData = null;
        if (imgPath != null) {
            File compressedImgFile = Compressor.getDefault(mContext).compressToFile(file);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImgFile);
            fileData = MultipartBody.Part.createFormData("Media", file.getName(), requestFile);
        }

        MainService.uploadApiCallerImage(mContext, map, fileData).observe((LifecycleOwner) mContext, responseupload -> {
            if (responseupload == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (responseupload != null) {

                    try {

                        JsonObject jsonObject = responseupload.getAsJsonObject();
                        if (jsonObject != null) {

                            try {
                                if (responseupload.getAsJsonObject().has("Message")) {
                                    Toast.makeText(mContext,
                                                    responseupload.getAsJsonObject().get("Message").getAsString(),
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                            catch (Exception e)
                            {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        }
                        else {
                            ((BaseActivity) requireActivity()).showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
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

    private void insertImageList()
    {
        System.out.println("imgPath"+imgPath);

      /*  Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        System.out.println("inside insertImageList file"+file);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        requireActivity().sendBroadcast(mediaScanIntent);
       // saveImage();*/
        AppDelegate.getInstance().setSingleImageIntoList(imgPath);
        imageListBlank();
    }
}
