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

import com.bumptech.glide.Glide;
import com.google.ar.core.examples.java.IntroduceItem;
import com.google.ar.core.examples.java.Model.SliderItem;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterRecommend extends
        SliderViewAdapter<SliderAdapterRecommend.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    // 인터페이스 생성
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    // 추천 받은 아이템 정보 리스트
    private ArrayList<IntroduceItem> List_recommend = new ArrayList<IntroduceItem>();


    public SliderAdapterRecommend(Context context, ArrayList<IntroduceItem> List_recommend) {
        this.context = context;
        this.List_recommend = List_recommend;
        Log.i("추천액티비티어댑터", "생성자 실행");
    }

    public void renewRecommend() {
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        Log.i("추천액티비티어댑터", "onCreateViewHolder 실행");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Log.i("추천액티비티어댑터", "onBindViewHolder실행");

        List_recommend.get(position);

        viewHolder.textViewDescription.setText(List_recommend.get(position).getName());
        viewHolder.textViewDescription.setTextSize(16);
        Glide.with(context).load(List_recommend.get(position).getImage())
                .into(viewHolder.imageViewBackground);

//        SliderItem sliderItem = mSliderItems.get(position);
//
//        viewHolder.textViewDescription.setText(sliderItem.getDescription());
//        viewHolder.textViewDescription.setTextColor(Color.WHITE);
//        Log.i("체크", "이미지 : " + sliderItem.getImageUrl());
//
//        Glide.with(context).load(List_recommend.get(position).getImage())
//                .into(viewHolder.imageViewBackground);

//        Glide.with(context)
//                .load("https://img1.daumcdn.net/thumb/R720x0.q80/?scode=mtistory2&fname=http%3A%2F%2Fcfile7.uf.tistory.com%2Fimage%2F24283C3858F778CA2EFABE")// sliderItem.getImageUrl()
//                .fitCenter()
//                .error(R.drawable.arrow_back)
//                .into(viewHolder.imageViewBackground);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
                onItemClickListener.onItemClick(viewHolder.itemView, position);
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
        return List_recommend.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}
