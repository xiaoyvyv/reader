package com.xiaoyv.comic.datasource.jni.codec;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;

import com.xiaoyv.comic.datasource.jni.AnnotationType;
import com.xiaoyv.comic.datasource.jni.bitmaps.BitmapRef;

import java.util.List;

public interface CodecPage {

    int getWidth();

    int getHeight();

    BitmapRef renderBitmap(int width, int height, RectF pageSliceBounds, boolean cache);

    BitmapRef renderBitmapSimple(int width, int height, RectF pageSliceBounds);

    Bitmap renderThumbnail(int width);

    Bitmap renderThumbnail(int width, int originW, int originH);

    List<PageLink> getPageLinks();

    List<Annotation> getAnnotations();

    List<Annotation> getAnnotationsImpl();

    public TextWord[][] getText();

    void recycle();

    boolean isRecycled();

    public void addAnnotation(float[] color, PointF[][] points, float width, float alpha);

    long getPageHandle();

    void addMarkupAnnotation(PointF[] quadPoints, AnnotationType type, float[] color);

    String getPageHTML();

    String getPageHTMLWithImages();

    int getCharCount();

}
