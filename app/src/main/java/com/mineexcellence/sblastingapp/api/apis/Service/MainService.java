package com.mineexcellence.sblastingapp.api.apis.Service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mineexcellence.sblastingapp.api.APIError;
import com.mineexcellence.sblastingapp.api.APiInterface;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseAllRecordData;
import com.mineexcellence.sblastingapp.api.apis.response.hole_tables.GetAllMinePitZoneBenchResult;
import com.mineexcellence.sblastingapp.app.BaseApplication;
import com.mineexcellence.sblastingapp.dialogs.AppProgressBar;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.utils.StringUtill;

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
    private static final APiInterface drimzApiService = BaseService.getAPIClient(Constants.API_DRIMS_BASE_URL).create(APiInterface.class);
    private static final APiInterface blastSblastApiService = BaseService.getAPIClient(Constants.BLAST_S_BLAST_BASE_URL).create(APiInterface.class);
    private static final APiInterface testBlastSblastApiService = BaseService.getAPIClient(Constants.TEST_BLAST_S_BLAST_BASE_URL).create(APiInterface.class);

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

    public static LiveData<JsonObject> RegisterApiCaller(final Context context, Map<String, Object> map) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("sendemail_getaccess", true);
        Call<JsonObject> call = loginApiService.registerApiCaller(/*queryMap, */map);
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
                data.setValue(null);
            }
        });

        return data;
    }

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

    public static LiveData<GetAllMinePitZoneBenchResult> getMinePitZoneBenchApiCaller(final Context context, String userId, String companyId) {
        final MutableLiveData<GetAllMinePitZoneBenchResult> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<GetAllMinePitZoneBenchResult> call = bladesApiService.getMinePitZoneBenchApiCaller(userId, companyId);
        call.enqueue(new Callback<GetAllMinePitZoneBenchResult>() {
            @Override
            public void onResponse(@NonNull Call<GetAllMinePitZoneBenchResult> call, @NonNull Response<GetAllMinePitZoneBenchResult> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    if (response.errorBody() != null) {
                        data.setValue(null);
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetAllMinePitZoneBenchResult> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonElement> getAll2D_3DDesignInfoApiCaller(final Context context, String userId, String companyId, String blastId, String dbName, int recordStatus, boolean is3D) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (is3D) {
            getAllDesign3DInfoApiCaller(context, userId, companyId, blastId, dbName, recordStatus).observe((LifecycleOwner) context, new Observer<JsonElement>() {
                @Override
                public void onChanged(JsonElement element) {
                    data.setValue(element);
                }
            });
        } else {
            getAllDesignInfoApiCaller(context, userId, companyId, blastId, dbName, recordStatus).observe((LifecycleOwner) context, new Observer<JsonElement>() {
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
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                ((BaseActivity) context).hideLoader();
                Log.e(" API FAILED ", t.getLocalizedMessage());
                data.setValue(null);
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
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                ((BaseActivity) context).hideLoader();
                Log.e(" API FAILED ", t.getLocalizedMessage());
                data.setValue(null);
            }
        });

        return data;
    }

    public static LiveData<ResponseAllRecordData> getRecordApiCaller(final Context context, String userId, String companyId) {
        final MutableLiveData<ResponseAllRecordData> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<ResponseAllRecordData> call = imageVideoApiService.getRecordApiCaller(userId, companyId);
        call.enqueue(new Callback<ResponseAllRecordData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAllRecordData> call, @NonNull Response<ResponseAllRecordData> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseAllRecordData> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonElement> getAllMineInfoSurfaceInitiatorApiCaller(final Context context, String userId, String companyId) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonElement> call = bladesApiService.getAllMineInfoSurfaceInitiatorApiCaller(userId, companyId);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonElement> getDrillAccessoriesInfoAllDataApiCaller(final Context context, String userId, String companyId) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonElement> call = drimzApiService.getDrillAccessoriesInfoAllDataApiCaller(userId, companyId);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonElement> getDrillMethodApiCaller(final Context context) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonElement> call = drimzApiService.getDrillMethodApiCaller();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });

        return data;
    }

    public static LiveData<JsonElement> getDrillMaterialApiCaller(final Context context) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }

        Call<JsonElement> call = drimzApiService.getDrillMaterialApiCaller();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                data.setValue(null);
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
                AppProgressBar.hideLoaderDialog();
                Log.e(" API FAILED ", t.getLocalizedMessage());
                data.setValue(null);
            }
        });
        return data;
    }

    public static LiveData<JsonObject> uploadApiCallerImage(final Context context,String mediatype, MultipartBody.Part fileData) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonObject> call = uplodeApiService.UploadeApiCallerImage(mediatype,fileData);
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
                data.setValue(null);
            }
        });
        return data;
    }

    public static LiveData<JsonObject> uploadApiCallerVideo(final Context context,String mediaType, MultipartBody.Part fileData) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonObject> call = uplodeApiService.UploadeApiCallerVideo(mediaType, fileData);
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

    public static LiveData<JsonPrimitive> insertUpdateAppSyncDetailsApiCaller(final Context context, JsonObject map) {
        final MutableLiveData<JsonPrimitive> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonPrimitive> call = drimzApiService.insertUpdateAppSyncDetailsApiCaller(map);
        call.enqueue(new Callback<JsonPrimitive>() {
            @Override
            public void onResponse(Call<JsonPrimitive> call, Response<JsonPrimitive> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonPrimitive> call, Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

    public static LiveData<JsonPrimitive> insertUpdateAppHoleDetailsSyncApiCaller(final Context context, JsonObject map) {
        final MutableLiveData<JsonPrimitive> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonPrimitive> call = drimzApiService.insertUpdateAppHoleDetailsSyncApiCaller(map);
        call.enqueue(new Callback<JsonPrimitive>() {
            @Override
            public void onResponse(Call<JsonPrimitive> call, Response<JsonPrimitive> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonPrimitive> call, Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

    public static LiveData<JsonElement> insertUpdateAppHoleDetailsmultipleSyncApiCaller(final Context context, JsonObject map) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonElement> call = drimzApiService.insertUpdateAppHoleDetailsmultipleSyncApiCaller(map);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

    public static LiveData<JsonObject> bimsInsertSyncRecordApiCaller(final Context context, JsonObject map) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonObject> call = blastSblastApiService.bimsInsertSyncRecordApiCaller(map);
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
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

    public static LiveData<JsonElement> insertActualDesignChartSheetApiCaller(final Context context, JsonArray map) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonElement> call = testBlastSblastApiService.insertActualDesignChartSheetApiCaller(map);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

    public static LiveData<JsonElement> insertUpdate3DActualDesignHoleDetailApiCaller(final Context context, JsonArray map, int apiHoleType) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonElement> call;
        if (apiHoleType == Constants.PILOT_HOLE) {
            call = testBlastSblastApiService.insertUpdate3DActualDesignPilotHoleDetailApiCaller(map);
        } else {
            call = testBlastSblastApiService.insertUpdate3DActualDesignHoleDetailApiCaller(map);
        }
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

    public static LiveData<JsonElement> insertUpdate3DActualPreSplitHoleDetailApiCaller(final Context context, JsonObject map) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonElement> call = testBlastSblastApiService.insertUpdate3DActualPreSplitHoleDetailApiCaller(map);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

    public static LiveData<JsonObject> getDrillShiftInfoApiCaller(final Context context, String userId, String companyId) {
        final MutableLiveData<JsonObject> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonObject> call = drimzApiService.getDrillShiftInfoApiCaller(userId, companyId);
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
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

    public static LiveData<JsonElement> update3DDesignBIMSIdApiCaller(final Context context, JsonObject object, boolean is3D, boolean isBims) {
        final MutableLiveData<JsonElement> data = new MutableLiveData<>();
        if (!BaseApplication.getInstance().isInternetConnected(context)) {
            return data;
        }
        Call<JsonElement> call = null;

        if (is3D) {
            call = isBims ? testBlastSblastApiService.update3DDesignBIMSIdApiCaller(object) : testBlastSblastApiService.update3DDesignDRIMSIdApiCaller(object);
        } else {
            call = isBims ? testBlastSblastApiService.updateDesignBIMSIdApiCaller(object) : testBlastSblastApiService.updateDesignDRIMSIdApiCaller(object);
        }
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                data.setValue(null);
                Log.e(" API FAILED ", t.getLocalizedMessage());
            }
        });
        return data;
    }

}
