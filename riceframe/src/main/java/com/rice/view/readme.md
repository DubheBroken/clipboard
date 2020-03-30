# 自定义View Module
### CircleView[圆形View]
绘制一个圆形，继承自View。  
##### 参数列表

| 参数名  |允许的值|含义|setter|
|---|---|---|---|
| cir_color|16进制颜色值|圆形的填充色|setColor|

### ArcImageView[弧形ImageView]
弧形的ImageView，继承自ImageView。记得要写`android:scaleType="centerCrop"`
##### 参数列表

| 参数名  |允许的值|含义|setter|
|---|---|---|---|
| arcHeight| dp |弧形高度，正数为外凸|N/A|

### PagerSlidingTabStrip[带滑动动画的TabLayout]

##### 参数列表
```
pstsDividerColor="@android:color/transparent"
app:pstsDividerPadding="20dp"
app:pstsIndicatorColor="@color/colorPrimaryDark"
app:pstsIndicatorHeight="2dp"
app:pstsShouldExpand="true"
app:pstsTabPaddingLeftRight="0dp"
app:pstsUnderlineColor="@color/colorPrimaryDark"
app:pstsUnderlineHeight="0dp"
```

### CircleImageView[圆形ImageView]
直接用就完了，管他什么图统统盘圆了。记得要写`android:scaleType="centerCrop"`

### AutoNextLineLinearLayout[自动换行LinearLayout]
直接用就完了

### FlowLayoutManager[自动换行的LinearLayoutManager]
初始化时第二个参数为是否开启自动换行