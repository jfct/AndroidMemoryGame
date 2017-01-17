package pt.isec.memoriaamov;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by JFCT on 1/6/2016.
 */
public class Jogo2 extends Activity {

    ImageAdapter ia;
    public ImageView img;

    View viewAux, viewComp;

    TextView text, text2;
    ImageView turno1, turno2;

    Baralho listaCartas;
    GridView tabelaCartas;
    int nPares;

    int posComp = -1;
    int flag = 0;
    int posAux = -1;
    int idAux= -1;


    // Jogador 1
    int pontuacao1 = 0;
    int tentativas1 = 0;
    int bonus1 = 0;

    // Jogador 2
    int pontuacao2 = 0;
    int tentativas2 = 0;
    int bonus2 = 0;

    int totalpares=0;
    int flagJogador=0;

    private String m_Text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jogo2);

        listaCartas = criaJogo(getIntent().getIntExtra("nPares", 1), getIntent().getIntExtra("tema", 0), getIntent().getIntExtra("intrusos", 0));
        ia = new ImageAdapter(getApplicationContext(), nPares, listaCartas);

        TextView text = (TextView)findViewById(R.id.Tentativas);
        text.setText(String.valueOf(0));

        tabelaCartas = (GridView) findViewById(R.id.gridcartas);
        tabelaCartas.setAdapter(ia);

        flagJogador = 1;

        text = (TextView) findViewById(R.id.txtJogador1);
        text.setTextColor(Color.parseColor("#00FF22"));
        text2 = (TextView) findViewById(R.id.txtJogador2);
        text2.setTextColor(Color.parseColor("#FF0000"));

        turno2 = (ImageView) findViewById(R.id.imageView2);
        turno2.setVisibility(View.GONE);

        tabelaCartas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewComp = view;
                posComp = position;

                // efectuarTurno
                efectuarTurno(view, position, id);
            }});
    }

    public void efectuarTurno( View view, int position, long id) {

        if (position != posAux) {

            listaCartas.getListaCartas().get(position).setVisivel(true);

            flag++;

            // SE TIVER A 2, JOGADOR JA ESCOLHEU 2 CARTAS
            if (flag == 2) {
                flag = 0;
                if (flagJogador == 2) {
                    tentativas2++;
                    TextView texto = (TextView) findViewById(R.id.Tentativas2);
                    texto.setText(String.valueOf(tentativas2));
                } else {
                    tentativas1++;
                    TextView texto = (TextView) findViewById(R.id.Tentativas);
                    texto.setText(String.valueOf(tentativas1));
                }

                // CASO ACERTE A CARTA
                if (listaCartas.getListaCartas().get(position).getId() == idAux) {

                    // CASO ACERTE A CARTA
                    cartaAcerta(position);
                    if (idAux < getIntent().getIntExtra("nPares", 1)) {
                        // Comeca atribuir pontuacao
                        efectuarPontuacao();
                    }
                    // Verifica Final de jogo
                    verificaFinal();

                // CASO FALHE A CARTA
            } else {
                    cartaFalha(position);
                    // Inicio deduzir score
                    deduzirPontuacao();

                    // Inicio de Troca de Turno ( Jogador Falhou)
                    trocaTurno();
                }
            }
            // CASO SEJA 1 CARTA
            else{
                posAux = position;
                idAux = listaCartas.getListaCartas().get(position).getId();
                viewAux = view;
            }
        }
        actualizaCartas();
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

    public void cartaAcerta(int position){
        tabelaCartas.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewComp.setVisibility(viewComp.INVISIBLE);
                viewAux.setVisibility(viewAux.INVISIBLE);
                tabelaCartas.setEnabled(true);
            }
        }, 500);
    }

    public void cartaFalha(int position){
        tabelaCartas.setEnabled(false);

        class gestorCartas implements Runnable {
            int aux1, aux2;

            gestorCartas(int a, int b) {
                aux1 = a;
                aux2 = b;
            }

            public void run() {
                tabelaCartas.setEnabled(true);
                listaCartas.getListaCartas().get(aux1).setVisivel(false);
                listaCartas.getListaCartas().get(aux2).setVisivel(false);
                actualizaCartas();
            }
        }
        new Handler().postDelayed(new gestorCartas(position, posAux) {
        }, 500);
    }

    public void verificaFinal() {
        if (totalpares == (getIntent().getIntExtra("nPares", 1)) - getIntent().getIntExtra("intrusos", 0)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Jogo2.this);
            if (pontuacao1 > pontuacao2)
                builder.setTitle("Parabéns Jogador 1!                  Insira o seu Nome");
            else
                builder.setTitle("Parabéns Jogador 2!                  Insira o seu Nome");

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
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Jogo2.this);

                    while (contador < 5 && flagsp == 0) {
                        String resultado = "score" + contador;
                        if (sp.getInt(resultado, 0) == 0) {
                            preencheSP(sp, contador);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("username" + 0, m_Text);
                            editor.putString("jogo" + 0, "2J");

                            if (pontuacao1 > pontuacao2)
                                editor.putInt(resultado, pontuacao1);
                            else
                                editor.putInt(resultado, pontuacao2);

                            editor.commit();   // Do not forget this to actually store the values
                            flagsp = 1;
                        }
                        contador++;
                    }
                    contador = 0;
                    if (flagsp == 0) {
                        int loop = 4;
                        preencheSP(sp, loop);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("username0", m_Text);
                        editor.putString("jogo0", "2J");

                        if (pontuacao1 > pontuacao2)
                            editor.putInt("score0", pontuacao1);
                        else
                            editor.putInt("score0", pontuacao2);

                        editor.commit();
                    }
                    Jogo2.super.onBackPressed();
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
    }

    public void efectuarPontuacao(){
        if (flagJogador == 1) {
            if (bonus1 != 0)
                pontuacao1 += 300 + (bonus1 * 200);
            else {
                pontuacao1 += 300;
            }

            bonus1++;
            TextView texto1 = (TextView) findViewById(R.id.Pontuacao);
            texto1.setText(String.valueOf(pontuacao1));
            totalpares++;
        } else {
            if (bonus2 != 0)
                pontuacao2 += 300 + (bonus2 * 200);
            else {
                pontuacao2 += 300;
            }

            bonus2++;
            TextView texto1 = (TextView) findViewById(R.id.Pontuacao2);
            texto1.setText(String.valueOf(pontuacao2));
            totalpares++;
        }

    }

    public void deduzirPontuacao(){
        if(flagJogador==1) {
            if (pontuacao1 >= 50) {
                pontuacao1 -= 50;
                TextView texto1 = (TextView) findViewById(R.id.Pontuacao);
                texto1.setText(String.valueOf(pontuacao1));
            }
            bonus1 = 0;
        }else {
            if (pontuacao2 >= 50) {
                pontuacao2 -= 50;
                TextView texto1 = (TextView) findViewById(R.id.Pontuacao2);
                texto1.setText(String.valueOf(pontuacao2));
            }
            bonus2 = 0;
        }

    }

    public void trocaTurno(){
        if(flagJogador == 2) {
            flagJogador = 1;
            TextView text = (TextView) findViewById(R.id.txtJogador2);
            text.setTextColor(Color.parseColor("#FF0000"));
            TextView text2 = (TextView) findViewById(R.id.txtJogador1);
            text2.setTextColor(Color.parseColor("#00FF22"));

            // Amostra de Turno
            turno1 = (ImageView) findViewById(R.id.imageView1);
            turno1.setVisibility(View.VISIBLE);

            turno2 = (ImageView) findViewById(R.id.imageView2);
            turno2.setVisibility(View.GONE);

        }
        else {
            flagJogador = 2;
            TextView text = (TextView) findViewById(R.id.txtJogador2);
            text.setTextColor(Color.parseColor("#00FF22"));
            TextView text2 = (TextView) findViewById(R.id.txtJogador1);
            text2.setTextColor(Color.parseColor("#FF0000"));

            // Amostra de Turno
            turno1 = (ImageView) findViewById(R.id.imageView1);
            turno1.setVisibility(View.GONE);

            turno2 = (ImageView) findViewById(R.id.imageView2);
            turno2.setVisibility(View.VISIBLE);
        }
        posAux = -1;
    }

    public void actualizaCartas(){
        ia.Update();
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Jogo2.this);

        builder.setTitle(getApplicationContext().getString(R.string.QuerSair));

        //final EditText input = new EditText(getApplicationContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        //builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Jogo2.super.onBackPressed();
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

