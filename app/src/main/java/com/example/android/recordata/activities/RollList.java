package com.example.android.recordata.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.recordata.JsonAdmin;
import com.example.android.recordata.R;

import java.util.ArrayList;

public class RollList extends AppCompatActivity {

    private ArrayList<Carrete> carretes;
    private ListView listaRolls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_list);
        this.listaRolls = (ListView) findViewById(R.id.roll_list);
    }

    public void ponerCarrete(View view){
        Intent intent = new Intent(RollList.this, RollCreation.class);
        startActivity(intent);
    }

    private class MiAdaptador extends BaseAdapter {

        private ArrayList<Carrete> carretes;

        @Override
        public int getCount() {
            return this.carretes.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public MiAdaptador(ArrayList<Carrete> carretes){
            this.carretes = carretes;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.roll_info, container, false);
            }
            Log.d("Listview", "Entra al getView" + position);
            LinearLayout rollInfoView = (LinearLayout) convertView;
            Carrete carreteActual = this.carretes.get(position);
            ((TextView) rollInfoView.findViewById(R.id.nombre)).setText(carreteActual.getNombre());
            ((TextView)rollInfoView.findViewById(R.id.fechaIni)).setText(carreteActual.fechaInicio());
            ((TextView)rollInfoView.findViewById(R.id.fechaEnd)).setText(carreteActual.fechaFinal());
            //((TextView) convertView.findViewById(android.R.id.text1))
              //      .setText(getItem(position));

            Button button = (Button) rollInfoView.findViewById(R.id.boton);
            button.setTag(position);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RollList.this, RollManager.class);
                    intent.putExtra("numCarrete", (int) view.getTag());
                    startActivity(intent);
                }
            });

            return rollInfoView;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        this.carretes = JsonAdmin.getCarretes(getApplicationContext());
        this.listaRolls.setAdapter(new MiAdaptador(this.carretes));
        if(this.carretes.size() == 0){
            Intent intent = new Intent(this, RollCreation.class);
            startActivity(intent);
        }
        Button button_addRoll = findViewById(R.id.button_addRoll);
        if(this.carretes.get(this.carretes.size() - 1).isAcabado()){
            button_addRoll.setVisibility(View.VISIBLE);
        }else{
            button_addRoll.setVisibility(View.GONE);
        }
    }

}
