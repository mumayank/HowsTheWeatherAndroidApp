package com.mumayank.howstheweather

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mumayank.howstheweather.helpers.GooglePlayServicesHelper
import com.mumayank.howstheweather.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var googlePlayServicesHelper: GooglePlayServicesHelper

    companion object {
        private fun closeAppWithAlert(activity: AppCompatActivity) {
            AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.google_play_services_issue))
                .setMessage(activity.getString(R.string.google_play_service_issue_detail))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.exit)) { _, _ ->
                    activity.onBackPressed()
                }
        }

        private fun startMainActivity(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googlePlayServicesHelper = GooglePlayServicesHelper(
            activity = this,
            onSuccess = {
                startMainActivity(this)
            }, onFailure = {
                closeAppWithAlert(this)
            })

        googlePlayServicesHelper.checkIfAvailable()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        googlePlayServicesHelper.onActivityResult(requestCode)
        super.onActivityResult(requestCode, resultCode, data)
    }

}