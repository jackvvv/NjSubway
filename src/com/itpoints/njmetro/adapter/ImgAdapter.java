package com.itpoints.njmetro.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.itpoints.njmetro.R;
import com.itpoints.njmetro.utils.BitmapHelp;
import com.itpoints.njmetro.utils.ImageBimp;
import com.shorigo.photo.model.PhotoModel;

/**
 * 
 * @ClassName: GdAdapter
 * 
 */
public class ImgAdapter extends BaseAdapter {
	private Context mContext;
	private List<PhotoModel> mLists;
	private Handler mHandler;

	public ImgAdapter(Context mContext, List<PhotoModel> mLists, Handler handler) {
		this.mLists = mLists;
		this.mContext = mContext;
		this.mHandler = handler;
	}

	public void setData(List<PhotoModel> mLists) {
		this.mLists = mLists;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mLists == null ? 0 : mLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mLists == null ? null : mLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View view, ViewGroup group) {
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(R.layout.img_item, null);
			holder.img = (ImageView) view.findViewById(R.id.img);
			holder.iv_del = (ImageView) view.findViewById(R.id.iv_activity_slidingmenu_albums_item_delete);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		PhotoModel info = mLists.get(position);

		if (info != null) {
			if (info.getOriginalPath().equals("default")) {
				holder.img.setImageResource(R.drawable.add_img);
				holder.iv_del.setVisibility(View.INVISIBLE);
			} else {
				holder.iv_del.setVisibility(View.VISIBLE);
				BitmapHelp.getInstance(mContext).display(holder.img, info.getOriginalPath());
			}
			ImageBimp.setViewHeight2(holder.img, (ImageBimp.getScreen(mContext)[1] - ImageBimp.dip2px(mContext, 50)) / 4);

			holder.iv_del.setTag(position);
			holder.iv_del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Message msg = new Message();
					msg.what = 1;
					msg.arg1 = (Integer) arg0.getTag();
					mHandler.sendMessage(msg);
				}
			});
		}
		return view;
	}

	class Holder {
		ImageView img;
		ImageView iv_del;
	}

}
