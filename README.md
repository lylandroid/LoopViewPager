# LoopViewPager
Android LoopViewPager 轮播图控件

#添加权限
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

#添加依赖
```xml
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    compile 'com.github.itcastsh:loopviewpager:1.2.0'
}
```

#可配置的属性
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <declare-styleable name="LoopViewPager">
        <!-- 轮播时间，默认值0表示不自动轮播 -->
        <attr name="loopTime" format="integer" />
        <!-- 动画时间 -->
        <attr name="animTime" format="integer" />
        <!-- 动画效果 -->
        <attr name="animStyle" format="enum">
            <!-- 折叠效果 -->
            <enum name="accordion" value="1" />
            <!-- 立方体效果 -->
            <enum name="cube" value="2" />
        </attr>
        <!-- 是否可以手动滚动页面，默认true -->
        <attr name="scrollEnable" format="boolean" />
        <!-- 触摸页面是否停止轮播，默认true -->
        <attr name="touchEnable" format="boolean" />
    </declare-styleable>

    <declare-styleable name="LoopDotsView">
        <!-- 圆点大小 -->
        <attr name="dotSize" format="integer|dimension|reference" />
        <!-- 圆点宽度 -->
        <attr name="dotWidth" format="integer|dimension|reference" />
        <!-- 圆点高度 -->
        <attr name="dotHeight" format="integer|dimension|reference" />
        <!-- 圆点距离 -->
        <attr name="dotRange" format="integer|dimension|reference" />

        <!-- 圆点形状 -->
        <attr name="dotShape">
            <!-- 矩形，默认值 -->
            <enum name="rectangle" value="1" />
            <!-- 圆形 -->
            <enum name="oval" value="2" />
            <!-- 三角形 -->
            <enum name="triangle" value="3" />
            <!-- 菱形 -->
            <enum name="diamond" value="4" />
        </attr>
        <!-- 圆点颜色，默认值0xFFFFFFFF -->
        <attr name="dotColor" format="color|reference" />
        <!-- 圆点选中颜色，默认值0x55000000 -->
        <attr name="dotSelectColor" format="color|reference" />

        <!-- 圆点资源 -->
        <attr name="dotResource" format="reference" />
        <!-- 圆点选中资源 -->
        <attr name="dotSelectResource" format="reference" />
    </declare-styleable>

</resources>
```


#代码示例
###XML
```xml
    <com.itheima.loopviewpager.LoopViewPager
        android:id="@+id/lvp_pager"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:animStyle="accordion"
        app:animTime="1000"
        app:loopTime="3000">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            android:background="#55000000"
            android:gravity="center"
            android:orientation="horizontal"
            android:padding="10dp">

            <com.itheima.loopviewpager.LoopTitleView
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:maxLines="1"
                android:textColor="#FF0000"
                android:textSize="16sp" />

            <com.itheima.loopviewpager.LoopDotsView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginLeft="10dp"
                app:dotShape="oval"
                app:dotSize="10dp" />

        </LinearLayout>

    </com.itheima.loopviewpager.LoopViewPager>
```
###Java
```java
    private LoopViewPager loopViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_demo2);
        loopViewPager = (LoopViewPager) findViewById(R.id.lvp_pager);
        loopViewPager.setImgAndTitleData(imgListString(), titleListString());
    }

    private List<String> imgListString() {
        List<String> imageData = new ArrayList<>();
        imageData.add("http://d.hiphotos.baidu.com/image/h%3D200/sign=72b32dc4b719ebc4df787199b227cf79/58ee3d6d55fbb2fb48944ab34b4a20a44723dcd7.jpg");
        imageData.add("http://pic.4j4j.cn/upload/pic/20130815/31e652fe2d.jpg");
        imageData.add("http://pic.4j4j.cn/upload/pic/20130815/5e604404fe.jpg");
        imageData.add("http://pic.4j4j.cn/upload/pic/20130909/681ebf9d64.jpg");
        imageData.add("http://d.hiphotos.baidu.com/image/pic/item/54fbb2fb43166d22dc28839a442309f79052d265.jpg");
        return imageData;
    }

    private List<String> titleListString() {
        List<String> titleData = new ArrayList<>();
        titleData.add("1、在这里等着你");
        titleData.add("2、在你身边");
        titleData.add("3、打电话给你就是想说声“嗨”");
        titleData.add("4、不介意你对我大喊大叫");
        titleData.add("5、期待你总是尽全力");
        return titleData;
    }
```

#运行效果
![image](https://github.com/itcastsh/LoopViewPager/blob/master/gif/v1.2.0.gif)

#最低版本
v14+

#QQ交流群
334700525