# HeaderSelector
【Android】弹框图片选择器，支持裁剪圆形矩形和矩形裁剪  

### 配置
`Android 7.0`以上，在`manifest`的`application`下(和`activity`同级)加入以下代码

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths" />
</provider>
```
并在`res`下新建`xml`目录，新建`provider_paths.xml`文件，内容如下：  
其中的`com.dubhe.headerselector`建议替换为你自己的项目名称。
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <files-path
        name="images"
        path="Android/data/com.dubhe.headerselector/files/Pictures/OriPicture/" />
    <external-path
        name="images"
        path="Android/data/com.dubhe.headerselector/files/Pictures/OriPicture/" />
    <external-files-path
        name="images"
        path="files/Pictures/OriPicture" />
    <root-path
        name="images"
        path="" />
    <root-path
        name="images"
        path="" />
</paths>
```

### 初始化
支持链式调用
```java
HeaderSelector.getInstance(mActivity)//初始化图片选择器对象，参数是Activity
              .setEnableClip(true)//是否裁剪图片
              .setClipMode(ClipImageActivity.TYPE_CIRCLE)//裁图模式 TYPE_CIRCLE圆形 TYPE_RECTANGLE矩形
              .setOnProcessFinishListener(new HeaderSelector.OnProcessFinishListener() {
                  //完成所有操作后返回最终结果的path
                  //TODO:不set将会导致无法拿到返回结果
                  @Override
                  public void onProcessFinish(String path) {
                       //TODO:拿到path进行逻辑操作
                  }
              });
```

### 接收回调
重写`onActivityResult`方法来接收回调数据，若在`Fragment`中使用则重写所在`Activity`的`onActivityResult`
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    HeaderSelector.getInstance(mActivity).onHeaderResult(requestCode, resultCode, data);
}
``` 

### 启动选择器
最终选择/裁剪后得到的图片`path`将从`onProcessFinish`方法中返回
```java
HeaderSelector.getInstance(mActivity).showImageSelectMenu();//显示图片选择器
```

### 回收
在调用的`Activity`的`onDestroy`方法中调用以下方法避免报空并回收资源
```java
@Override
protected void onDestroy() {
    super.onDestroy();
    HeaderSelector.getInstance(this).clear();
}
```

### 注意事项
裁剪器的圆形矩形仅为裁剪框的形状，仅便于用户定位，并不会改变图片的形状。  
**如果要显示圆形的图片请使用圆形的ImageView。**


