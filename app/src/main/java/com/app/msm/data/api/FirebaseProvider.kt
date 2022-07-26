package com.app.msm.data.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseProvider {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    val msmReference: DatabaseReference by lazy { firebaseDatabase.getReference("msm") }
    val lampuStatusReference: DatabaseReference by lazy { firebaseDatabase.getReference("msm/lampu/status") }
    val blowerStatusReference: DatabaseReference by lazy { firebaseDatabase.getReference("msm/blower/status") }
    val kelembapanStatusReference: DatabaseReference by lazy { firebaseDatabase.getReference("msm/kelembapan/status") }
    val suhuStatusReference: DatabaseReference by lazy { firebaseDatabase.getReference("msm/suhu/status") }
}
