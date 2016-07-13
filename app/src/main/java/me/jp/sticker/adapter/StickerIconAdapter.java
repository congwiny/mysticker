package me.jp.sticker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.jp.sticker.R;

/**
 * Created by congwiny on 2016/7/13.
 */
public class StickerIconAdapter extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    private Context mContext;

    private static final int[] STICKER_ICON = new int[]{
            R.drawable.artboard_1,
            R.drawable.artboard_1_copy,
            R.drawable.artboard_1_copy_2,
            R.drawable.artboard_1_copy_3,
            R.drawable.artboard_1_copy_4,
            R.drawable.artboard_1_copy_5,
            R.drawable.artboard_1_copy_6,
            R.drawable.artboard_1_copy_7,
            R.drawable.artboard_1_copy_8,
            R.drawable.artboard_1_copy_9,

            R.drawable.artboard_2_copy_1,
            R.drawable.artboard_2_copy_2,
            R.drawable.artboard_2_copy_3,
            R.drawable.artboard_3_copy_4,
            R.drawable.artboard_4_copy_5

    };

    public StickerIconAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_image_sticker, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        vh.stickerPic.setImageResource(STICKER_ICON[position]);
    }

    @Override
    public int getItemCount() {
        return STICKER_ICON.length;
    }

    public int getStickerResId(int position){
        return STICKER_ICON[position];
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView stickerPic;

        public ViewHolder(View itemView) {
            super(itemView);
            stickerPic = (ImageView) itemView.findViewById(R.id.iv_sticker_pic);
        }
    }
}
