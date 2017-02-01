package vivatexai.rasp_pi_android_bt.common;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vivatexai.rasp_pi_android_bt.R;

public class FragmentBase extends Fragment{
    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Empty constructor.
     */
    public FragmentBase() {}

    public static FragmentBase newInstance(int sectionNumber) {
        FragmentBase fragment = new FragmentBase();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format,
                getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
