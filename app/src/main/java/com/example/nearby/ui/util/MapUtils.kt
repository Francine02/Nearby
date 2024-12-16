package com.example.nearby.ui.util

import com.google.android.gms.maps.model.LatLng

fun findSouthwest(points: List<LatLng>): LatLng {
    if (points.isEmpty()) return LatLng(0.0, 0.0)

    var southwestPoint = points[0]

    for (point in points) {
        if (point.latitude < southwestPoint.latitude ||
            (point.latitude == southwestPoint.latitude && point.latitude < southwestPoint.latitude)
        ) {
            southwestPoint = point
        }
    }

    return southwestPoint
}

fun findNortheast(points: List<LatLng>): LatLng {
    if (points.isEmpty()) return LatLng(0.0, 0.0)

    var northeastPoint = points[0]

    for (point in points) {
        if (point.latitude > northeastPoint.latitude ||
            (point.latitude == northeastPoint.latitude && point.latitude > northeastPoint.latitude)
        ) {
            northeastPoint = point
        }
    }

    return northeastPoint
}