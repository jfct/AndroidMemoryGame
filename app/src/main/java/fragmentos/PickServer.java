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
import pt.isec.memoriaamov.Jogo2Online;
import pt.isec.memoriaamov.R;

/**
 * Created by JFCT on 1/7/2016.
 */

// Escolhe Jogo Online como Servidor

public class PickServer extends Fragment {
        View v;
        Button nivel1, nivel2, nivel3, nivel4, nivel5, custom;

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
                    Intent i = new Intent(getActivity().getApplicationContext(), Jogo2Online.class).putExtra("tema", 1).putExtra("nPares", 4).putExtra("modo", 0);
                    startActivity(i);
                }
            });

            nivel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO modificar values a enviar
                    Intent i = new Intent(getActivity().getApplicationContext(), Jogo2Online.class).putExtra("tema", 2).putExtra("nPares", 15).putExtra("modo", 0);
                    startActivity(i);
                }
            });

            nivel3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO modificar values a enviar
                    Intent i = new Intent(getActivity().getApplicationContext(), Jogo2Online.class).putExtra("tema", 3).putExtra("nPares", 15).putExtra("modo", 0);
                    startActivity(i);
                }
            });

            nivel3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO modificar values a enviar
                    Intent i = new Intent(getActivity().getApplicationContext(), Jogo.class).putExtra("tema", 2).putExtra("nPares", 8).putExtra("modo", 0);
                    startActivity(i);
                }
            });

            nivel4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO modificar values a enviar
                    Intent i = new Intent(getActivity().getApplicationContext(), Jogo.class).putExtra("tema", 1).putExtra("nPares", 10).putExtra("modo", 0);
                    startActivity(i);
                }
            });

            nivel5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO modificar values a enviar
                    Intent i = new Intent(getActivity().getApplicationContext(), Jogo.class).putExtra("tema", 2).putExtra("nPares", 15).putExtra("modo", 0);
                    startActivity(i);
                }
            });

            custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new CustomLevel2(), "custom level").addToBackStack("fragment3").commit();
                }
            });

            return v;
        }
}
