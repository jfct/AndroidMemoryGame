package pt.isec.memoriaamov;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JFCT on 12/31/2015.
 */
public class ImageAdapter extends BaseAdapter{

    private Baralho baralhoCartas;
    private Context context;

    public ImageAdapter(Context applicationContext, int nPares, Baralho baralhoCartas){
        this.baralhoCartas = baralhoCartas;
        context = applicationContext;
    }

    @Override
    public int getCount() {
        //number of dataelements to be displayed
        return baralhoCartas.getSizeBaralho();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;
        List<Cartas> aux = baralhoCartas.getListaCartas();

        if(convertView != null){
            iv = (ImageView) convertView;
        }
        else{
            iv = new ImageView(context);
            iv.setLayoutParams(new GridView.LayoutParams(80,80));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setPadding(8,8,8,8);
        }

        String res = aux.get(position).getImagem();
        int id = context.getResources().getIdentifier("pt.isec.memoriaamov:drawable/" + res, null, null);

        iv.setImageResource(id);

        return iv;
    }

    public void Update(){
        this.notifyDataSetChanged();
    }
}
