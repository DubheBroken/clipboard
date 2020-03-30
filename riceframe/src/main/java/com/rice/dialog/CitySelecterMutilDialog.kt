//package com.rice.dialog
//
//import android.app.Activity
//import android.app.Dialog
//import android.graphics.drawable.ColorDrawable
//import android.view.Gravity
//import android.view.View
//import android.widget.FrameLayout
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.rice.adapter.CityMutiSelectAdapter
//import com.rice.adapter.ProviceMutiSelectAdapter
//import com.rice.model.CityModel
//import com.rice.riceframe.R
//import com.rice.tool.ToastUtil
//import kotlinx.android.synthetic.main.city_mutl_select_dialog.*
//import kotlinx.android.synthetic.main.city_mutl_select_dialog.textbtn_cancel
//import kotlinx.android.synthetic.main.city_mutl_select_dialog.textbtn_ok
//import java.util.*
//
///**
// * 城市多选选择器
// */
//class CitySelecterMutilDialog(var activity: Activity) : Dialog(activity, R.style.BottomDialog) {
//
//    lateinit var proviceAdapter: ProviceMutiSelectAdapter
//    lateinit var cityAdapter: CityMutiSelectAdapter
//    var listProvices: MutableList<CityModel.Data> = ArrayList()
//    var listCitys: MutableList<CityModel.Data.City> = ArrayList()
//    var onOkClickListener: OnOkClickListener? = null
//    var onCancelClickListener: OnCancelClickListener? = null
//
//    interface OnOkClickListener {
//        fun onOkClick(ids: String)
//    }
//
//    interface OnCancelClickListener {
//        fun onCancelClick()
//    }
//
//    var view: View
//
//    init {
//        view = View.inflate(context, R.layout.city_mutl_select_dialog, null)
//        setContentView(view)
//        val window = this.window
//        window!!.setBackgroundDrawable(ColorDrawable(0))
//        window.setGravity(Gravity.BOTTOM)
//        val lp = window.attributes
//        //设置宽
//        lp.width = FrameLayout.LayoutParams.MATCH_PARENT
//        //设置高
//        lp.height = context.resources.getDimensionPixelOffset(R.dimen.dp_400)
//        window.attributes = lp
//        window.setBackgroundDrawable(ColorDrawable(0))
//        initView()
//        setCanceledOnTouchOutside(true)
//        setCancelable(true)
//    }
//
//    fun initView() {
//        listProvices = AreaSelecterDialog.getArea1Items()
//        proviceAdapter = ProviceMutiSelectAdapter(activity, listProvices)
//        cityAdapter = CityMutiSelectAdapter(activity, listCitys)
//        proviceAdapter.setOnItemClickListener { adapter, view, position ->
//            for (item in listProvices) {
//                item.isChecked = listProvices.indexOf(item) == position
//            }
//            listCitys.clear()
//            listCitys.addAll(listProvices[position].city)
//            proviceAdapter.notifyDataSetChanged()
//            cityAdapter.notifyDataSetChanged()
//        }
//        cityAdapter.setOnItemClickListener { adapter, view, position ->
//            listCitys[position].isChecked = !listCitys[position].isChecked
//            proviceAdapter.getCheckedItem()?.selectNum = cityAdapter.getCheckedIds().size
//            proviceAdapter.notifyDataSetChanged()
//            cityAdapter.notifyDataSetChanged()
//        }
//        recycler1.layoutManager = LinearLayoutManager(context)
//        recycler2.layoutManager = LinearLayoutManager(context)
//        recycler1.adapter = proviceAdapter
//        recycler2.adapter = cityAdapter
//        textbtn_cancel.setOnClickListener {
//            if (onCancelClickListener != null) {
//                onCancelClickListener!!.onCancelClick()
//            }
//            dismiss()
//        }
//        textbtn_ok.setOnClickListener {
//            if (onOkClickListener != null) {
//                dismiss()
//                var ids = ""
//                for (item1 in listProvices) {
//                    for (item in item1.city) {
//                        if (item.isChecked) {
//                            ids += item.id + ","
//                        }
//                    }
//                }
//                if (ids.length > 2) {
//                    if (ids.endsWith(",")) {
//                        ids = ids.substring(0, ids.lastIndex)
//                    }
//                } else {
//                    ToastUtil.showShort("请选择至少一个城市")
//                    return@setOnClickListener
//                }
//                onOkClickListener!!.onOkClick(ids)
//            }
//        }
//    }
//
//}
