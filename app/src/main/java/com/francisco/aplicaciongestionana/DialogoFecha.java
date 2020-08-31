package com.francisco.aplicaciongestionana;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DialogoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    // interfaz del escuchador para el main
    EscuchadorDia escuchadorDia;

    public interface EscuchadorDia{
        public void fechaCambiada(GregorianCalendar g);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        // fecha actual
        Calendar actual= Calendar.getInstance();
        int dia=actual.get(Calendar.DAY_OF_MONTH);
        int mes=actual.get(Calendar.MONTH);
        int año=actual.get(Calendar.YEAR);



        return new DatePickerDialog(getContext(),this,año,mes,dia);

    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        escuchadorDia=(EscuchadorDia)activity ;
        super.onAttach(activity);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        GregorianCalendar g=new GregorianCalendar();
        g.set(year,month,dayOfMonth);
        escuchadorDia.fechaCambiada(g);

    }
}
