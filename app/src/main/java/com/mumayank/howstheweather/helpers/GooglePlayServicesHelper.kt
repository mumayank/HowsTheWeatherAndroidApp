package com.mumayank.howstheweather.helpers

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.lang.ref.WeakReference

class GooglePlayServicesHelper(
    activity: AppCompatActivity?,
    private val onSuccess: (() -> Unit)?,
    private val onFailure: (() -> Unit)?
) {
    companion object {
        private const val REQUEST_CODE = 1237
    }

    private val activityWeakReference = WeakReference(activity)

    fun checkIfAvailable() {
        val availability = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(activityWeakReference.get())
        if (activityWeakReference.get() == null) {
            return
        }
        if (availability == ConnectionResult.SUCCESS) {
            onSuccess?.invoke()
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(
                activityWeakReference.get(),
                availability,
                REQUEST_CODE
            ) {
                onFailure?.invoke()
            }.show()
        }
    }

    fun onActivityResult(requestCode: Int) {
        if (activityWeakReference.get() == null) {
            return
        }

        if (requestCode == REQUEST_CODE) {
            checkIfAvailable()
        }
    }
}