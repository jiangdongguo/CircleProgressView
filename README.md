# CircleProgressView
**自定义圆形进度条控件**

![image](http://img.blog.csdn.net/20170823211900753?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvQW5kckV4cGVydA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

**1. 添加依赖**  

(1) 在工程build.gradle中添加
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
  
(2) 在module的gradle中添加
```
dependencies {
    compile 'com.github.jiangdongguo:CircleProgressView:v1.02'
}
```

**2. 使用方法**  

(1) Java代码  

没有具体数值的进度条  
```
// 设置状态为连接中，此外，
// CircleProgressView.STATE_UNDONE为失败恢复到默认
// CircleProgressView.STATE_DONE为成功执行完毕
mCircleView.setConnectState(CircleProgressView.STATE_DOING);
// 设置风格为没有具体数值进度条
mCircleView.setProgressVaule(CircleProgressView.NONE);

```
有具体数据的进度条
```  
// 状态为进行中
mCircleView.setConnectState(CircleProgressView.STATE_DOING);
// 进度条最大值，可设置其他具体值
mCircleView.setTotalSize(100);
// 进度条当前值，可设置其他具体值
mCircleView.setProgressVaule(10);
// 中间显示进度百分比文本
mCircleView.setShowTextTipFlag(true);
// 状态为执行完毕
mCircleView.setConnectState(CircleProgressView.STATE_DONE);
```  
```  
// 添加点击事件监听，点击动画  
  mProgressView1.setOnViewClickListener(new CircleProgressView.OnViewClickListener() {
            @Override
            public void onViewClick() {
                mProgressView1.setConnectState(CircleProgressView.STATE_DOING);
                mProgressView1.setTotalSize(100);
                mProgressView1.setShowTextTipFlag(true);
                mProgressView1.setProgressVaule(i);
                i++;
            }
        });
```

(2) XML文件配置  

```  
  <!--使用默认配置-->
  <com.jiangdg.circleprogressview.CircleProgressView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
    <!--自定义配置-->        
   <com.jiangdg.circleprogressview.CircleProgressView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            custom:outsideCircleBgColor="@color/white_color"     // 外部圆形颜色
            custom:insideRectangleBgColor="@color/red_deep_color"// 内部矩形颜色
            custom:insideCircleBgColor="@color/red_deep_color"  // 内部圆形颜色
            custom:progressArcBgColor="@color/black_color" // 进度条颜色
            custom:tipTextColor="@color/white_color"  // 进度百分比字体颜色
            custom:tipTextSize="14sp"/> // 进度百分比字体大小
```
