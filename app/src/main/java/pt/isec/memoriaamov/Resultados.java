package pt.isec.memoriaamov;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by JFCT on 1/6/2016.
 */



public class Resultados extends Activity {

    TextView tipojogo0, tipojogo1, tipojogo2, tipojogo3, tipojogo4;
    TextView txt0, txt1, txt2, txt3, txt4;
    TextView scr0, scr1, scr2, scr3, scr4;

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Resultados.this);

        //Usernames
        txt0 = (TextView)findViewById(R.id.txtUsername0);
        txt0.setText(sp.getString("username0", "N/A"));

        txt1 = (TextView)findViewById(R.id.txtUsername1);
        txt1.setText(sp.getString("username1", "N/A"));

        txt2 = (TextView)findViewById(R.id.txtUsername2);
        txt2.setText(sp.getString("username2", "N/A"));

        txt3 = (TextView)findViewById(R.id.txtUsername3);
        txt3.setText(sp.getString("username3", "N/A"));

        txt4 = (TextView)findViewById(R.id.txtUsername4);
        txt4.setText(sp.getString("username4", "N/A"));

        //Scores
        scr0 = (TextView)findViewById(R.id.txtScore0);
        scr0.setText(String.valueOf(sp.getInt("score0", 0)));

        scr1 = (TextView)findViewById(R.id.txtScore1);
        scr1.setText(String.valueOf(sp.getInt("score1", 0)));

        scr2 = (TextView)findViewById(R.id.txtScore2);
        scr2.setText(String.valueOf(sp.getInt("score2", 0)));

        scr3 = (TextView)findViewById(R.id.txtScore3);
        scr3.setText(String.valueOf(sp.getInt("score3", 0)));

        scr4 = (TextView)findViewById(R.id.txtScore4);
        scr4.setText(String.valueOf(sp.getInt("score4", 0)));

        //Tipo de Jogo
        tipojogo0 = (TextView)findViewById(R.id.txtJogo0);
        tipojogo0.setText(sp.getString("jogo0", "N/A"));

        tipojogo1 = (TextView)findViewById(R.id.txtJogo1);
        tipojogo1.setText(sp.getString("jogo1", "N/A"));

        tipojogo2 = (TextView)findViewById(R.id.txtJogo2);
        tipojogo2.setText(sp.getString("jogo2", "N/A"));

        tipojogo3 = (TextView)findViewById(R.id.txtJogo3);
        tipojogo3.setText(sp.getString("jogo3", "N/A"));

        tipojogo4 = (TextView)findViewById(R.id.txtJogo4);
        tipojogo4.setText(sp.getString("jogo4", "N/A"));

    }
}
