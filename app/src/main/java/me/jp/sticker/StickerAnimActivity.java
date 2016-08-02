package me.jp.sticker;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

public class StickerAnimActivity extends AppCompatActivity {

    private static final String TAG = StickerAnimActivity.class.getSimpleName();
    private SimpleDraweeView mSimpleDraweeView;
    private FrameLayout mStickerFlyt;

    private ControllerListener mControllerListener;

    //http://s3-us-west-2.amazonaws.com/solomedia/test/webp/ba277df5feaa5af7b28c2b29568f8ed4.webp
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sticker_anim);

        initListener();
        initView();

    }

    private void initListener() {
        mControllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.e("Final image received! " +
                                "Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(),
                        imageInfo.getHeight(),
                        qualityInfo.getQuality(),
                        qualityInfo.isOfGoodEnoughQuality(),
                        qualityInfo.isOfFullQuality());
                Log.e(TAG, "image width=" + imageInfo.getWidth() + ",image height=" + imageInfo.getHeight());
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                FLog.e(getClass(), "Intermediate image received");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                FLog.e(getClass(), throwable, "Error loading %s", id);
            }
        };
    }

    private void initView() {


        mStickerFlyt = (FrameLayout) findViewById(R.id.sticker_container);

        mSimpleDraweeView = new SimpleDraweeView(this);
        mSimpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(mControllerListener)
                .setUri(Uri.parse("http://s3-us-west-2.amazonaws.com/solomedia/test/webp/ba277df5feaa5af7b28c2b29568f8ed4.webp"))
                .setAutoPlayAnimations(true)
                .build();
        mSimpleDraweeView.setController(controller);


        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(300, 50);
        mStickerFlyt.addView(mSimpleDraweeView, lp);
    }
}
