package com.rice.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.rice.base.RiceBaseActivity
import com.rice.dialog.OkCancelDialog
import com.rice.riceframe.R
import com.rice.tool.ToastUtil
import kotlinx.android.synthetic.main.activity_test_scan.*

/**
 * 扫描二维码界面
 */
@SuppressLint("Registered")
class ScanQRActivity : RiceBaseActivity(), QRCodeView.Delegate {

    override fun getLayoutId(): Int {
        return R.layout.activity_test_scan
    }

    override fun initView() {
        initDialog()
    }

    override fun getIntentData() {

    }

    override fun clear() {

    }

    val REQUEST_PERMISSIONS = 0x000526

    lateinit var okCancelDialog: OkCancelDialog

    private fun initDialog() {
        okCancelDialog = OkCancelDialog(this)
        okCancelDialog.setInfo("您必须给予相机权限才能正常使用该功能")
        okCancelDialog.onOkClickListener = object : OkCancelDialog.OnOkClickListener {
            override fun onOkClick() {
                initPermission(true)
            }
        }
        okCancelDialog.onCancelClickListener = object : OkCancelDialog.OnCancelClickListener {
            override fun onCancelClick() {
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initPermission(false)
    }

    override fun onStop() {
        zxingview!!.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        zxingview!!.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }


    /**
     * 初始化权限
     * 适配6.0+手机的运行时权限
     *
     * @param forceRequest 强制申请权限
     */
    fun initPermission(forceRequest: Boolean) {
        val TAG = "---申请权限---"
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.VIBRATE)
        //检查权限
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            //判断权限是否被拒绝过
            if (forceRequest) {
                //强制申请
                Log.d(TAG, "强制申请")
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.VIBRATE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //用户曾拒绝过权限
                Log.d(TAG, "用户曾拒绝过权限")
                runOnUiThread {
                    if (!okCancelDialog.isShowing) {
                        okCancelDialog.show()
                    }
                }
            } else {
                //用户没有拒绝过，首次申请
                Log.d(TAG, "首次申请")
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
            }
        } else {
            //已有权限
            zxingview!!.setDelegate(this)
            zxingview!!.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
            //        zxingview.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
            zxingview!!.startSpotAndShowRect() // 显示扫描框，并开始识别
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        initPermission(false)
    }

    /**
     * 振动
     */
    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(100)
    }

    /**
     * 扫描结果回调
     */
    override fun onScanQRCodeSuccess(result: String) {
        //        Log.i(TAG, "result:" + result);
        ToastUtil.showShort("扫描结果为：$result")
        vibrate()

//        val bundle = Bundle()
//        bundle.putString("order_no", result)
//        bundle.putInt("from", CheckoutCounterActivity.FROM_VIP)
//        Common.openActivity(mContext, CheckoutCounterActivity::class.java, bundle, 20, R.anim.push_right_in, R.anim.push_left_out)
//        finish()
        //        zxingview.startSpot(); // 开始识别
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        var tipText = zxingview!!.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zxingview!!.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                zxingview!!.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        ToastUtil.showShort("打开相机失败")
        Log.e(TAG, "打开相机出错")
    }

    //    public void onClick(View v) {
    //        switch (v.getId()) {
    //            case R.id.start_preview:
    //                zxingview.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
    //                break;
    //            case R.id.stop_preview:
    //                zxingview.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
    //                break;
    //            case R.id.start_spot:
    //                zxingview.startSpot(); // 开始识别
    //                break;
    //            case R.id.stop_spot:
    //                zxingview.stopSpot(); // 停止识别
    //                break;
    //            case R.id.start_spot_showrect:
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并且开始识别
    //                break;
    //            case R.id.stop_spot_hiddenrect:
    //                zxingview.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
    //                break;
    //            case R.id.show_scan_rect:
    //                zxingview.showScanRect(); // 显示扫描框
    //                break;
    //            case R.id.hidden_scan_rect:
    //                zxingview.hiddenScanRect(); // 隐藏扫描框
    //                break;
    //            case R.id.decode_scan_box_area:
    //                zxingview.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
    //                break;
    //            case R.id.decode_full_screen_area:
    //                zxingview.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
    //                break;
    //            case R.id.open_flashlight:
    //                zxingview.openFlashlight(); // 打开闪光灯
    //                break;
    //            case R.id.close_flashlight:
    //                zxingview.closeFlashlight(); // 关闭闪光灯
    //                break;
    //            case R.id.scan_one_dimension:
    //                zxingview.changeToScanBarcodeStyle(); // 切换成扫描条码样式
    //                zxingview.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    //                break;
    //            case R.id.scan_two_dimension:
    //                zxingview.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
    //                zxingview.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    //                break;
    //            case R.id.scan_qr_code:
    //                zxingview.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
    //                zxingview.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    //                break;
    //            case R.id.scan_code128:
    //                zxingview.changeToScanBarcodeStyle(); // 切换成扫描条码样式
    //                zxingview.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    //                break;
    //            case R.id.scan_ean13:
    //                zxingview.changeToScanBarcodeStyle(); // 切换成扫描条码样式
    //                zxingview.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    //                break;
    //            case R.id.scan_high_frequency:
    //                zxingview.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
    //                zxingview.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、UPC_A、EAN_13、CODE_128
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    //                break;
    //            case R.id.scan_all:
    //                zxingview.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
    //                zxingview.setType(BarcodeType.ALL, null); // 识别所有类型的码
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    //                break;
    //            case R.id.scan_custom:
    //                zxingview.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
    //
    //                Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
    //                List<BarcodeFormat> formatList = new ArrayList<>();
    //                formatList.add(BarcodeFormat.QR_CODE);
    //                formatList.add(BarcodeFormat.UPC_A);
    //                formatList.add(BarcodeFormat.EAN_13);
    //                formatList.add(BarcodeFormat.CODE_128);
    //                hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formatList); // 可能的编码格式
    //                hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
    //                hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 编码字符集
    //                zxingview.setType(BarcodeType.CUSTOM, hintMap); // 自定义识别的类型
    //
    //                zxingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
    //                break;
    //            case R.id.choose_qrcde_from_gallery:
    //                /*
    //                从相册选取二维码图片，这里为了方便演示，使用的是
    //                https://github.com/bingoogolapple/BGAPhotoPicker-Android
    //                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
    //                 */
    //                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
    //                        .cameraFileDir(null)
    //                        .maxChooseCount(1)
    //                        .selectedPhotos(null)
    //                        .pauseOnScroll(false)
    //                        .build();
    //                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
    //                break;
    //        }
    //    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        zxingview!!.startSpotAndShowRect() // 显示扫描框，并开始识别

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            //            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
            //             本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
            //            zxingview.decodeQRCode(picturePath);

            /*
            没有用到 QRCodeView 时可以调用 QRCodeDecoder 的 syncDecodeQRCode 方法

            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
            .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
            //            new AsyncTask<Void, Void, String>() {
            //                @Override
            //                protected String doInBackground(Void... params) {
            //                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
            //                }
            //
            //                @Override
            //                protected void onPostExecute(String result) {
            //                    if (TextUtils.isEmpty(result)) {
            //                        Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
            //                    } else {
            //                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
            //                    }
            //                }
            //            }.execute();
        }
    }

    companion object {
        private val TAG = ScanQRActivity::class.java.simpleName
        private val REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666
    }


}