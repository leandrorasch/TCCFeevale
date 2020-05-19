package com.example.efs.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.efs.R;
import com.example.efs.alarmeActivity;
import com.example.efs.conexao.ConexaoWS;
import com.example.efs.estruturas.Alarme;

import java.util.ArrayList;

public class listAlarmesAdapter extends BaseAdapter {

    ArrayList<Alarme> lista = new ArrayList();
    LayoutInflater inflater;
    Alarme alarme;

    public listAlarmesAdapter (Context ctx, ArrayList<Alarme> lista){
        inflater = LayoutInflater.from(ctx);
        this.lista = lista;
    }

    public ArrayList<Alarme> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Alarme> lista) {
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId_alarme();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.activity_list_alarmes, parent, false);
        final TextView txtDispositivo = (TextView) v.findViewById(R.id.txtLAlarmeDispositivo);
        TextView txtTopico      = (TextView) v.findViewById(R.id.txtLAlarmeTopico);
        TextView txtFaixa       = (TextView) v.findViewById(R.id.txtLAlarmeFaixa);
        Switch   swiAtivo       = (Switch)   v.findViewById(R.id.SwiLAlarmeAtivo);
        alarme = lista.get(position);

        txtDispositivo.setText(lista.get(position).getDispositivo().getDs_dispositivo());
        txtTopico.setText     (lista.get(position).getTopico().getDs_topico());
        txtFaixa.setText      (lista.get(position).getVlr_minimo().toString()+"-"+
                               lista.get(position).getVlr_maximo().toString());

        if (lista.get(position).getFl_ativo() == "S"){
            swiAtivo.setChecked(true);
        }else{
            swiAtivo.setChecked(false);
        }


        // Set up On checked change listener for the switch
        /*swiAtivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {

                if (isChecked) {
                    Log.i("EFSAPP", txtDispositivo.getText().toString() + alarme.getId_alarme().toString());
                    alarme.setFl_ativo("S");
                    new atualizaFlag().execute();
                }else{
                    Log.i("EFSAPP", txtDispositivo.getText().toString() + alarme.getId_alarme().toString());
                    alarme.setFl_ativo("N");
                    new atualizaFlag().execute();
                }

            }});
*/
        return v;
    }
}
