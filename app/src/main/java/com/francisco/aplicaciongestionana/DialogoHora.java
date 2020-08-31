package com.francisco.aplicaciongestionana;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DialogoHora extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    EscuchadorHora escuchadorhora;

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        GregorianCalendar g=new GregorianCalendar();
         g.set(Calendar.HOUR,hourOfDay);
         g.set(Calendar.MINUTE,minute);
         escuchadorhora.horaccambiada(g);


    }

    public interface EscuchadorHora{
         void horaccambiada(GregorianCalendar g);

    }

    @Override
    public void onAttach(@NonNull Context activity) {
        super.onAttach(activity);
        escuchadorhora = (EscuchadorHora) activity;


    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
                    final Calendar actual=Calendar.getInstance();

                    int hora=actual.get(Calendar.HOUR);
                    int minutos=actual.get(Calendar.MINUTE);



              return new TimePickerDialog(
                      getActivity(), this,hora,minutos,true);



    }
}
