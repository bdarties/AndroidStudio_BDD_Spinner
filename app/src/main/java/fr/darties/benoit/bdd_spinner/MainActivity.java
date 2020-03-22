package fr.darties.benoit.bdd_spinner;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // on définit les principaux objets références de notre activité  (en dehors de la méthode onCreate, c'est mieux
    Spinner spinner_pokemon;
    TextView fenetre_resultat;
    SQLiteDatabase maBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ici on va creer une base de  donnée et une table si elle n'existaient pas
        // Création ou ouverture d'une connexion vers la base de donnee
        try {
            maBase = openOrCreateDatabase("maBaseDeDonneesPokemon",MODE_PRIVATE,null);
            // on cree la table pokemon si elle n'existait pas
            maBase.execSQL("CREATE TABLE IF NOT EXISTS pokemon(" +
                    " id INTEGER PRIMARY KEY," +
                    " nom text NOT NULL," +
                    " type text);"
            );
            // on la vide (sinon on recréerait a chaque fois les pokemon a chaque nouveau lancement)
            maBase.execSQL(" delete from pokemon where 1;");
            // on la remplit de quelques elements  la table pokemon
            maBase.execSQL("insert into pokemon (id, nom, type) values (25, 'Pikachu', 'Eletrik');");
            maBase.execSQL("insert into pokemon (id, nom, type) values (26, 'Raichu', 'Eletrik');");
            maBase.execSQL("insert into pokemon (id, nom, type) values (95, 'Onix', 'Roche/sol');");
            maBase.execSQL("insert into pokemon (id, nom, type) values (143, 'Ronflex', 'Normal');");
            maBase.execSQL("insert into pokemon (id, nom, type) values (147, 'Minidraco', 'Dragon');");
            maBase.execSQL("insert into pokemon (id, nom, type) values (148, 'Draco', 'Dragon');");
            maBase.execSQL("insert into pokemon (id, nom, type) values (149, 'Dracolosse', 'Dragon');");
        }
        catch (SQLException e) {
            // s'il y a eu un probleme lors de l'exécution de la requete, on le capture
            Log.e("execSQL","Erreur SQL : " +e.getMessage());
        };

        // on associe ensuitre les références objets  aux éléments de l'activité
        spinner_pokemon  = findViewById(R.id.spinner_pkm);
        fenetre_resultat  = findViewById(R.id.resultat);

        // on crée un tableau de string appelé results qui va contenir les pokemons de la base que l'on veut dans le spinner
        // par exemple on ne va afficher que les pokemon Dragon
        final ArrayList<String> results = new ArrayList<String>();
        try {
            // on execute la requete SQL et on récupère les résultats dans un Cursor c
            Cursor c = maBase.rawQuery("Select nom from pokemon WHERE type='Dragon' order by id asc;", null);
            // on ajoute chaque ligne du cursor dans le tableau results
            while (c.moveToNext()) {
                String a = c.getString(c.getColumnIndex("nom"));
                results.add(a);
            }
        }
        catch (SQLiteException se ) {
            Log.e("rawQuery", "Probleme SQL");
        }


        // On cree un ArrayAdapter à partir de results et on sélectionne la mise en forme par defaut
        ArrayAdapter monAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, results);

        // On définit la mise en page à applique
        //monAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // on lie enfin le spinner avec l'adapteur créé
        spinner_pokemon.setAdapter(monAdapter);



        // On définit enfin ce qu'on fait quand on selectionne un pokemon du menu deroulant
        spinner_pokemon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On récupère le pokemon choisi dans une variable
                String pokemonChoisi = parent.getSelectedItem().toString();
                // on affiche le contenu de la variable dans la zone de texte resultat
                fenetre_resultat.setText(pokemonChoisi);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


    }


}
