package fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import pt.isec.memoriaamov.Jogo2;
import pt.isec.memoriaamov.Jogo2Online;
import pt.isec.memoriaamov.R;

/**
 * Created by JFCT on 1/7/2016.
 */
public class PickServerCustom extends Fragment {

    View v;
    Context context;
    Button play;

    Spinner spinner, spinner2, spinner3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.custom, container, false);

        play = (Button) v.findViewById(R.id.play);

        spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);
        spinner3 = (Spinner) v.findViewById(R.id.spinner3);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.numeroPares, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.temas, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.Intrusos, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner3.setAdapter(adapter3);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temas;
                int intrusos;

                int pares = Integer.parseInt(spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString());

                if(spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString().equals("Fruta"))
                    temas = 1;
                else
                    temas = 2;

                if(spinner3.getItemAtPosition(spinner3.getSelectedItemPosition()).toString().equals("Sim"))
                    intrusos = pares / 3;
                else
                    intrusos = 0;


                Intent i = new Intent(getActivity().getApplicationContext(), Jogo2Online.class).putExtra("tema", temas).putExtra("nPares", pares).putExtra("intrusos", intrusos).putExtra("modo", 0);
                startActivity(i);
            }
        });

        return v;
    }
}
