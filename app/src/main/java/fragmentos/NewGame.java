package fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pt.isec.memoriaamov.Jogo2Online;
import pt.isec.memoriaamov.R;
import pt.isec.memoriaamov.Resultados;

/**
 * Created by JFCT on 12/30/2015.
 */

//Escolhe Jogo Online como Cliente

public class NewGame extends Fragment {
    View v;
    Button tipojogo1, tipojogo2, tipojogo3, tipojogo4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.newgame, container, false);

        tipojogo1 = (Button) v.findViewById(R.id.txt1Player);
        tipojogo2 = (Button) v.findViewById(R.id.txt2PlayersL);
        tipojogo3 = (Button) v.findViewById(R.id.txtPlayersNServer);
        tipojogo4 = (Button) v.findViewById(R.id.txtPlayersNClient);

        tipojogo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PickLevel(), "pick level").addToBackStack("fragment3").commit();
            }
        });

        tipojogo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PickLevel2(), "pick level2").addToBackStack("fragment3").commit();
            }
        });

        tipojogo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new PickServer(), "pick role").addToBackStack("fragment3").commit();
            }
        });

        tipojogo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), Jogo2Online.class).putExtra("modo", 1);
                startActivity(i);
            }
        });

        return v;
    }
}
