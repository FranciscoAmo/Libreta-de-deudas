package com.francisco.aplicaciongestionana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CrearNuevaCuenta extends AppCompatActivity implements DialogoHora.EscuchadorHora,DialogoFecha.EscuchadorDia {

    private EditText nombre;
    private TextView fecha;
    private Spinner cantidad;
    private Button seleccionarfecha;
    private Button confirmar;

    DialogoHora dialogohora;
    DialogoFecha dialogofecha;

    String horatexto="";
    String fechatexto="";
    String cantidadtexto="";



    // array de cantidades
    String[] cantidades={"10","20","30","40","50"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nueva_cuenta);
        _init();
    }

    // vincula datos
    public void _init(){
        confirmar=(Button)findViewById(R.id.buttonconfirmar);
        nombre=(EditText)findViewById(R.id.nombreclientenueva);
        fecha =(TextView) findViewById(R.id.fechanueva);
        cantidad=(Spinner)findViewById(R.id.spinnerCantidad);
        seleccionarfecha=(Button)findViewById(R.id.buttonfecha);
        seleccionarfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              dialogofecha=new DialogoFecha();
              dialogofecha.show(getSupportFragmentManager(),"fecha");

              dialogohora=new DialogoHora();
              dialogohora.show(getSupportFragmentManager(),"hora");


            }
        });

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // obtengo los datos
                String nombreintroducido=nombre.getText().toString();
                String fechafinal=fecha.getText().toString();
                String cantidadtexto=cantidad.getSelectedItem().toString();

                // si los datos se an introducido sigo si no no hago nada
                if(!nombreintroducido.equalsIgnoreCase("") && !fechafinal.equalsIgnoreCase("") && nombreintroducido!=null && fechafinal!=null){

                    // SI SE SELECCIONA ALGO SE CREA OTRO INTENT PARA VORLVER A LA APLICACON Y SE ENVIA UN EXTRA CON UN CODIGO
                    // EN ESTE CASO SELECCIONADO CON EL ITEM DE LA LISTA
                    Intent i = new Intent();
                    i.putExtra("nombre",nombreintroducido );
                    i.putExtra("fecha",fechafinal );
                    i.putExtra("cantidad",cantidadtexto );

                    // ESTE SET SESULT INDICA COMO ACABO LA ACTIVIDAD CON rESULT_OK O rESULT_CANCELED Y EL INTENT
                    setResult(RESULT_OK, i);
                    // TERMINAMOS LA APLICACION
                    finish();



                }else{

                    Snackbar.make(getCurrentFocus(),"Debes introducir un nombre y/o fecha para completar",Snackbar.LENGTH_SHORT).show();
                }



            }
        });


// adaptador para rellenar spinner=> primero contexto, despues tipo de spinner, por ultimo texto en vector que se va a mostrar
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.spiner_cantidad,cantidades);
        cantidad.setAdapter(adaptador);
    }





    // escuchadores del datepicker y el Timepicker primero pedire el Timepicker y despues el la fecha y lo edito en el texto
    @Override
    public void fechaCambiada(GregorianCalendar g) {
        fechatexto=g.get(Calendar.DAY_OF_MONTH)+"|"+g.get(Calendar.MONTH)+"|"+g.get(Calendar.YEAR);
        fecha.setText(horatexto+fechatexto);
    }



    @Override
    public void horaccambiada(GregorianCalendar g) {
        if(g.get(Calendar.MINUTE)<10){
            horatexto=g.get(Calendar.HOUR) +":" + g.get(Calendar.MINUTE)+"0 - ";

        }else{
            horatexto=g.get(Calendar.HOUR) +":" + g.get(Calendar.MINUTE)+" - ";


        }
    }





}
