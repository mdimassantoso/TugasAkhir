package com.app.msm.data.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseProvider {

    object Path {
        const val CONTROLLING = "controlling"
        const val MONITORING = "monitoring"
        const val CONFIGURATION = "configuration"
        const val AUTO_CONFIG = "auto_configuration"
        const val CAMERA_SENSOR_URL = "camera"
        const val WATERING_CONTROL = "$CONTROLLING/penyiraman_air"
        const val LAMP_CONTROL = "$CONTROLLING/lampu"
        const val BLOWER_CONTROL = "$CONTROLLING/blower"
    }

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    val controllingReference: DatabaseReference by lazy {
        firebaseDatabase.getReference(Path.CONTROLLING)
    }

    val monitoringReference: DatabaseReference by lazy {
        firebaseDatabase.getReference(Path.MONITORING)
    }

    val configurationReference: DatabaseReference by lazy {
        firebaseDatabase.getReference(Path.CONFIGURATION)
    }

    val autoConfigReference: DatabaseReference by lazy {
        firebaseDatabase.getReference(Path.AUTO_CONFIG)
    }

    val cameraSensorUrlReference: DatabaseReference by lazy {
        firebaseDatabase.getReference(Path.CAMERA_SENSOR_URL)
    }

    val wateringControlReference: DatabaseReference by lazy {
        firebaseDatabase.getReference(Path.WATERING_CONTROL)
    }

    val lampControlReference: DatabaseReference by lazy {
        firebaseDatabase.getReference(Path.LAMP_CONTROL)
    }

    val blowerControlReference: DatabaseReference by lazy {
        firebaseDatabase.getReference(Path.BLOWER_CONTROL)
    }
}
