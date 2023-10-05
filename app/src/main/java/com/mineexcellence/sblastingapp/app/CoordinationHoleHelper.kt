package com.mineexcellence.sblastingapp.app

import android.util.Log
import com.google.gson.Gson
import com.mineexcellence.sblastingapp.api.apis.response.ResponseHoleDetailData
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel
import com.mineexcellence.sblastingapp.helper.Constants
import com.mineexcellence.sblastingapp.ui.models.NorthEastCoordinateModel
import com.mineexcellence.sblastingapp.ui.models.ReferenceCoordinateModel
import kotlin.math.abs

fun mapCoordinatesAndroid(
    northing: Double,
    easting: Double,
    holeDetailData: List<ResponseHoleDetailData>
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
    holeDetailData: List<Response3DTable4HoleChargingDataModel>
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
        val width: Long = 537
        val height: Long = 527
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