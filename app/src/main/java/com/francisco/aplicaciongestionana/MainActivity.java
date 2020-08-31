package com.francisco.aplicaciongestionana;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;


// no se puede escribir en un raw debo usar el almacenamiento interno
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView lista;
    FloatingActionButton fab;

    // clases del adapatador
    TextView idCliente;
    TextView fechaadap;
    TextView cantidadadap;

    // clases del menu
    SearchView busqueda;

    // arrayList de los clientes
    ArrayList<Cliente> listaCuentas=new ArrayList<>();
    ArrayList<Cliente> listaBusqueda=new ArrayList<>();
    ArrayList<Cliente> listaaux=new ArrayList<>();

    // clase para leer un json y manipularlo
    Gson gson=new Gson();

    //string donde meto el json
    String json;

    // esta es la referencia de la actividad
    private int INTENTO_NUEVO_CLIENTE=1;

    // booleano para indicar que hemos buscado algo
    boolean realiadabusqueda=false;

    // elemento searchView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // creo el primer fichero
        crearPrimerFichero();


        // vinculo los elementos del diseño
        _init();

        // establezco los on click listener de elementos
        establecerAccionesBotones();

        // leo el fichero
        json=readtexto();

        // lo convierto a clase y lo introduzco en el array para el listview
        listaCuentas=convertirAClases(json);

        // relleno el listview
        rellenarListview(listaCuentas);


        lista.setOnItemClickListener(this);
    }






    // escribir fichero de almacenamiento interno para meter los datos
    public void crearPrimerFichero(){
        File f=new File(getApplicationContext().getFilesDir(),"datos.json");
        // si no existe lo creo
        if(!f.exists()) {
            String filename = "datos.json";
            String fileContents = "[]";
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // relleno el listview con los datos del arraylist

    public void rellenarListview(ArrayList<Cliente> listausar) {
        // edito el listview
        AdaptadorPersonalizado adaptador = new AdaptadorPersonalizado(getApplicationContext(), listausar);
        lista.setAdapter(adaptador);
    }

    public void establecerAccionesBotones(){
        // boton flotante
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicio la actividad de crear una cuenta
                Intent intent=new Intent(getApplicationContext(),CrearNuevaCuenta.class);
                startActivityForResult(intent,INTENTO_NUEVO_CLIENTE);

            }
        });

    }



    // convierto el texto del json a un arraylist
   public ArrayList<Cliente> convertirAClases(String ficheroEnTexto){
       Cliente[] clientess;
       ArrayList<Cliente> listcliente;
       clientess= new Gson().fromJson(ficheroEnTexto,Cliente[].class);
       listcliente = new ArrayList<>(Arrays.asList(clientess));

       return listcliente;

   }


// lee el fichero y devuelve un string del fichero
    public String readtexto() {
        String texto="";
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("datos.json")));

             texto = fin.readLine();
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }



        return texto;
    }






    public void _init(){
        lista=(ListView)findViewById(R.id.listalayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("Busqueda cliente");

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(realiadabusqueda==true){
                    listaCuentas=listaaux;
                    rellenarListview(listaCuentas);

                    realiadabusqueda=false;
                }

                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listaBusqueda.clear();
                for (Cliente cliente : listaCuentas) {
                    String nombreclienteaux = cliente.nombreCliente;
                    if (nombreclienteaux.contains(query)) {
                        listaBusqueda.add(cliente);

                    }

                }

                Toast.makeText(MainActivity.this, "existen " + listaBusqueda.size() + " cuentas para este cliente", Toast.LENGTH_SHORT).show();



                searchView.setQuery("",false);
                listaaux=listaCuentas;
                listaCuentas=listaBusqueda;
                rellenarListview(listaCuentas);


                // marco el booleano
                realiadabusqueda=true;
                    return true;

            }
            @Override
            public boolean onQueryTextChange(String newText) {


                return true;
            }


        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(realiadabusqueda){
            String nombre = listaaux.get(position).getNombreCliente();
            String fecha = listaaux.get(position).getFechaconsulta();

            Instrucciones(nombre, fecha, position).show();

        }else {
            String nombre = listaCuentas.get(position).getNombreCliente();
            String fecha = listaCuentas.get(position).getFechaconsulta();

            Instrucciones(nombre,fecha, position).show();
        }

    }


    public AlertDialog Instrucciones(final String nombre,final String fecha, final int position){



        AlertDialog.Builder constructor=new AlertDialog.Builder(this);

        constructor.setTitle("AVISO");
        constructor.setMessage("Quieres eliminar la cuenta : \nnombre: "+nombre+"\nfecha: "+fecha);
        constructor.setPositiveButton("Si eliminala", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // elimina de la lista de cuenta del arraylist no del fichero cuando cierro la actividad reescribo el fichero
               if(realiadabusqueda==true){
                    for(Cliente cli:listaaux) {
                       if(listaCuentas.get(position).getNombreCliente().equals(cli.getNombreCliente())&&
                                listaCuentas.get(position).getCantidad()==cli.getCantidad() &&
                                listaCuentas.get(position).getFechaconsulta().equals(cli.getFechaconsulta())){

                           listaaux.remove(cli);
                           break;
                       }

                    }
                   searchView.setIconified(true);

               }else {
                   listaCuentas.remove(position);
               }
                // cierro la ventana
                dialog.dismiss();
                // renuevo la vista
                rellenarListview(listaCuentas);



            }
        });
        constructor.setNegativeButton("No elimines", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
            }
        });


        return constructor.create();
    }

