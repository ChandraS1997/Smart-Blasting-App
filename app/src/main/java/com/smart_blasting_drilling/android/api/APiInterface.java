package com.smart_blasting_drilling.android.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.api.apis.response.ResponseAllRecordData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.GetAllMinePitZoneBenchResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface APiInterface {

    @Multipart
    @POST("v2/index.php")
    Call<JsonObject> loginApiCaller(@QueryMap Map<String, Object> queryMap, @PartMap Map<String, RequestBody> map);

    @Multipart
    @POST("v1/index.php")
    Call<JsonObject> registerApiCaller(@QueryMap Map<String, Object> queryMap, @PartMap Map<String, RequestBody> map);

    @GET("BLADES_API/Service1.svc/Retrieve3DDesignByDate/{start_time}/{end_time}/{user_id}/1/{company_id}/centralmineinfo")
    Call<JsonObject> retrieve3DDegignByDateApiCaller(@Path(value = "start_time", encoded = true) String startDate, @Path(value = "end_time", encoded = true) String endDate, @Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("BLADES_API/Service1.svc/RetrieveByDate/{start_time}/{end_time}/{user_id}/1/{company_id}/centralmineinfo")
    Call<JsonObject> retrieveByDateApiCaller(@Path(value = "start_time", encoded = true) String startDate, @Path(value = "end_time", encoded = true) String endDate, @Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("BLADES_API/Service1.svc//get-mine-pit-zone-bench/{user_id}/{company_id}/1")
    Call<GetAllMinePitZoneBenchResult> getMinePitZoneBenchApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("BLADES_API/Service1.svc/GetAllDesignInfo/{blast_id}/{user_id}/{company_id}/{db_name}/{record_status}")
    Call<JsonElement> getAllDesignInfoApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "blast_id", encoded = true) String blastId, @Path(value = "db_name", encoded = true) String dbName, @Path(value = "company_id", encoded = true) String companyId, @Path(value = "record_status", encoded = true) int recordStatus);

    @GET("BLADES_API/Service1.svc/GetAll3DDesignInfo/{blast_id}/{user_id}/{company_id}/{db_name}/{record_status}")
    Call<JsonElement> getAllDesign3DInfoApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "blast_id", encoded = true) String blastId, @Path(value = "db_name", encoded = true) String dbName, @Path(value = "company_id", encoded = true) String companyId, @Path(value = "record_status", encoded = true) int recordStatus);

    @GET("v5/getrecord/{user_id}/{company_id}/1")
    Call<ResponseAllRecordData> getRecordApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("BLADES_API/Service1.svc//get-allmineinfo/SurfaceInitiator/{user_id}/{company_id}/1")
    Call<JsonElement> getAllMineInfoSurfaceInitiatorApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("get-drillaccessoriesinfoalldata/{user_id}/{company_id}")
    Call<JsonElement> getDrillAccessoriesInfoAllDataApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("getDrillMethod")
    Call<JsonElement> getDrillMethodApiCaller();

    @GET("getDrillMaterial")
    Call<JsonElement> getDrillMaterialApiCaller();

    @POST("v3/Insertmedia")
    Call<JsonObject> InsertMediaApiCaller(@Body Map<String, Object> map);

    @Multipart
    @POST("api/sblast/upload_media.php?mediatype=Image")
    Call<JsonObject> UploadeApiCallerImage(/*@PartMap Map<String, RequestBody> map, */@Part MultipartBody.Part fileData);

    @Multipart
    @POST("api/sblast/upload_media.php?mediatype=Video")
    Call<JsonObject> UploadeApiCallerVideo(@PartMap Map<String, RequestBody> map,@Part MultipartBody.Part fileData);

    @POST("insertUpdateAppSyncDetails")
    Call<JsonObject> insertUpdateAppSyncDetailsApiCaller(@Body Map<String, Object> map);

    @POST("insertUpdateAppHoleDetailsSync")
    Call<JsonObject> insertUpdateAppHoleDetailsSyncApiCaller(@Body Map<String, Object> map);

    @POST("insertUpdateAppHoleDetailsmultipleSync")
    Call<JsonObject> insertUpdateAppHoleDetailsmultipleSyncApiCaller(@Body Map<String, Object> map);

}
