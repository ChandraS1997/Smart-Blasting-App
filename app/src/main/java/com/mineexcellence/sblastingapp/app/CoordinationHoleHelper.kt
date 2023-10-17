package com.mineexcellence.sblastingapp.app

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.mineexcellence.sblastingapp.api.apis.response.ResponseHoleDetailData
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable16PilotDataModel
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.HoleDetailItem
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.Response3DTable18PreSpilitDataModel
import com.mineexcellence.sblastingapp.helper.Constants
import com.mineexcellence.sblastingapp.ui.models.NorthEastCoordinateModel
import com.mineexcellence.sblastingapp.ui.models.ReferenceCoordinateModel
import kotlin.math.abs

fun mapCoordinatesAndroid(
    northing: Double,
    easting: Double,
    holeDetailData: List<ResponseHoleDetailData>,
) {
    try {
        val width: Long = 537
        val height: Long = 527
        var x: Double
        var y: Double
        val referenceCoordinateModelList: MutableList<ReferenceCoordinateModel> = ArrayList()
        if (northing == 0.0 || easting == 20.0) {
            for (i in holeDetailData.indices) {
                x = if (holeDetailData[i].x == 0.0) {
                    20.0
                } else {
                    abs(holeDetailData[i].x + 30)
                }
                y = abs(holeDetailData[i].y)
                referenceCoordinateModelList.add(
                    ReferenceCoordinateModel(
                        holeDetailData[i].rowNo,
                        holeDetailData[i].holeNo,
                        x,
                        y
                    )
                )
            }
        } else {
            var holeNo = 0
            val arrMinMaxEastingNorthing = findMinMaxEastingNorthingAndroid(holeDetailData)
            //for Easting- No. of pixels in meter
            val scaleFactorEasting =
                (width / (arrMinMaxEastingNorthing.maxEasting - arrMinMaxEastingNorthing.minEasting)).toFloat()
            //for Northing- No. of pixels in meter
            val scaleFactorNorthing =
                (height / (arrMinMaxEastingNorthing.maxNorthing - arrMinMaxEastingNorthing.minNorthing)).toFloat()

            // to find the scale that will fit the canvas get the min scale to fit height or width
            val scaleMinEastNorth = Math.min(scaleFactorEasting, scaleFactorNorthing)
            for (i in holeDetailData.indices) {
                x = (holeDetailData[i].y - arrMinMaxEastingNorthing.minEasting) * scaleFactorEasting
                y =
                    (holeDetailData[i].x - arrMinMaxEastingNorthing.minNorthing) * scaleFactorNorthing
                y = height - y
                holeNo += 1
                referenceCoordinateModelList.add(
                    ReferenceCoordinateModel(
                        holeDetailData[i].rowNo,
                        holeDetailData[i].holeNo,
                        x,
                        y
                    )
                )
            }
        }

        Log.e("referenceCoordinateModelList : ", Gson().toJson(referenceCoordinateModelList))
    } catch (e: Exception) {
        e.localizedMessage
    }
}

fun findMinMaxEastingNorthingAndroid(holeDetailData: List<ResponseHoleDetailData>): NorthEastCoordinateModel {
    var minEasting: Float
    var maxEasting: Float
    var minNorthing: Float
    var maxNorthing: Float
    return if (!Constants.isListEmpty(holeDetailData)) {
        minEasting = holeDetailData[0].y.toString().toFloat()
        maxEasting = holeDetailData[0].y.toString().toFloat()
        minNorthing = holeDetailData[0].x.toString().toFloat()
        maxNorthing = holeDetailData[0].x.toString().toFloat()
        var tempNorthing: Float
        var tempEasting: Float
        var i = 1
        val len = holeDetailData.size
        while (i < len) {
            holeDetailData[i].setX(holeDetailData[i].x.toString().toFloat())
            tempNorthing = holeDetailData[i].x.toString().toFloat()
            minNorthing = Math.min(tempNorthing, minNorthing)
            maxNorthing = Math.max(tempNorthing, maxNorthing)
            holeDetailData[i].setY(holeDetailData[i].y.toString().toFloat())
            tempEasting = holeDetailData[i].y.toString().toFloat()
            minEasting = tempEasting.coerceAtMost(minEasting)
            maxEasting = tempEasting.coerceAtLeast(maxEasting)
            i++
        }
        minEasting -= 10
        maxEasting += 10
        minNorthing -= 10
        maxNorthing += 10
        NorthEastCoordinateModel(
            minEasting.toDouble(),
            maxEasting.toDouble(), minNorthing.toDouble(), maxNorthing.toDouble()
        )
    } else {
        NorthEastCoordinateModel()
    }
}

