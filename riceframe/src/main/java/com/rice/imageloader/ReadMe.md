# ImageLoader
图片加载框架封装
### GlideLoadUtils
包含判空，不会在异步时抛出异常
```java
/**
* imgurl    要加载的图片，可以是本地path或网络url
* imageview 要加载图片的ImageView对象
* isCir     是否加载为圆形
*/
GlideLoadUtils.getInstance().glideLoad(context, imgurl, imageview,isCir)
```