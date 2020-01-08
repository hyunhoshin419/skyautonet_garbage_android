package com.skyautonet.garbage

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.crashlytics.android.Crashlytics
import com.g00fy2.versioncompare.Version
import com.skyautonet.garbage.api.checkUpdate
import com.skyautonet.garbage.api.model.CheckUpdateResponse
import com.skyautonet.garbage.api.model.UpdateInfo
import com.skyautonet.garbage.api.model.UpdatePolicy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SplashActivity : AppCompatActivity() {
    val PACKAGE_NAME = "com.skyautonet.garbage"
    private val disposables = CompositeDisposable()

    override fun onStart() {
        super.onStart()
        if(!isTestVersion()){
            requestVersion()
        }
    }

    override fun onStop() {
        disposables.clear()
        super.onStop()
    }
    private fun requestVersion() {
        val disposable = checkUpdate()
            .retry(1)
            .map (CheckUpdateResponse::result)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::checkUpdateVersion, this::handleOnError)

        disposables.add(disposable)
    }

    private fun handleOnError(throwable: Throwable) {

        Crashlytics.log(Log.INFO, "SPLASH", "버전 가져오기 실패 : " + throwable.message)
        Crashlytics.logException(throwable)
        goMainActivity()
    }

    private fun checkUpdateVersion(updateInfo: UpdateInfo) {

        Timber.i("최신 버전 : $updateInfo")

        if (updateInfo.isActivated && isHigher(updateInfo.version)) {

            if (updateInfo.policy === UpdatePolicy.NECESSARY.value) {
                finishAndGoUpdate()
                return
            } else if (updateInfo.policy === UpdatePolicy.USER_DECISION.value) {
                //사용자 확인창
                showSelectiveUpdateDialog()
                return
            }
        }
        goMainActivity()
    }

    private fun showSelectiveUpdateDialog() {

        MaterialDialog.Builder(this)
            .title("앱 업데이트")
            .content("최신 버전이 업데이트 되었습니다.\n업데이트 하시겠습니까?")
            .positiveText("업데이트")
            .negativeText(android.R.string.cancel)
            .onPositive { _, _ -> finishAndGoUpdate() }
            .onNegative { _, _ -> goMainActivity() }
            .show()
    }

    private fun finishAndGoUpdate() {

        finish()
        Toast.makeText(applicationContext, "업데이트를 위해 구글 플레이로 이동합니다.", Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_VIEW)

        //리팩토링중 주소 안바뀌게 조심...
        intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$PACKAGE_NAME")
        startActivity(intent)
    }



    private fun isHigher(version: String): Boolean {

        val current = getVersionName()
        return if (current.isEmpty()) false else Version(version).isHigherThan(current)
    }

    private fun isTestVersion() : Boolean {
        val version = getVersionName()
        if(version.isEmpty()) return false
        return version.matches("[0-9]+.[0-9]+.[0-9]+-.+".toRegex())
    }

    private fun getVersionName(): String {

        try {
            val pi = packageManager.getPackageInfo(packageName, 0)
            return pi.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }




    private fun goMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}