package fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pt.isec.memoriaamov.Jogo;
import pt.isec.memoriaamov.R;
import pt.isec.memoriaamov.Resultados;

/**
 * Created by JFCT on 12/30/2015.
 */
public class MainFrag  extends Fragment {

    Button novojogo, creditos, resultados;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_main, container, false);
        novojogo = (Button) v.findViewById(R.id.btNewGame);
        creditos = (Button) v.findViewById(R.id.btCredits);
        resultados = (Button) v.findViewById(R.id.btResults);

        novojogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewGame(), "new game").addToBackStack("fragment2").commit();
            }
        });
        creditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new Credits(), "credits").addToBackStack("fragment2").commit();
            }
        });
        resultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), Resultados.class);
                startActivity(i);
            }
        });

        return v;
    }
}
