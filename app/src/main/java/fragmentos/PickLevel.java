package fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pt.isec.memoriaamov.Jogo;
import pt.isec.memoriaamov.Jogo2;
import pt.isec.memoriaamov.R;

/**
 * Created by JFCT on 12/30/2015.
 */
public class PickLevel extends Fragment {
    View v;
    Button nivel1, nivel2, nivel3, nivel4, nivel5, custom;

    int intrusos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.picklevel, container, false);

        nivel1 = (Button) v.findViewById(R.id.btLevel1);
        nivel2 = (Button) v.findViewById(R.id.btLevel2);
        nivel3 = (Button) v.findViewById(R.id.btLevel3);
        nivel4 = (Button) v.findViewById(R.id.btLevel4);
        nivel5 = (Button) v.findViewById(R.id.btLevel5);

        custom = (Button) v.findViewById(R.id.button9);

        nivel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), Jogo.class).putExtra("tema", 1).putExtra("nPares", 4);
                startActivity(i);
            }
        });

        nivel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO modificar values a enviar
                Intent i = new Intent(getActivity().getApplicationContext(), Jogo.class).putExtra("tema", 2).putExtra("nPares", 6);
                startActivity(i);
            }
        });

        nivel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO modificar values a enviar
                Intent i = new Intent(getActivity().getApplicationContext(), Jogo.class).putExtra("tema", 2).putExtra("nPares", 8);
                startActivity(i);
            }
        });

        nivel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO modificar values a enviar
                Intent i = new Intent(getActivity().getApplicationContext(), Jogo.class).putExtra("tema", 1).putExtra("nPares", 10);
                startActivity(i);
            }
        });

        nivel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO modificar values a enviar
                Intent i = new Intent(getActivity().getApplicationContext(), Jogo.class).putExtra("tema", 2).putExtra("nPares", 15);
                startActivity(i);
            }
        });

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new CustomLevel(), "custom level").addToBackStack("fragment3").commit();
            }
        });

        return v;
    }




}