fun _3dMapCoordinatesAndroid(
    northing: Double,
    easting: Double,
    holeDetailData: List<Response3DTable4HoleChargingDataModel>,
): List<ReferenceCoordinateModel> {
    val referenceCoordinateModelList: MutableList<ReferenceCoordinateModel> = ArrayList()
    try {
        val width: Long = 537
        val height: Long = 527
        var x: Double
        var y: Double
        if (northing == 0.0 || easting == 20.0) {
            for (i in holeDetailData.indices) {
                x = if (holeDetailData[i].topX.toDouble() == 0.0) {
                    20.0
                } else {
                    abs(holeDetailData[i].topX.toDouble() + 30)
                }
                y = abs(holeDetailData[i].topY.toDouble())
                referenceCoordinateModelList.add(
                    ReferenceCoordinateModel(
                        holeDetailData[i].rowNo,
                        holeDetailData[i].holeNo,
                        x,
                        y
                    )
                )
            }
        } else {
            var holeNo = 0
            val arrMinMaxEastingNorthing = findMinMaxEastingNorthingFor3dMapAndroid(holeDetailData)
            //for Easting- No. of pixels in meter
            val scaleFactorEasting =
                (width / (arrMinMaxEastingNorthing.maxEasting - arrMinMaxEastingNorthing.minEasting)).toFloat()
            //for Northing- No. of pixels in meter
            val scaleFactorNorthing =
                (height / (arrMinMaxEastingNorthing.maxNorthing - arrMinMaxEastingNorthing.minNorthing)).toFloat()

            // to find the scale that will fit the canvas get the min scale to fit height or width
            val scaleMinEastNorth = Math.min(scaleFactorEasting, scaleFactorNorthing)
            for (i in holeDetailData.indices) {
                x =
                    (holeDetailData[i].topY.toDouble() - arrMinMaxEastingNorthing.minEasting) * scaleFactorEasting
                y =
                    (holeDetailData[i].topX.toDouble() - arrMinMaxEastingNorthing.minNorthing) * scaleFactorNorthing
                y = height - y
                holeNo += 1
                referenceCoordinateModelList.add(
                    ReferenceCoordinateModel(
                        holeDetailData[i].rowNo,
                        holeDetailData[i].holeNo,
                        x,
                        y
                    )
                )
            }
        }

        Log.e("referenceCoordinateModelList : ", Gson().toJson(referenceCoordinateModelList))
    } catch (e: Exception) {
        e.localizedMessage
    }
    return referenceCoordinateModelList
}

fun findMinMaxEastingNorthingFor3dMapAndroid(holeDetailData: List<Response3DTable4HoleChargingDataModel>): NorthEastCoordinateModel {
    var minEasting: Float
    var maxEasting: Float
    var minNorthing: Float
    var maxNorthing: Float
    return if (!Constants.isListEmpty(holeDetailData)) {
        minEasting = holeDetailData[0].topY.toString().toFloat()
        maxEasting = holeDetailData[0].topY.toString().toFloat()
        minNorthing = holeDetailData[0].topX.toString().toFloat()
        maxNorthing = holeDetailData[0].topX.toString().toFloat()
        var tempNorthing: Float
        var tempEasting: Float
        var i = 1
        val len = holeDetailData.size
        while (i < len) {
            holeDetailData[i].topX = holeDetailData[i].topX.toString()
            tempNorthing = holeDetailData[i].topX.toString().toFloat()
            minNorthing = Math.min(tempNorthing, minNorthing)
            maxNorthing = Math.max(tempNorthing, maxNorthing)
            holeDetailData[i].topY = holeDetailData[i].topY.toString()
            tempEasting = holeDetailData[i].topY.toString().toFloat()
            minEasting = Math.min(tempEasting, minEasting)
            maxEasting = Math.max(tempEasting, maxEasting)
            i++
        }
        minEasting -= 10
        maxEasting += 10
        minNorthing -= 10
        maxNorthing += 10
        NorthEastCoordinateModel(
            minEasting.toDouble(),
            maxEasting.toDouble(), minNorthing.toDouble(), maxNorthing.toDouble()
        )
    } else {
        NorthEastCoordinateModel()
    }
}

