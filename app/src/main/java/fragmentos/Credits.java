package fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.isec.memoriaamov.R;

/**
 * Created by JFCT on 12/30/2015.
 */

public class Credits extends Fragment {
    View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.credits, container, false);
        return v;
    }
}
