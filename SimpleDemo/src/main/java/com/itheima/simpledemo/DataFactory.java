package com.itheima.simpledemo;

import java.util.ArrayList;
import java.util.List;

public class DataFactory {

    public static List<String> imgListString() {
        List<String> imageData = new ArrayList<>();
        imageData.add("http://d.hiphotos.baidu.com/image/h%3D200/sign=72b32dc4b719ebc4df787199b227cf79/58ee3d6d55fbb2fb48944ab34b4a20a44723dcd7.jpg");
        imageData.add("http://pic.4j4j.cn/upload/pic/20130815/31e652fe2d.jpg");
        imageData.add("http://pic.4j4j.cn/upload/pic/20130815/5e604404fe.jpg");
        imageData.add("http://pic.4j4j.cn/upload/pic/20130909/681ebf9d64.jpg");
        imageData.add("http://d.hiphotos.baidu.com/image/pic/item/54fbb2fb43166d22dc28839a442309f79052d265.jpg");
        return imageData;
    }

    public static List<Integer> imgListInt() {
        List<Integer> imageData = new ArrayList<>();
        imageData.add(R.mipmap.image1);
        imageData.add(R.mipmap.image2);
        imageData.add(R.mipmap.image3);
        imageData.add(R.mipmap.image4);
        imageData.add(R.mipmap.image5);
        return imageData;
    }


    public static String[] imgArrayString() {
        String[] imageData = new String[]{
                "http://d.hiphotos.baidu.com/image/h%3D200/sign=72b32dc4b719ebc4df787199b227cf79/58ee3d6d55fbb2fb48944ab34b4a20a44723dcd7.jpg",
                "http://pic.4j4j.cn/upload/pic/20130815/31e652fe2d.jpg",
                "http://pic.4j4j.cn/upload/pic/20130815/5e604404fe.jpg",
                "http://pic.4j4j.cn/upload/pic/20130909/681ebf9d64.jpg",
                "http://d.hiphotos.baidu.com/image/pic/item/54fbb2fb43166d22dc28839a442309f79052d265.jpg",
        };
        return imageData;
    }


    public static int[] imgArrayInt() {
        int[] imageData = new int[]{
                R.mipmap.image1,
                R.mipmap.image2,
                R.mipmap.image3,
                R.mipmap.image4,
                R.mipmap.image5
        };
        return imageData;
    }

    public static List<String> titleListString() {
        List<String> imageData = new ArrayList<>();
        imageData.add("我的轮播标题一");
        imageData.add("我的轮播标题二");
        imageData.add("我的轮播标题三");
        imageData.add("我的轮播标题四");
        imageData.add("我的轮播标题五");
        return imageData;
    }

    public static String[] titleArrayString() {
        String[] imageData = new String[]{
                "我的轮播标题一",
                "我的轮播标题二",
                "我的轮播标题三",
                "我的轮播标题四",
                "我的轮播标题五",
        };
        return imageData;
    }

}