fun getCoOrdinateOfHole(topX: String, topY: String): String {
    var x = topX.toDouble()
    var y = topY.toDouble()
    try {
        val width: Long = 575
        val height: Long = 758
        val arrMinMaxEastingNorthing =
            findSingleMinMaxEastingNorthingFor3dMapAndroid(topX, topY)
        //for Easting- No. of pixels in meter
        val scaleFactorEasting =
            (width / (arrMinMaxEastingNorthing.maxEasting - arrMinMaxEastingNorthing.minEasting)).toFloat()
        //for Northing- No. of pixels in meter
        val scaleFactorNorthing =
            (height / (arrMinMaxEastingNorthing.maxNorthing - arrMinMaxEastingNorthing.minNorthing)).toFloat()

        // to find the scale that will fit the canvas get the min scale to fit height or width
        val scaleMinEastNorth = Math.min(scaleFactorEasting, scaleFactorNorthing)
        x = (topY.toDouble() - arrMinMaxEastingNorthing.minEasting) * scaleFactorEasting
        y = (topX.toDouble() - arrMinMaxEastingNorthing.minNorthing) * scaleFactorNorthing
        y = height - y
    } catch (e: Exception) {
        e.localizedMessage
    }
    return String.format("%s,%s", x, y)
}

fun findSingleMinMaxEastingNorthingFor3dMapAndroid(topX: String, topY: String): NorthEastCoordinateModel {
    var minEasting: Float = topY.toString().toFloat()
    var maxEasting: Float = topY.toString().toFloat()
    var minNorthing: Float = topX.toString().toFloat()
    var maxNorthing: Float = topX.toString().toFloat()

//    topX = topX.toString()
    val tempNorthing: Float = topX.toString().toFloat()
    minNorthing = tempNorthing.coerceAtMost(minNorthing)
    maxNorthing = tempNorthing.coerceAtLeast(maxNorthing)
//    topY = topY.toString()
    val tempEasting: Float = topY.toString().toFloat()
    minEasting = Math.min(tempEasting, minEasting)
    maxEasting = Math.max(tempEasting, maxEasting)
    minEasting -= 10
    maxEasting += 10
    minNorthing -= 10
    maxNorthing += 10
    return NorthEastCoordinateModel(
        minEasting.toDouble(),
        maxEasting.toDouble(), minNorthing.toDouble(), maxNorthing.toDouble()
    )
}

fun mapCorrdinates(context: Context, holeDetailDataList : List<Response3DTable4HoleChargingDataModel>) : List<Response3DTable4HoleChargingDataModel> {
    var tempHoleDetailDataList = holeDetailDataList.toMutableList();
    val width: Long = 537
    val height: Long = 527
    var holeNo = 0;

    val listLiveUpdate = MutableLiveData<List<Response3DTable4HoleChargingDataModel>>()

    val northEastConstants = findMinMaxEastingNorthing(holeDetailDataList, listLiveUpdate)

    listLiveUpdate.observe(context as LifecycleOwner) {
        tempHoleDetailDataList = it as MutableList<Response3DTable4HoleChargingDataModel>
    }

    val scaleFactorEasting =
        (width / (northEastConstants.maxEasting - northEastConstants.minEasting)).toFloat()

    val scaleFactorNorthing =
        (height / (northEastConstants.maxNorthing - northEastConstants.minNorthing)).toFloat()

    val scaleMinEastNorth = scaleFactorEasting.coerceAtMost(scaleFactorNorthing);

    for (i in 1 until holeDetailDataList.size) {
        var y = (holeDetailDataList[i].easting.toDouble() - northEastConstants.maxEasting) * scaleFactorEasting
        val x = (holeDetailDataList[i].northing.toDouble() - northEastConstants.maxNorthing) * scaleFactorNorthing

        y = height - y;
        holeNo += 1;

        val model = holeDetailDataList[i]
        model.easting = y.toString()
        model.northing = x.toString()
        tempHoleDetailDataList[i] = model
    }

    return tempHoleDetailDataList

}

