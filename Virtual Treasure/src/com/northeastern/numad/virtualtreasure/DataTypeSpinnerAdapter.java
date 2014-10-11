package com.northeastern.numad.virtualtreasure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.neu.virtualtreasure.R;

public class DataTypeSpinnerAdapter extends SimpleAdapter{
	
	public static String MAP_KEY_NAME = "Name";
	public static String MAP_KEY_ICON = "Icon";
	public static String MAP_KEY_TYPE = "Type";

	Activity mActivity;
	
    public DataTypeSpinnerAdapter(Context context, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to, Activity activity) {
        super(context, data, resource, from, to);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.custom_spinner_dropdown,
                    null);
        }

		@SuppressWarnings("unchecked")
		HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

        ((TextView) convertView.findViewById(R.id.tvSpinnerType))
                .setText((String) data.get("Name"));
        ((ImageView) convertView.findViewById(R.id.ivSpinnerType))
                .setImageResource((Integer) data.get("Icon"));

        return convertView;
    }
    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.custom_spinner,
                    null);
        }

        @SuppressWarnings("unchecked")
		HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

        ((TextView) convertView.findViewById(R.id.tvSpinnerType))
                .setText((String) data.get(MAP_KEY_NAME));
        ((ImageView) convertView.findViewById(R.id.ivSpinnerType))
                .setImageResource((Integer) data.get(MAP_KEY_ICON));

        return convertView;
    }
}
