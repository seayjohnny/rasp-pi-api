package vivatexai.rasp_pi_android_bt.common;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vivatexai.rasp_pi_android_bt.R;

public class DeviceListAdapter extends ArrayAdapter<DeviceListItem> {
    Context context;
    List<DeviceListItem> items;

    public DeviceListAdapter(Context context, int resourceId, List<DeviceListItem> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    private class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    public View getView(int position, View convertView, ViewGroup container) {
        ViewHolder holder = null;
        DeviceListItem device = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.
                getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.device_list_name, null);
            holder = new ViewHolder();
            holder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.deviceName.setText(device.getName());
            holder.deviceAddress.setText(device.getAddress());
        }

        return convertView;
    }
}
