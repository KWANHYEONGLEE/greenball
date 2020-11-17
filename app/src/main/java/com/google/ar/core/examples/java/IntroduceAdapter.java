package com.google.ar.core.examples.java;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.core.examples.java.augmentedimage.R;

import java.util.ArrayList;


public class IntroduceAdapter extends RecyclerView.Adapter<IntroduceAdapter.ViewHolder>{

    private ArrayList<IntroduceItem> mData = null ;
    private ArrayList<IntroduceItem> checkedData = null;

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    IntroduceAdapter(ArrayList<IntroduceItem> list) {
        Log.d("context","test.AddMemoAdapter");
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public IntroduceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        Log.d("context","test.AddMemoAdapter.ViewHolder");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_introduce_item, parent, false) ;
        IntroduceAdapter.ViewHolder vh = new IntroduceAdapter.ViewHolder(view) ;

        return vh;
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    @Override
    public void onBindViewHolder(@NonNull final IntroduceAdapter.ViewHolder holder, int position) {
        final IntroduceItem item = mData.get(position) ;
        Log.d("context","test.onBindViewHolder");

        holder.Info_name.setText(item.getName());
        holder.Info_description.setText(item.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClick(holder.itemView, position);
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Info_name;
        TextView Info_description;

        ViewHolder(View itemView){
            super(itemView);

            Log.d("context","test.ViewHolder");

            Info_name = itemView.findViewById(R.id.info_name);
            Info_description = itemView.findViewById(R.id.info_description);
        }
    }
}
