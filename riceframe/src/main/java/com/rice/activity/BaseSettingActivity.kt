package com.rice.activity

import android.content.res.TypedArray
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.rice.adapter.MineFragmentAdapter
import com.rice.base.RiceBaseActivity
import com.rice.model.MineFragmnetModel
import com.rice.riceframe.R
import com.rice.tool.ToastUtil
import kotlinx.android.synthetic.main.activity_smr_recycler_match.*
import kotlinx.android.synthetic.main.include_smr_recycler_match.*

/**
 * 设置界面模板
 */
class BaseSettingActivity : RiceBaseActivity() {

    private lateinit var mAdapter: MineFragmentAdapter
    private val mList = ArrayList<MineFragmnetModel>()
    var firstIds: ArrayList<Int> = ArrayList()//头部项ID
    var endIds: ArrayList<Int> = ArrayList()//尾部项ID

    override fun getLayoutId(): Int {
        return R.layout.activity_smr_recycler_match
    }

    override fun initView() {
        val stringArray = mContext.resources.getStringArray(R.array.setting_str)//设置项名称数组
        val iconArray = mContext.resources.getIntArray(R.array.setting_icon)//设置项图标数组
        if (stringArray.size != iconArray.size) {
            Log.e(this.toString(), "标题数组setting_str(${stringArray.size})图标数组setting_str(${iconArray.size})长度不一致，请检查arrays.xml")
        }
        for (i in stringArray.indices) {
            val model = MineFragmnetModel()
            model.title = stringArray[i]
            model.resId = iconArray[i]
            if (firstIds.indexOf(stringArray.indices.indexOf(i)) >= 0) {
                //头部添加空白区域的项，头部
                model.location = MineFragmnetModel.FIRST
            } else if (endIds.indexOf(stringArray.indices.indexOf(i)) >= 0) {
                //不要分割线的项，尾部
                model.location = MineFragmnetModel.END
            }
            mList.add(model)
        }
        mAdapter = MineFragmentAdapter(mList)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            ToastUtil.showShort(mList[position].title)
        }
        recycler.layoutManager = LinearLayoutManager(mContext)
        recycler.adapter = mAdapter
        refresh.setEnableLoadMore(false)
        refresh.setEnableRefresh(false)
    }

    override fun getIntentData() {
        if (intent != null && intent.extras != null) {
            firstIds = intent?.extras!!.getIntegerArrayList("firstIds") as ArrayList<Int>
            endIds = intent?.extras!!.getIntegerArrayList("endIds") as ArrayList<Int>
        }
    }

    override fun clear() {
        firstIds.clear()
        endIds.clear()
    }

}