fun findMinMaxEastingNorthing(
    holeDetailDataList: List<Response3DTable4HoleChargingDataModel>,
    listLiveUpdate: MutableLiveData<List<Response3DTable4HoleChargingDataModel>>
): NorthEastCoordinateModel {
    var minEasting: Float = holeDetailDataList.first().topY.toString().toFloat()
    var maxEasting: Float = holeDetailDataList.first().topY.toString().toFloat()
    var minNorthing: Float = holeDetailDataList.first().topX.toString().toFloat()
    var maxNorthing: Float = holeDetailDataList.first().topX.toString().toFloat()

    var tempNorthing = 0f
    var tempEasting = 0f

    for (i in holeDetailDataList.indices) {
        holeDetailDataList[i].easting = holeDetailDataList[i].topY.toString()
        tempEasting = holeDetailDataList[i].topY.toString().toFloat()
        minEasting = tempEasting.coerceAtMost(minEasting)
        maxEasting = tempEasting.coerceAtMost(maxEasting)

        holeDetailDataList[i].northing = holeDetailDataList[i].topX.toString()
        tempNorthing = holeDetailDataList[i].topX.toString().toFloat()
        minNorthing = tempNorthing.coerceAtMost(minNorthing)
        maxNorthing = tempNorthing.coerceAtMost(maxNorthing)
    }

    minEasting -= 10;
    maxEasting += 10;
    minNorthing -= 10;
    maxNorthing += 10;

    listLiveUpdate.postValue(holeDetailDataList)

    return NorthEastCoordinateModel(minEasting.toDouble(), maxEasting.toDouble(), minNorthing.toDouble(), maxNorthing.toDouble())
}

// Pilot 3d Coordinates
fun pilotMapCoordinates(context: Context, holeDetailDataList : List<Response3DTable16PilotDataModel>) : List<Response3DTable16PilotDataModel> {
    var tempHoleDetailDataList = holeDetailDataList.toMutableList();
    val width: Long = 537
    val height: Long = 527
    var holeNo = 0;

    val listLiveUpdate = MutableLiveData<List<Response3DTable16PilotDataModel>>()

    val northEastConstants = findMinMaxEastingNorthingForPilot(holeDetailDataList, listLiveUpdate)

    listLiveUpdate.observe(context as LifecycleOwner) {
        tempHoleDetailDataList = it as MutableList<Response3DTable16PilotDataModel>
    }

    val scaleFactorEasting =
        (width / (northEastConstants.maxEasting - northEastConstants.minEasting)).toFloat()

    val scaleFactorNorthing =
        (height / (northEastConstants.maxNorthing - northEastConstants.minNorthing)).toFloat()

    val scaleMinEastNorth = scaleFactorEasting.coerceAtMost(scaleFactorNorthing);

    for (i in 1 until holeDetailDataList.size) {
        var y = (holeDetailDataList[i].easting.toDouble() - northEastConstants.maxEasting) * scaleFactorEasting
        val x = (holeDetailDataList[i].northing.toDouble() - northEastConstants.maxNorthing) * scaleFactorNorthing

        y = height - y;
        holeNo += 1;

        val model = holeDetailDataList[i]
        model.easting = y.toString()
        model.northing = x.toString()
        tempHoleDetailDataList[i] = model
    }

    return tempHoleDetailDataList

}

fun findMinMaxEastingNorthingForPilot(
    holeDetailDataList: List<Response3DTable16PilotDataModel>,
    listLiveUpdate: MutableLiveData<List<Response3DTable16PilotDataModel>>
): NorthEastCoordinateModel {
    var minEasting: Float = holeDetailDataList.first().topY.toString().toFloat()
    var maxEasting: Float = holeDetailDataList.first().topY.toString().toFloat()
    var minNorthing: Float = holeDetailDataList.first().topX.toString().toFloat()
    var maxNorthing: Float = holeDetailDataList.first().topX.toString().toFloat()

    var tempNorthing = 0f
    var tempEasting = 0f

    for (i in holeDetailDataList.indices) {
        holeDetailDataList[i].easting = holeDetailDataList[i].topY.toString()
        tempEasting = holeDetailDataList[i].topY.toString().toFloat()
        minEasting = tempEasting.coerceAtMost(minEasting)
        maxEasting = tempEasting.coerceAtMost(maxEasting)

        holeDetailDataList[i].northing = holeDetailDataList[i].topX.toString()
        tempNorthing = holeDetailDataList[i].topX.toString().toFloat()
        minNorthing = tempNorthing.coerceAtMost(minNorthing)
        maxNorthing = tempNorthing.coerceAtMost(maxNorthing)
    }

    minEasting -= 10;
    maxEasting += 10;
    minNorthing -= 10;
    maxNorthing += 10;

    listLiveUpdate.postValue(holeDetailDataList)

    return NorthEastCoordinateModel(minEasting.toDouble(), maxEasting.toDouble(), minNorthing.toDouble(), maxNorthing.toDouble())
}


