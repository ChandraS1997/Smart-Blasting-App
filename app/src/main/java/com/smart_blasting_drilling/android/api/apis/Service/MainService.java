package com.smart_blasting_drilling.android.api.apis.Service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.api.APIError;
import com.smart_blasting_drilling.android.api.APiInterface;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainService {

    private static final APiInterface loginApiService = BaseService.getAPIClient(Constants.API_DEFAULT_URL).create(APiInterface.class);
    private static final APiInterface bladesApiService = BaseService.getAPIClient(Constants.API_BLADES_URL).create(APiInterface.class);
    private static final APiInterface imageVideoApiService = BaseService.getAPIClient(Constants.API_IMAGE_VIDEO_BASE_URL).create(APiInterface.class);
    private static final APiInterface uplodeApiService = BaseService.getAPIClient(Constants.API_UPLOAD_BASE_URL).create(APiInterface.class);

    public static JsonObject tokenExpiredResponse(String msg, int code, int errorCode) {
        JsonObject apiResponse = new JsonObject();
        apiResponse.addProperty("errorMessage", msg);
        apiResponse.addProperty("errorCode", errorCode);
        apiResponse.addProperty("statusCode", code);
        return apiResponse;
    }

    public static JsonObject getError(Response<JsonObject> response) {
        JsonObject jsonObject = new JsonObject();
        String message = "Something went wrong!";
        try {
            APIError apiError = new Gson().fromJson(response.errorBody().charStream(), APIError.class);
            if (apiError != null) {
                if (!StringUtill.isEmpty(apiError.getErrorMessage())) {
                    jsonObject.addProperty("errorMessage", apiError.getErrorMessage());
                }
                if (!StringUtill.isEmpty(apiError.getMessage())) {
                    jsonObject.addProperty("errorMessage", apiError.getMessage());
                } else {
                    jsonObject.addProperty("errorMessage", message);
                }
                jsonObject.addProperty("errorCode", apiError.getErrorCode());
                jsonObject.addProperty("statusCode", apiError.getStatusCode());
            }
        } catch (Exception e) {
            jsonObject.addProperty("errorMessage", message);
            jsonObject.addProperty("errorCode", 4);
            jsonObject.addProperty("statusCode", 400);
        }

        return jsonObject;
    }

    public static LiveData<JsonObject> loginApiCaller(final Context context, Map<String, RequestBody> map) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("Login", true);

        Call<JsonObject> call = loginApiService.loginApiCaller(queryMap, map);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    if (response.errorBody() != null) {
                        data.setValue(getError(response));
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    ////////////
    public static LiveData<JsonObject> RegisterApiCaller(final Context context, Map<String, RequestBody> map) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("sendemail_getaccess", true);
        Call<JsonObject> call = loginApiService.registerApiCaller(queryMap, map);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    if (response.errorBody() != null) {
                        data.setValue(getError(response));
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }
    ////////////

    public static LiveData<JsonObject> retrieve3DDegignByDateApiCaller(final Context context, String startDate, String endDate, String userId, String companyId) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonObject> call = bladesApiService.retrieve3DDegignByDateApiCaller(startDate, endDate, userId, companyId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    if (response.errorBody() != null) {
                        data.setValue(getError(response));
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonObject> retrieveByDateApiCaller(final Context context, String startDate, String endDate, String userId, String companyId) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonObject> call = bladesApiService.retrieveByDateApiCaller(startDate, endDate, userId, companyId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    if (response.errorBody() != null) {
                        data.setValue(getError(response));
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                ((BaseActivity) context).hideLoader();
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonObject> getMinePitZoneBenchApiCaller(final Context context, String userId, String companyId, String blastId) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonObject> call = bladesApiService.getMinePitZoneBenchApiCaller(userId, companyId, blastId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    if (response.errorBody() != null) {
                        data.setValue(getError(response));
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                ((BaseActivity) context).hideLoader();
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonElement> getAll2D_3DDesignInfoApiCaller(final Context context, String userId, String companyId, String blastId, String dbName, int recordStatus, boolean is3D) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (is3D) {
            getAllDesign3DInfoApiCaller(context, userId, blastId, dbName, companyId, recordStatus).observe((LifecycleOwner) context, new Observer<JsonElement>() {
                @Override
                public void onChanged(JsonElement element) {
                    data.setValue(element);
                }
            });
        } else {
            getAllDesignInfoApiCaller(context, userId, blastId, dbName, companyId, recordStatus).observe((LifecycleOwner) context, new Observer<JsonElement>() {
                @Override
                public void onChanged(JsonElement element) {
                    data.setValue(element);
                }
            });
        }

        return data;
    }

    public static LiveData<JsonElement> getAllDesignInfoApiCaller(final Context context, String userId, String companyId, String blastId, String dbName, int recordStatus) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonElement> call = bladesApiService.getAllDesignInfoApiCaller(userId, blastId, dbName, companyId, recordStatus);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    if (response.errorBody() != null) {
//                        data.setValue(getError(response));
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                ((BaseActivity) context).hideLoader();
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonElement> getAllDesign3DInfoApiCaller(final Context context, String userId, String companyId, String blastId, String dbName, int recordStatus) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonElement> call = bladesApiService.getAllDesign3DInfoApiCaller(userId, blastId, dbName, companyId, recordStatus);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    if (response.errorBody() != null) {
//                        data.setValue(getError(response));
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                ((BaseActivity) context).hideLoader();
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonObject> ImageVideoApiCaller(final Context context, Map<String, Object> map) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonObject> call = imageVideoApiService.InsertMediaApiCaller(map);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(" API FAILED ", t.getLocalizedMessage());

            }
        });
        return data;
    }

    public static LiveData<JsonObject> uploadApiCallerImage(final Context context, Map<String, RequestBody> map, MultipartBody.Part fileData) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonObject> call = uplodeApiService.UploadeApiCallerImage(map, fileData);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(" API FAILED ", t.getLocalizedMessage());

            }
        });
        return data;
    }

    public static LiveData<JsonObject> uploadApiCallerVideo(final Context context, Map<String, RequestBody> map, MultipartBody.Part fileData) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonObject> call = uplodeApiService.UploadeApiCallerVideo(map, fileData);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(" API FAILED ", t.getLocalizedMessage());

            }
        });
        return data;
    }

}
