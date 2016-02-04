package com.peaceworld.wikisms.view.newui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.entity.SimilarContent;
import com.peaceworld.wikisms.view.activity.SimilarContentVerificationActivity;

public class CustomListDialog extends Dialog {
	
	protected ContentViewerAdapter adapter;
	private Drawable itemBg1, itemBg2;	
	private SimilarContent similarContent;
	private Context context;
	private TextView titleTextView;
	private ListView listView;
	private Button okButton,cancelButton;
	private SimilarContentVerificationActivity scva;
	public CustomListDialog(Context context,SimilarContent similarContent,SimilarContentVerificationActivity scva) {
		
		super(context);
		this.context=context;
		this.scva=scva;
		this.similarContent=similarContent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_similarity_confirmation);
		titleTextView=(TextView)findViewById(R.id.titleTextView);
		listView=(ListView)findViewById(R.id.contentViewListView);
		okButton=(Button)findViewById(R.id.OkButton);
		cancelButton=(Button)findViewById(R.id.CancelButton);
		okButton.setOnClickListener(onClickListener_ok);
		cancelButton.setOnClickListener(onClickListener_cancle);
		titleTextView.setText(context.getResources().getString(R.string.similarity_check_dialog_header));
		adapter =new ContentViewerAdapter();
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	
	private View.OnClickListener onClickListener_ok = new View.OnClickListener() {
		

		@Override
		public void onClick(View v) {
			
			scva.removeView(true);
			dismiss();
		}
	};

	private View.OnClickListener onClickListener_cancle = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			scva.removeView(false);
			dismiss();
		}
	};
	
	public class ContentViewerAdapter extends BaseAdapter {

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public int getCount() {
			if (similarContent.SimilarContentList != null)
				return similarContent.SimilarContentList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if (similarContent.SimilarContentList != null)
				return similarContent.SimilarContentList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {

			view = getLayoutInflater().inflate(
					R.layout.similar_content_verification_list_itme, null);
			TextView contentTextView = (TextView) view
					.findViewById(R.id.ContentViewListItemTextView);
			TextView metaInfoTextView = (TextView) view
					.findViewById(R.id.metaInfoTextView);
			if (index % 2 == 0)
				contentTextView.setBackgroundDrawable(itemBg1);
			else
				contentTextView.setBackgroundDrawable(itemBg2);
			
			contentTextView.setText(similarContent.SimilarContentList.get(index)+"\r\n");
			try{
				int similarityPercent=(int)(similarContent.SimilarityList.get(index)*100);
				String info= similarityPercent+
						"% شباهت ";
				metaInfoTextView.setText(info);
			}catch(Exception ex){}
			metaInfoTextView.setTag(index);
			contentTextView.setTag(index);
			return view;
		}
		
	}


}
