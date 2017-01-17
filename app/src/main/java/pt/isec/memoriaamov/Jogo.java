package pt.isec.memoriaamov;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.*;
import android.widget.TableRow;
import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by JFCT on 12/31/2015.
 */

public class Jogo extends Activity {

    ImageAdapter ia;
    public ImageView img;

    Baralho listaCartas;

    GridView tabelaCartas;

    int posComp = -1;
    int flag = 0;
    int posAux = -1;
    int idAux= -1;

    int nPares;

    int pontuacao = 0;
    int tentativas = 0;
    int bonus = 0;

    int totalpares=0;

    private String m_Text = "";

    View viewAux, viewComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jogo);

        listaCartas = criaJogo(getIntent().getIntExtra("nPares", 1), getIntent().getIntExtra("tema", 0), getIntent().getIntExtra("intrusos", 0));
        ia = new ImageAdapter(getApplicationContext(), nPares, listaCartas);

        TextView text = (TextView)findViewById(R.id.Tentativas);
        text.setText(String.valueOf(0));

        tabelaCartas = (GridView) findViewById(R.id.gridcartas);
        tabelaCartas.setAdapter(ia);

        tabelaCartas.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                viewComp = view;
                posComp = position;


                if (position != posAux ) {
                    listaCartas.getListaCartas().get(position).setVisivel(true);

                    flag++;

                    if (flag == 2) {
                        flag = 0;
                        tentativas++;

                        TextView texto = (TextView)findViewById(R.id.Tentativas);
                        texto.setText(String.valueOf(tentativas));

                        if (listaCartas.getListaCartas().get(position).getId() == idAux) {

                            tabelaCartas.setEnabled(false);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewComp.setVisibility(viewComp.INVISIBLE);
                                    viewAux.setVisibility(viewAux.INVISIBLE);
                                    tabelaCartas.setEnabled(true);
                                }
                            }, 500);

                            if(idAux < getIntent().getIntExtra("nPares", 1)) {
                                if (bonus != 0)
                                    pontuacao += 300 + (bonus * 200);
                                else {
                                    pontuacao += 300;
                                }
                                bonus++;
                                TextView texto1 = (TextView) findViewById(R.id.Pontuacao);
                                texto1.setText(String.valueOf(pontuacao));

                                totalpares++;
                            }

                            // Verifica se é fim de jogo
                            if(totalpares == (getIntent().getIntExtra("nPares", 1)) - getIntent().getIntExtra("intrusos", 0)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Jogo.this);
                                builder.setTitle("Parabéns!!");

                                final EditText input = new EditText(getApplicationContext());
                                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                                builder.setView(input);

                                // Set up the buttons
                                builder.setPositiveButton("Submeter", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int contador = 0, flagsp = 0;

                                        m_Text = input.getText().toString();
                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Jogo.this);

                                        while(contador < 5 && flagsp == 0) {
                                            String resultado = "score"+contador;
                                            if (sp.getInt(resultado, 0) == 0) {
                                                preencheSP(sp, contador);
                                                SharedPreferences.Editor editor = sp.edit();
                                                editor.putString("username" + 0, m_Text);
                                                editor.putString("jogo" + 0, "1J");
                                                editor.putInt(resultado, pontuacao);

                                                editor.commit();   // Do not forget this to actually store the values
                                                flagsp =1;
                                            }
                                            contador++;
                                        }
                                        contador = 0;
                                        if(flagsp == 0)
                                        {
                                            int loop = 4;
                                            preencheSP(sp, loop);
                                            SharedPreferences.Editor editor = sp.edit();

                                            editor.putString("username0", m_Text);
                                            editor.putString("jogo0", "1J");
                                            editor.putInt("score0", pontuacao);
                                            editor.commit();
                                        }
                                        Jogo.super.onBackPressed();
                                    }
                                });
                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                            // CASO FALHE A CARTA
                        } else {
                            tabelaCartas.setEnabled(false);

                            class gestorCartas implements Runnable {
                                int aux1, aux2;
                                gestorCartas(int a, int b) { aux1 = a; aux2 = b; }
                                public void run() {
                                    tabelaCartas.setEnabled(true);
                                    listaCartas.getListaCartas().get(aux1).setVisivel(false);
                                    listaCartas.getListaCartas().get(aux2).setVisivel(false);
                                    actualizaCartas();
                                }
                            }
                            new Handler().postDelayed(new gestorCartas(position, posAux) {
                            }, 500);

                            if(pontuacao >= 50) {
                                pontuacao -= 50;
                                TextView texto1 = (TextView) findViewById(R.id.Pontuacao);
                                texto1.setText(String.valueOf(pontuacao));
                            }
                            bonus = 0;
                            posAux = -1;
                        }
                    }
                    // CASO SEJA 1 CARTA
                    else {
                        posAux = position;
                        idAux = listaCartas.getListaCartas().get(position).getId();
                        viewAux = view;
                    }
                    actualizaCartas();
                }
            }
        });
    }

    public Baralho criaJogo(int nPares, int tema, int intrusos){
        listaCartas = new Baralho(nPares, getApplicationContext(), intrusos);

        listaCartas.setTema(tema);
        return listaCartas;
    }

    public void preencheSP(SharedPreferences sp, int loops){
        SharedPreferences.Editor editor = sp.edit();
        for(int i = loops; i > 0; i--)
        {
            editor.putString("username"+i, sp.getString("username"+(i-1), "default"));
            editor.putString("jogo"+i, sp.getString("jogo"+(i-1), "default"));
            editor.putInt("score"+i, sp.getInt("score"+(i-1), 0));
        }
        editor.commit();
    }

    public void actualizaCartas(){
        ia.Update();
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Jogo.this);

        builder.setTitle(getApplicationContext().getString(R.string.QuerSair));

        //final EditText input = new EditText(getApplicationContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        //builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Jogo.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}