// reescribir fichero
    public void escribirfichero(ArrayList<Cliente> lista){
        try {
            FileOutputStream fileout=openFileOutput("datos.json", MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            String json = new Gson().toJson(lista);
            outputWriter.write(json);
            outputWriter.close();



        } catch (Exception e) {
            e.printStackTrace();

            File file = new File(getApplication().getFilesDir(), "datos.json");

            // creo un string con los nuevos datos


        }

    }


    // SE DEBE SOBREESCRIBIR ON ACTIVIRY RESULT INDICANDO LO QUE HACE Y COMO RESULTA EL RESULTADO
    // REQUESTcODE INDICA EL CODIGO DEL INTENT (ESTA AL PRINCIPIO) EL RESULTcODE INDICA COMO ACABO LA ACTIVIDAD
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENTO_NUEVO_CLIENTE) {
            if (resultCode == RESULT_OK) {

                // obtengo los datos del cliente lo meto en un objeto cliente y lo meto en el arrylist

                String nombre=data.getStringExtra("nombre");
                int cantidad=Integer.parseInt(data.getStringExtra("cantidad"));
                String fecha=data.getStringExtra("fecha");
                // creo un objeto cliente
                listaCuentas.add(new Cliente(nombre,cantidad,fecha));
                rellenarListview(listaCuentas);

                // mientras escribo el archivo
            }
            if (resultCode == RESULT_CANCELED) {
                // no hago nada


            }


        }
    }

    public void escribirArchivo(){



    }




    // adaptador para mostrar toda la lista de clientes
    public class AdaptadorPersonalizado extends ArrayAdapter {


        public AdaptadorPersonalizado(@NonNull Context context, @NonNull ArrayList<Cliente> cuentas) {
            super(context, R.layout.adaptador_lista, cuentas);
            context=getContext();
            cuentas=listaCuentas;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View adaptadorimagen=inflater.inflate(R.layout.adaptador_lista,null);

            // cambio el color uno de cada campo
            if(position==0 || position%2==0) {

                adaptadorimagen.setBackgroundColor(Color.BLACK);

            }

                // ojo lo debo buscar a partir del view del adaptador no del principal en este caso "luchadores"
                idCliente = (TextView) adaptadorimagen.findViewById(R.id.idNombre);
                idCliente.setText(listaCuentas.get(position).getNombreCliente());



                fechaadap = (TextView) adaptadorimagen.findViewById(R.id.fechaconsulta);
                fechaadap.setText(listaCuentas.get(position).getFechaconsulta());

                cantidadadap = (TextView) adaptadorimagen.findViewById(R.id.cantidadspiner);
                cantidadadap.setText("" + listaCuentas.get(position).getCantidad()+" €");

            if(position==0 || position%2==0) {

               idCliente.setTextColor(Color.WHITE);
                fechaadap.setTextColor(Color.WHITE);
                cantidadadap.setTextColor(Color.WHITE);
            }

            return  adaptadorimagen;
        }


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        String texto_a_guardar=pasarAjson(listaCuentas);
        escribirFichero(texto_a_guardar);
        Toast.makeText(getBaseContext(),"guardando...",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        String texto_a_guardar=pasarAjson(listaCuentas);
        escribirFichero(texto_a_guardar);
        super.onPause();

    }

    // metodo para escribir un fichero
    public void escribirFichero(String textoenjson){
        {
            OutputStreamWriter escritor=null;
            try
            {
                escritor=new OutputStreamWriter(openFileOutput("datos.json", Context.MODE_PRIVATE));
                escritor.write(textoenjson);
            }
            catch (Exception ex)
            {
                Log.e("ivan", "Error al escribir fichero a memoria interna");
            }
            finally
            {
                try {
                    if(escritor!=null)
                        escritor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    // paso a json en texto
    public String pasarAjson(ArrayList<Cliente> miLista){
        String textoenjson;
        Gson gson = new Gson();
        textoenjson = gson.toJson(miLista);


        return textoenjson;
    }





}
