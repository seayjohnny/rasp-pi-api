package vivatexai.rasp_pi_android_bt.service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import vivatexai.rasp_pi_android_bt.R;


public class LogFragment extends Fragment {
    // Layout views
    private ListView mConversationView;

    // Member fields
    private static ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Empty constructor.
     */
    public LogFragment() {}

    public static LogFragment newInstance(int sectionNumber) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get view
        View rootView = inflater.inflate(R.layout.fragment_log, container, false);

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.log_message);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.log);

        mConversationView.setAdapter(mConversationArrayAdapter);
    }

    public synchronized void write(String message) {
        System.out.println("MESSAGE SENT TO LOG--------------------");
        mConversationArrayAdapter.add(message);
    }
}
