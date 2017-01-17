package pt.isec.memoriaamov;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by JFCT on 1/7/2016.
 */
public class Jogo2Online extends Activity {
    private static final int PORT = 8899;
    private static final int PORTaux = 9988;

    int modo = 0;

    ProgressDialog pd = null;

    ServerSocket serverSocket=null;
    Socket socketGame = null;

    ObjectInputStream input;
    ObjectOutputStream output;

    BufferedReader in;
    PrintWriter out;

    Handler procMsg = null;




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

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        procMsg = new Handler();

        //listaCartas = criaJogo(getIntent().getIntExtra("nPares", 1), getIntent().getIntExtra("tema", 0), getIntent().getIntExtra("intrusos", 0));
        //ia = new ImageAdapter(getApplicationContext(), nPares, listaCartas);

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
        Log.d("OnCreate", "Inicio");

        tabelaCartas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewComp = view;
                posComp = position;

                // efectuarTurno
                efectuarTurno(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getIntExtra("modo", 0) == 0) {
            flagJogador = 1;
            server();
        }
        else {
            Log.d("onResume", "entrou no onresume");
            flagJogador = 2;
            clientDlg();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            commThread.interrupt();
            if (socketGame != null)
                socketGame.close();
            if (output != null)
                output.close();
            if (input != null)
                input.close();
        } catch (Exception e) {
        }
        input = null;
        output = null;
        socketGame = null;
    }

    void server() {
        // WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        // String ip = Formatter.formatIpAddress(wm.getConnectionInfo()
        // .getIpAddress());

        listaCartas = criaJogo(getIntent().getIntExtra("nPares", 1), getIntent().getIntExtra("tema", 0), getIntent().getIntExtra("intrusos", 0));
        listaCartas.setTurnoJogador(1);

        String ip = getLocalIpAddress();
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.serverdlg_msg) + "\n(IP: " + ip
                + ")");
        pd.setTitle(R.string.serverdlg_title);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                    }
                    serverSocket = null;
                }
            }
        });
        pd.show();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    socketGame = serverSocket.accept();
                    serverSocket.close();
                    serverSocket=null;
                    Log.d("server", "server, socketGame passou");
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("server", "Servidor Handler, socketGame?");
                        pd.dismiss();
                        if (socketGame == null)
                            finish();
                    }
                });
            }
        });
        t.start();
    }

    void clientDlg() {
        final EditText edtIP = new EditText(this);
        edtIP.setText("192.168.1.75");
        AlertDialog ad = new AlertDialog.Builder(this).setTitle(R.string.client_title)
                .setMessage(R.string.InsertIP).setView(edtIP)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        client(edtIP.getText().toString(), PORT); // to test with emulators: PORTaux);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).create();
        ad.show();
    }

    void client(final String strIP, final int Port) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("cliente", "Connecting to the server  " + strIP);
                    socketGame = new Socket(strIP, Port);
                } catch (Exception e) {
                    socketGame = null;
                }
                if (socketGame == null) {
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("cliente", "Handler, socket game Nulo");
                            finish();
                        }
                    });
                    Log.d("cliente", "client, socket game Nulo");
                    return;
                }
                commThreadClient.start();
            }
        });
        t.start();
    }

    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {

                output = new ObjectOutputStream(socketGame.getOutputStream());
                output.flush();
                ia = new ImageAdapter(getApplicationContext(), nPares, listaCartas);

                output.writeObject(listaCartas);
                output.flush();

                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        actualizaThreads();
                        pd.dismiss();
                    }
                });

                Log.d("threadServer", "Escreveu");

                in = new BufferedReader(new InputStreamReader(
                        socketGame.getInputStream()));
                out = new PrintWriter(socketGame.getOutputStream());


                while (!Thread.currentThread().isInterrupted()) {
                        if(listaCartas.getTurnoJogador()==1) {
                            procMsg.post(new Runnable() {
                                @Override
                                public void run() {
                                    tabelaCartas.setEnabled(true);
                                    //moveOtherPlayer(move);
                                }
                            });
                        }
                    else
                            procMsg.post(new Runnable() {
                                @Override
                                public void run() {
                                    tabelaCartas.setEnabled(false);
                                    //moveOtherPlayer(move);
                                }
                            });
                        String read = in.readLine();
                        final int move = Integer.parseInt(read);


                        Log.d("RPS", "Received: " + move);
                        procMsg.post(new Runnable() {
                            @Override
                            public void run() {
                                efectuarTurnoAdversario(move);
                                //moveOtherPlayer(move);
                            }
                        });
                    }
            } catch (Exception e) {
                Log.d("Exception Server", e.toString());
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        finish();
                        Toast.makeText(getApplicationContext(),"ERRO" , Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }
    });

    Thread commThreadClient = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                output = new ObjectOutputStream(socketGame.getOutputStream());
                output.flush();
                input = new ObjectInputStream(socketGame.getInputStream());

                listaCartas = (Baralho) input.readObject();
                ia = new ImageAdapter(getApplicationContext(), nPares, listaCartas);

                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        actualizaThreads();
                        tabelaCartas.setEnabled(false);
                    }
                });

                in = new BufferedReader(new InputStreamReader(
                        socketGame.getInputStream()));
                out = new PrintWriter(socketGame.getOutputStream());

                while (!Thread.currentThread().isInterrupted()) {
                    if(listaCartas.getTurnoJogador()==2) {
                        procMsg.post(new Runnable() {
                            @Override
                            public void run() {
                                tabelaCartas.setEnabled(true);
                                //moveOtherPlayer(move);
                            }
                        });
                    }
                    else
                        procMsg.post(new Runnable() {
                            @Override
                            public void run() {
                                tabelaCartas.setEnabled(false);
                                //moveOtherPlayer(move);
                            }
                        });
                        String read = in.readLine();
                        final int move = Integer.parseInt(read);
                        Log.d("RPS", "Received: ");
                        procMsg.post(new Runnable() {
                            @Override
                            public void run() {
                                efectuarTurnoAdversario(move);
                                actualizaCartas();
                                //moveOtherPlayer();
                            }
                        });
                }
            } catch (Exception e) {
                Log.d("Exception Client", e.toString());
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        Toast.makeText(getApplicationContext(),
                                "The game was finished", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
    });

    void sendMove(final int pos){
        Thread t = new Thread(new Runnable() {
            int move = pos;

            @Override
            public void run() {
                try {
                    Log.d("RPS", "Sending a move: ");
                    out.println(pos);
                    out.flush();
                } catch (Exception e) {
                    Log.d("RPS", "Error sending a move");
                }
            }
        });
        t.start();
    }

    void moveOtherPlayer(){

    }
    public void efectuarTurno(int position) {

        if (position != posAux) {
            listaCartas.getListaCartas().get(position).setVisivel(true);

            flag++;
            // SE TIVER A 2, JOGADOR JA ESCOLHEU 2 CARTAS
            if (flag == 2) {
                flag = 0;
                if (listaCartas.getTurnoJogador() == 2) {
                    listaCartas.setTentativas2(listaCartas.getTentativas2() + 1);
                    TextView texto = (TextView) findViewById(R.id.Tentativas2);
                    texto.setText(String.valueOf(listaCartas.getTentativas2()));
                } else {
                    listaCartas.setTentativas1(listaCartas.getTentativas1() + 1);
                    TextView texto = (TextView) findViewById(R.id.Tentativas);
                    texto.setText(String.valueOf(listaCartas.getTentativas1()));
                }

                // CASO ACERTE A CARTA
                if (listaCartas.getListaCartas().get(position).getId() == idAux) {

                    // CASO ACERTE A CARTA
                    //cartaAcerta(position);
                    if (idAux < listaCartas.getnPares()) {
                        // Comeca atribuir pontuacao
                        efectuarPontuacao();
                        sendMove(position);
                    }
                    // Verifica Final de jogo
                    verificaFinal();

                    // CASO FALHE A CARTA
                } else {
                    cartaFalha(position);
                    // Inicio deduzir score
                    deduzirPontuacao();
                    sendMove(position);
                    // Inicio de Troca de Turno ( Jogador Falhou)
                    //trocaTurno();
                }
            }
            // CASO SEJA 1 CARTA
            else{
                sendMove(position);
                posAux = position;
                idAux = listaCartas.getListaCartas().get(position).getId();
                //viewAux = view;
            }
        }
        actualizaCartas();
    }

    public void efectuarTurnoAdversario(int position) {
        if (position != posAux) {
            listaCartas.getListaCartas().get(position).setVisivel(true);

            flag++;
            // SE TIVER A 2, JOGADOR JA ESCOLHEU 2 CARTAS
            if (flag == 2) {
                flag = 0;
                if (flagJogador == 2) {
                    listaCartas.setTentativas2(listaCartas.getTentativas2() + 1);
                    TextView texto = (TextView) findViewById(R.id.Tentativas2);
                    texto.setText(String.valueOf(listaCartas.getTentativas2()));
                } else {
                    listaCartas.setTentativas1(listaCartas.getTentativas1() + 1);
                    TextView texto = (TextView) findViewById(R.id.Tentativas);
                    texto.setText(String.valueOf(listaCartas.getTentativas1()));
                }

                // CASO ACERTE A CARTA
                if (listaCartas.getListaCartas().get(position).getId() == idAux) {

                    // CASO ACERTE A CARTA
                    cartaAcerta(position);
                    if (idAux < listaCartas.getnPares()) {
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
                    //trocaTurno();
                }
            }
            // CASO SEJA 1 CARTA
            else{
                posAux = position;
                idAux = listaCartas.getListaCartas().get(position).getId();
                //viewAux = view;
            }
        }
        actualizaCartas();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Baralho criaJogo(int nPares, int tema, int intrusos){

        listaCartas = new Baralho(nPares, getApplicationContext(), intrusos);

        listaCartas.setTema(tema);
        return listaCartas;
    }

    public void actualizaThreads(){
        tabelaCartas = (GridView) findViewById(R.id.gridcartas);
        tabelaCartas.setAdapter(ia);
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
                //viewComp.setVisibility(viewComp.INVISIBLE);
                //viewAux.setVisibility(viewAux.INVISIBLE);
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

        if(listaCartas.getTurnoJogador()==1)
            listaCartas.setTurnoJogador(2);
        else
            listaCartas.setTurnoJogador(1);
    }

    public void verificaFinal() {
        if (totalpares == (getIntent().getIntExtra("nPares", 1)) - getIntent().getIntExtra("intrusos", 0)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Jogo2Online.this);
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
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Jogo2Online.this);

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
                    Jogo2Online.super.onBackPressed();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    public void efectuarPontuacao(){
        if (flagJogador == 1) {
            if (bonus1 != 0)
                listaCartas.setPontuacao1(listaCartas.getPontuacao1() + (300 + (bonus1 * 200)));
            else {
                listaCartas.setPontuacao1(listaCartas.getPontuacao1()+ 300);
            }

            bonus1++;
            TextView texto1 = (TextView) findViewById(R.id.Pontuacao);
            texto1.setText(String.valueOf(listaCartas.getPontuacao1()));
            totalpares++;
        } else {
            if (bonus2 != 0)
                listaCartas.setPontuacao2(listaCartas.getPontuacao2() + (300 + (bonus1 * 200)));
            else {
                listaCartas.setPontuacao2(listaCartas.getPontuacao2() + 300);
            }

            bonus2++;
            TextView texto1 = (TextView) findViewById(R.id.Pontuacao2);
            texto1.setText(String.valueOf(listaCartas.getPontuacao2()));
            listaCartas.setTotalpares(listaCartas.getTotalpares() + 1);
        }

    }

    public void deduzirPontuacao(){
        if(listaCartas.getTurnoJogador()==1) {
            if (listaCartas.getPontuacao1() >= 50) {
                listaCartas.setPontuacao1(listaCartas.getPontuacao1() - 50);
                TextView texto1 = (TextView) findViewById(R.id.Pontuacao);
                texto1.setText(String.valueOf(listaCartas.getPontuacao1()));
            }
            bonus1 = 0;
        }else {
            if (listaCartas.getPontuacao2() >= 50) {
                listaCartas.setPontuacao2(listaCartas.getPontuacao2() - 50);
                TextView texto1 = (TextView) findViewById(R.id.Pontuacao2);
                texto1.setText(String.valueOf(listaCartas.getPontuacao2()));
            }
            bonus2 = 0;
        }

    }

    public void trocaTurno(){

        if(listaCartas.getTurnoJogador() == 2) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Jogo2Online.this);

        builder.setTitle(getApplicationContext().getString(R.string.QuerSair));

        //final EditText input = new EditText(getApplicationContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        //builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Jogo2Online.super.onBackPressed();
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
