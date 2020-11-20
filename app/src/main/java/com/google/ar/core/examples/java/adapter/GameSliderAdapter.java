package com.google.ar.core.examples.java.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.ar.core.examples.java.IntroduceAdapter;
import com.google.ar.core.examples.java.Model.GameItem;
import com.google.ar.core.examples.java.Model.SliderItem;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameSliderAdapter extends
        SliderViewAdapter<GameSliderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GameItem> mGameItems = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    GameSliderAdapter.OnItemClickListener mListener;

    public void setOnItemClickListener(GameSliderAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public GameSliderAdapter(Context context) {
        this.context = context;
    }

    public void renewGameItems(ArrayList<GameItem> gameItems) {
        this.mGameItems = gameItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mGameItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(GameItem gameItem) {
        this.mGameItems.add(gameItem);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_slider_layout_item, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        GameItem gameItem = mGameItems.get(position);

        viewHolder.textViewDescription.setText(gameItem.getDescription());
        Log.i("체크", "이미지 : " + gameItem.getImageDrawable());

        Glide.with(context).load(gameItem.getImageDrawable()).into(viewHolder.imageViewBackground);

//        Glide.with(context)
//                .load("https://img1.daumcdn.net/thumb/R720x0.q80/?scode=mtistory2&fname=http%3A%2F%2Fcfile7.uf.tistory.com%2Fimage%2F24283C3858F778CA2EFABE")// sliderItem.getImageUrl()
//                .fitCenter()
//                .error(R.drawable.arrow_back)
//                .into(viewHolder.imageViewBackground);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(viewHolder.itemView, position);
                }
            }
        });
    }

    // 다음 장 같이 보이게 처리
    @Override
    public float getPageWidth(int position) {
        return (0.9f);
        //return super.getPageWidth(position);
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mGameItems.size();
    }

    class ViewHolder extends SliderViewAdapter.ViewHolder {

        View itemView;
        CardView game_cardview;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            game_cardview = itemView.findViewById(R.id.game_cardview);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_game_slider);
            textViewDescription = itemView.findViewById(R.id.tv_title_game_slider);
            this.itemView = itemView;
        }
    }

}
