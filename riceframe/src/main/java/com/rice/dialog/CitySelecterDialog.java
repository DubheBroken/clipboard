package com.rice.dialog;

import android.app.Dialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.rice.model.BankCityModel;
import com.rice.riceframe.R;
import com.rice.tool.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 省市二级联动选择器
 */
public class CitySelecterDialog {

    private static OptionsPickerView pvOptions;
    private static OnSelectedListener onSelectedListener;
    private static List<BankCityModel.Province> area1Items;
    private static List<List<BankCityModel.Province.City>> area2Items;
    public static boolean isComplete = false;

    public static List<BankCityModel.Province> getArea1Items() {
        return area1Items;
    }

    /**
     * 把服务器拿到的数据塞进去
     * 不执行这个方法是不会出选择框的
     */
    public static void setData(BankCityModel areaModel) {
        area1Items = areaModel.getLists();
        area2Items = new ArrayList<>();
//        for (AreaModel item : area1Items) {
//            if (item.get().equals("四川")) {
//                sichuanIndex = area1Items.indexOf(item);
//            }
//        }
//        AreaModel sichuan = area1Items.get(sichuanIndex);
//        area1Items.remove(sichuan);
//        area1Items.add(0, sichuan);
        for (BankCityModel.Province item : area1Items) {
            List<BankCityModel.Province.City> cityList = new ArrayList<>();//该省的城市列表（第二级）
            //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
            //                for (AreaModel.City.District item3 : item2.getDistrictList()) {
            //                    if (item2.getDistrictList() == null || item2.getDistrictList().size() == 0) {
            //                        AreaModel.City.District childBean = new AreaModel.City.District();
            //                        childBean.setName(item3.getName());
            //                    }
            //                }
            cityList.addAll(item.getList());
//            cityList.addAll(item.getCityList());
            /**
             * 添加城市数据
             */
            area2Items.add(cityList);
        }
        isComplete = true;
    }

    public static void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        CitySelecterDialog.onSelectedListener = onSelectedListener;
    }

    public interface OnSelectedListener {
        void onSelected(String provinceId, String cityId,
                        String provinceName, String cityName);
    }

    public static void getInstance(AppCompatActivity activity) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> new Thread(() -> {
            if (isComplete) {
                //等待数据加载完毕
                emitter.onComplete();
            } else {
                ToastUtil.showShort("地区信息初始化中···");
            }
        }).start()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                activity.runOnUiThread(() -> {
                    pvOptions = new OptionsPickerBuilder(activity, (options1, options2, options3, v) -> {
                        //返回的分别是三个级别的选中位置
//                        String str = area1Items.get(options1).getPickerViewText()
//                                + "-"
//                                + area2Items.get(options1).get(options2).getPickerViewText();
//                                + "-"
//                                + area3Items.get(options1).get(options2).get(options3).getPickerViewText();
                        if (onSelectedListener != null) {
                            onSelectedListener.onSelected(
                                    area1Items.get(options1).getId() + "",
                                    area2Items.get(options1).get(options2).getId() + "",
                                    area1Items.get(options1).getName() + "",
                                    area2Items.get(options1).get(options2).getName() + "");
                        }
                    })
                            .setSubmitText("确定")//确定按钮文字
                            .setCancelText("取消")//取消按钮文字
                            .setTitleText("")//标题
                            .setSubCalSize(20)//确定和取消文字大小
                            .setSubmitColor(ContextCompat.getColor(activity, R.color.black3))//确定按钮文字颜色
                            .setCancelColor(ContextCompat.getColor(activity, R.color.black3))//取消按钮文字颜色
                            .setTitleBgColor(0xFFDCDCDC)//标题背景颜色 Night mode
                            .setBgColor(0xFFF5F5F5)//滚轮背景颜色 Night mode
                            .setContentTextSize(20)//滚轮文字大小
                            .setLabels("", "", "")//设置选择的三级单位
                            .setCyclic(false, false, false)//循环与否
                            .setSelectOptions(0, 0)  //设置默认选中项
                            .setOutSideCancelable(false)//点击外部dismiss default true
                            .setDecorView(activity.getWindow().getDecorView().findViewById(android.R.id.content))
                            .isDialog(true)//是否显示为对话框样式
                            .build();
                    pvOptions.setPicker(area1Items, area2Items);
                    pvOptions.setKeyBackCancelable(true);
                    Dialog mDialog = pvOptions.getDialog();
                    mDialog.setCancelable(true);
                    if (mDialog != null) {

                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                Gravity.BOTTOM);

                        params.leftMargin = 0;
                        params.rightMargin = 0;
                        pvOptions.getDialogContainerLayout().setLayoutParams(params);

                        Window dialogWindow = mDialog.getWindow();
                        if (dialogWindow != null) {
                            dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                            dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                        }
                    }
                    pvOptions.show();
                });
            }
        });
    }

}
