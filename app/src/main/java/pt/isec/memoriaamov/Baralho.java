package pt.isec.memoriaamov;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by JFCT on 12/31/2015.
 */
public class Baralho implements Serializable{
    List<Cartas> listaCartas = new ArrayList<Cartas>();
    int intrusos = 0;
    int nPares;
    int id=0;
    int tentativas1 =0;
    int tentativas2 =0;
    int pontuacao1 =0;
    int pontuacao2 =0;
    int totalpares =0;
    int turnoJogador = 0;

    public transient Context context;

    Baralho(){};

   Baralho(int nPares, Context context, int intrusos){
        for(int i = 0; i < nPares; i++){
            listaCartas.add(new Cartas(id));
            listaCartas.add(new Cartas(id));
            id++;
        }
       this.context = context;
       this.intrusos = intrusos;
       this.nPares = nPares;
    }

    public void setTentativas1(int i){tentativas1 = i;};
    public void setTentativas2(int i){tentativas2 = i;};
    public void setPontuacao1(int i){ pontuacao1 = i;};
    public void setPontuacao2(int i){ pontuacao2 = i;};
    public void setTotalpares(int i) {totalpares = i;};
    public void setTurnoJogador(int i){turnoJogador= i;};

    public int getTurnoJogador(){return turnoJogador;};
    public int getTotalpares(){return totalpares;};
    public int getTentativas1(){ return tentativas1;};
    public int getTentativas2(){return tentativas2;};
    public int getPontuacao1(){ return pontuacao1;};
    public int getPontuacao2(){ return pontuacao2;};

    public List<Cartas> getListaCartas(){ return listaCartas; }
    public int getnPares(){return nPares;};
    public int getSizeBaralho(){
        return listaCartas.size();
    }

    public void setnPares(int nPares){ this.nPares = nPares;}

    public void setTema(int tema){
        String nome, nomeOriginal= "a";
        int a=1;

        if(tema == 1){
            nomeOriginal="fruta";
        }
        if(tema == 2){
            nomeOriginal="logo";
        }

        for(int i = 0 ; i < listaCartas.size(); i+=2){
            nome = nomeOriginal + a;
            listaCartas.get(i).setImagem(nome);
            listaCartas.get(i+1).setImagem(nome);
            a++;
        }

        setIntrusos(tema);
        Collections.shuffle(listaCartas);
    }

    public void setIntrusos(int t){
        String nome="a";
        boolean flag = true, flagArray = false ;
        int numero=0;
        List<Integer> auxiliar = new ArrayList<Integer>();

        if(t == 1){
            nome="logo";
        }
        if(t == 2){
            nome="fruta";
        }

        for(int i = 1; i <= intrusos; i++) {
            Random rn = new Random();
            // Verifica que não foi inserido o mesmo membro
            while(flag) {
                int random = rn.nextInt(nPares - 1 + 1) + 1;
                // Verificar se Array já se encontra inicializado ou não
                if(!flagArray)
                {
                    auxiliar.add(random);
                    flagArray = true;
                    flag = false;
                    numero = random;
                }else {
                    flag = false;
                    for (int j = 0; j < auxiliar.size(); j++) {
                        if (auxiliar.get(j) == random)
                            flag = true;
                        }
                    if(!flag){
                        flag = false;
                        auxiliar.add(random);
                        numero = random;
                    }
                }
            }
            listaCartas.get((((numero * 2) - 2))).setId(nPares + i);
            listaCartas.get((((numero * 2) - 2))).setImagem(nome+numero);
            listaCartas.get((((numero * 2) - 2) + 1)).setId(nPares + i);
            listaCartas.get((((numero * 2) - 2) + 1)).setImagem(nome+(numero));
            flag = true;
        }
    }
}
