package biln.notreappeventful3;


        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.DialogFragment;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.DatePicker;

        import java.util.Calendar;
        import java.util.Date;

/**
 * Created by Nassymo on 24/03/2015.
 */
public class SelecteurDateFin extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    //creation d'un listener sur la date (une variable)
    private ListenerDateFin Listenerdp;
    public static final int ID_SELECTEUR = 2 ;

    //definition de notre interface, qui continendra la méthode qui renverra date à notre activité MainActivity
    public interface ListenerDateFin {
        public void onDateSet(int ID_SELECTEUR, Date date);
    }

    //getter du listener
    public ListenerDateFin getListenerDate() {
        return this.Listenerdp;
    }

    //setter pour notre listener à partir d'un autre passé en paramètre
    public void setListenerDateFin(ListenerDateFin listener) {
        this.Listenerdp = listener;
    }

    //constructeur de fragment qui retourne une instance de classe de SelecteurDateDebout (de type DialogFragment)

    public static SelecteurDateFin nouvelleInstanceSelecteurDateFin(ListenerDateFin listener) {
        SelecteurDateFin mon_fragment_fin = new SelecteurDateFin();
        mon_fragment_fin.setListenerDateFin(listener);
        return mon_fragment_fin;
    }

    //modification de l'attribut Listenerdp
    public void modifierListener(Date date) {
        if(this.Listenerdp != null) {
            this.Listenerdp.onDateSet(ID_SELECTEUR,date);
        }
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // La date par défaut du dialogue avec le selecteur de date est la date courante
        final Calendar c = Calendar.getInstance();
        int annee = c.get(Calendar.YEAR);
        int mois = c.get(Calendar.MONTH);
        int jour = c.get(Calendar.DAY_OF_MONTH);


        //créer une instance de Date, initialisée avec la date courante
        Date date_actuelle = new Date() ;

        DatePickerDialog mon_dialogue = new DatePickerDialog(getActivity(), this, annee, mois, jour);
        Log.d("DATE", "DatePicker crée avec succès") ;

        // définir la date minimum du Dialog à date courante (on peut pas rechercher des évènements passés)
        DatePicker date_selecteur = mon_dialogue.getDatePicker() ;
        date_selecteur.setMinDate(date_actuelle.getTime()) ;

        return mon_dialogue ;

    }

    public void onDateSet(DatePicker view, int annee_set, int mois_set, int jour_set) {

        // creation d'une instance de Date avec données récupérées - dans la classe Date, année numérotée à partir de 1900
        Date ma_date_set_fin = new Date(annee_set-1900, mois_set, jour_set) ;

        modifierListener(ma_date_set_fin);

        // Autres instructions à faire quand une date est selectionnée - mettre en relation avec recherche dans appli (query)
    }

}