package pt.isec.memoriaamov;

import android.widget.Button;
import android.widget.ImageView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JFCT on 12/30/2015.
 */
public class Cartas implements Serializable {
    int id;
    int tema;
    int intruso;

    String nome;

    boolean visivel;


    Cartas(int id) {
        this.id = id;
        this.visivel = false;
    }

    public void setVisivel(boolean bool){
        this.visivel = bool;
    }
    public void setId(int i){
        this.id = i;
    }
    public void setImagem(String nome){
        this.nome = nome;
    }
    public void setIntruso(int i){intruso = i;};

    public int getIntruso(){return intruso;};
    public int getId(){ return id; }
    public String getImagem(){
        if(visivel)
            return nome;
        else
            return "cardback";
    }
}