// Pre Split 3d Coordinates
fun preSplitMapCoordinates(context: Context, holeDetailDataList : List<Response3DTable18PreSpilitDataModel>) : List<HoleDetailItem> {
    val tempHoleDetailDataList = holeDetailDataList.toMutableList();
    if (!Constants.isListEmpty(holeDetailDataList)) {
        var holeDetailData = holeDetailDataList.first().holeDetail
        val width: Long = 537
        val height: Long = 527
        var holeNo = 0;

        val listLiveUpdate = MutableLiveData<List<HoleDetailItem>>()

        val northEastConstants = findMinMaxEastingNorthingForPreSplit(holeDetailData, listLiveUpdate)

        listLiveUpdate.observe(context as LifecycleOwner) {
            holeDetailData = it as MutableList<HoleDetailItem>
        }

        val scaleFactorEasting =
            (width / (northEastConstants.maxEasting - northEastConstants.minEasting)).toFloat()

        val scaleFactorNorthing =
            (height / (northEastConstants.maxNorthing - northEastConstants.minNorthing)).toFloat()

        val scaleMinEastNorth = scaleFactorEasting.coerceAtMost(scaleFactorNorthing);

        for (i in 1 until holeDetailData.size) {
            var y = (holeDetailData[i].easting.toDouble() - northEastConstants.maxEasting) * scaleFactorEasting
            val x = (holeDetailData[i].northing.toDouble() - northEastConstants.maxNorthing) * scaleFactorNorthing

            y = height - y;
            holeNo += 1;

            val model = holeDetailData[i]
            model.easting = y.toString()
            model.northing = x.toString()
            holeDetailData[i] = model
        }

        val resp = holeDetailDataList.first()
        resp.holeDetail = holeDetailData

        tempHoleDetailDataList[0] = resp

    }
    return holeDetailDataList.first().holeDetail
}

fun findMinMaxEastingNorthingForPreSplit(
    holeDetailDataList: List<HoleDetailItem>,
    listLiveUpdate: MutableLiveData<List<HoleDetailItem>>
): NorthEastCoordinateModel {
    var minEasting: Float = holeDetailDataList.first().topEasting.toString().toFloat()
    var maxEasting: Float = holeDetailDataList.first().topEasting.toString().toFloat()
    var minNorthing: Float = holeDetailDataList.first().topNorthing.toString().toFloat()
    var maxNorthing: Float = holeDetailDataList.first().topNorthing.toString().toFloat()

    var tempNorthing = 0f
    var tempEasting = 0f

    for (i in holeDetailDataList.indices) {
        holeDetailDataList[i].easting = holeDetailDataList[i].topEasting.toString()
        tempEasting = holeDetailDataList[i].topEasting.toString().toFloat()
        minEasting = tempEasting.coerceAtMost(minEasting)
        maxEasting = tempEasting.coerceAtMost(maxEasting)

        holeDetailDataList[i].northing = holeDetailDataList[i].topNorthing.toString()
        tempNorthing = holeDetailDataList[i].topNorthing.toString().toFloat()
        minNorthing = tempNorthing.coerceAtMost(minNorthing)
        maxNorthing = tempNorthing.coerceAtMost(maxNorthing)
    }

    minEasting -= 10;
    maxEasting += 10;
    minNorthing -= 10;
    maxNorthing += 10;

    listLiveUpdate.postValue(holeDetailDataList)

    return NorthEastCoordinateModel(minEasting.toDouble(), maxEasting.toDouble(), minNorthing.toDouble(), maxNorthing.toDouble())
}
