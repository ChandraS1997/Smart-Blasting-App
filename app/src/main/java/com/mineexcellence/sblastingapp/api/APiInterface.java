package com.mineexcellence.sblastingapp.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseAllRecordData;
import com.mineexcellence.sblastingapp.api.apis.response.hole_tables.GetAllMinePitZoneBenchResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APiInterface {

    @Multipart
    @POST("v2/index.php")
    Call<JsonObject> loginApiCaller(@QueryMap Map<String, Object> queryMap, @PartMap Map<String, RequestBody> map);

    @POST("v2/index.php")
    Call<JsonObject> registerApiCaller(@QueryMap Map<String, Object> queryMap/*, @PartMap Map<String, RequestBody> map*/);

    @GET("BLADES_API/Service1.svc/Retrieve3DDesignByDate/{start_time}/{end_time}/{company_id}/1/{user_id}/centralmineinfo")
    Call<JsonObject> retrieve3DDegignByDateApiCaller(@Path(value = "start_time", encoded = true) String startDate, @Path(value = "end_time", encoded = true) String endDate, @Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("BLADES_API/Service1.svc/RetrieveByDate/{start_time}/{end_time}/{company_id}/1/{user_id}/centralmineinfo")
    Call<JsonObject> retrieveByDateApiCaller(@Path(value = "start_time", encoded = true) String startDate, @Path(value = "end_time", encoded = true) String endDate, @Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("BLADES_API/Service1.svc//get-mine-pit-zone-bench/{user_id}/{company_id}/1")
    Call<GetAllMinePitZoneBenchResult> getMinePitZoneBenchApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @GET("BLADES_API/Service1.svc/GetAllDesignInfo/{blast_id}/{company_id}/{user_id}/{db_name}/{record_status}")
    Call<JsonElement> getAllDesignInfoApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "blast_id", encoded = true) String blastId, @Path(value = "db_name", encoded = true) String dbName, @Path(value = "company_id", encoded = true) String companyId, @Path(value = "record_status", encoded = true) int recordStatus);

    // GetAll3DDesignInfo for test
    @GET("BLADES_API/Service1.svc/GetAll3DDesignwithActualInfo/{blast_id}/{company_id}/{user_id}/{db_name}/{record_status}")
    Call<JsonElement> getAllDesign3DInfoApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "blast_id", encoded = true) String blastId, @Path(value = "db_name", encoded = true) String dbName, @Path(value = "company_id", encoded = true) String companyId, @Path(value = "record_status", encoded = true) int recordStatus);

    @GET("v5/getrecord/{company_id}/{user_id}/1")
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
    @POST("upload_media.php")
    Call<JsonObject> UploadeApiCallerImage( @Query ("mediatype")String mediatype, @Part MultipartBody.Part fileData);

    @Multipart
    @POST("upload_media.php")
    Call<JsonObject> UploadeApiCallerVideo(@Query("mediatype") String mediatype,@Part MultipartBody.Part fileData);

    @POST("insertUpdateAppSyncDetails")
    Call<JsonPrimitive> insertUpdateAppSyncDetailsApiCaller(@Body JsonObject map);

    @POST("insertUpdateAppHoleDetailsSync")
    Call<JsonPrimitive> insertUpdateAppHoleDetailsSyncApiCaller(@Body JsonObject map);

    @POST("insertUpdateAppHoleDetailsmultipleSync")
    Call<JsonElement> insertUpdateAppHoleDetailsmultipleSyncApiCaller(@Body JsonObject map);

    @POST("v6/InsertRecord")
    Call<JsonObject> bimsInsertSyncRecordApiCaller(@Body JsonObject map);

    @POST("insertActualDesignChartsheet/reset")
    Call<JsonElement> insertActualDesignChartSheetApiCaller(@Body JsonArray map);

    @POST("InsertUpdate3DActualDesignHoleDetail/reset")
    Call<JsonElement> insertUpdate3DActualDesignHoleDetailApiCaller(@Body JsonArray map);

    @POST("InsertUpdate3DActualPreSplitHoleDetail")
    Call<JsonElement> insertUpdate3DActualPreSplitHoleDetailApiCaller(@Body JsonObject map);

    @POST("InsertUpdate3DActualDesignPilotHoleDetail/reset")
    Call<JsonElement> insertUpdate3DActualDesignPilotHoleDetailApiCaller(@Body JsonArray map);

    @GET("get-drillaccessoriesinfo/GetShiftInfo/{user_id}/{company_id}")
    Call<JsonObject> getDrillShiftInfoApiCaller(@Path(value = "user_id", encoded = true) String userId, @Path(value = "company_id", encoded = true) String companyId);

    @POST("Update3DDesignBIMSId")
    Call<JsonElement> update3DDesignBIMSIdApiCaller(@Body JsonObject map);

    @POST("Update3DDesignDRIMSId")
    Call<JsonElement> update3DDesignDRIMSIdApiCaller(@Body JsonObject map);

    @POST("UpdateDesignBIMSId")
    Call<JsonElement> updateDesignBIMSIdApiCaller(@Body JsonObject map);

    @POST("UpdateDesignDRIMSId")
    Call<JsonElement> updateDesignDRIMSIdApiCaller(@Body JsonObject map);

}
