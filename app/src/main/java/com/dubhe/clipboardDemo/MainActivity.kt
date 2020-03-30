package com.dubhe.clipboardDemo

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dubhe.clipboardDemo.activity.FileManagerActivity
import com.dubhe.imageselector.Path.REQUEST_PERMISSIONS
import com.rice.base.RiceBaseActivity
import com.rice.dialog.OkCancelDialog
import com.rice.tool.ActivityUtils
import com.rice.view.RiceToolbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RiceBaseActivity() {

    private lateinit var permissionDialog: OkCancelDialog
    var hasPermission = false

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        btnOpenFileManager.setOnClickListener {
            ActivityUtils.openActivity(mContext, FileManagerActivity::class.java)
        }
        permissionDialog = OkCancelDialog(this)
        permissionDialog.setTitle("温馨提示")
        permissionDialog.setInfo("您必须授予权限后才能正常使用本软件。")
        permissionDialog.onOkClickListener = object : OkCancelDialog.OnOkClickListener {
            override fun onOkClick() {
                initPermission(true)
            }
        }
//        permissionDialog.show()
        initPermission(true)
    }

    /**
     * 初始化权限
     * 适配6.0+手机的运行时权限
     *
     * @param forceRequest 强制申请权限
     */
    fun initPermission(forceRequest: Boolean) {
        val TAG = "---申请权限---"
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        //检查权限
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //判断权限是否被拒绝过
            if (forceRequest) {
                //强制申请
                Log.d(TAG, "强制申请")
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                //用户曾拒绝过权限
                Log.d(TAG, "用户曾拒绝过权限")
                runOnUiThread { permissionDialog.show() }
            } else {
                //用户没有拒绝过，首次申请
                Log.d(TAG, "首次申请")
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
            }
        } else {
            //已有权限
            hasPermission = true
        }
    }

    /**
     * 请求权限回调
     *
     * @param requestCode  请求码
     * @param permissions  权限集
     * @param grantResults 请求结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> if (grantResults.size > 1) {
                var hasFaild = false
                //文件权限是否到手
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //                        hasFaild = false;
                } else {
                    hasFaild = true
                }
                hasPermission = !hasFaild
                if (hasFaild) {
                    permissionDialog.show()
                }
            } else {
                permissionDialog.show()
            }
        }
    }

    override fun getIntentData() {

    }

    override fun clear() {

    }

}
