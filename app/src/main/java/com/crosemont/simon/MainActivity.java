package com.crosemont.simon;



import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.crosemont.simon.modele.CouleurCellule;
import com.crosemont.simon.modele.Jeu;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant l'activité principal de l'application Simon, elle permet la gestions des click dans l'interface utilisateur
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ImageButton> listeBoutonOrdinateur;
    private ArrayList<ImageButton> listeBoutonJoueur;
    private TextView cadreScore;
    private ArrayList<CouleurCellule> sequence;
    private Jeu jeu;
    private ImageButton boutonJaune;
    private ImageButton boutonRouge;
    private ImageButton boutonBleu;
    private ImageButton boutonVert;
    private ImageButton boutonChoisie;
    private boolean tourJoueur;
    private int positionSequence;
    private ImageButton boutonPlay;
    private int niveau;
    private RadioGroup groupeRadioNiveau;
    private TextView cadreIdentiteTour;
    private MediaPlayer lecteurSonBleu;
    private MediaPlayer lecteurSonRouge;
    private MediaPlayer lecteurSonVert;
    private MediaPlayer lecteurSonJaune;
    private MediaPlayer lecteurSonErreur;


    /**
     * Méthode onCreate() redéfinie , celle ci associe les variables et leur bouton ou media correspondant.
     * Elle redefinie aussi les écouteurs onClickListener() pour les boutons "play" et séléction de niveau.
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Detection des boutons

        lecteurSonBleu = MediaPlayer.create(this, R.raw.piano_bleu);
        lecteurSonRouge = MediaPlayer.create(this, R.raw.piano_rouge);
        lecteurSonJaune = MediaPlayer.create(this, R.raw.piano_jaune);
        lecteurSonVert = MediaPlayer.create(this, R.raw.piano_vert);
        lecteurSonErreur = MediaPlayer.create(this,R.raw.son_wrong);

        boutonJaune = findViewById(R.id.boutonJaune);
        boutonRouge = findViewById(R.id.boutonRouge);
        boutonBleu = findViewById(R.id.boutonBleu);
        boutonVert = findViewById(R.id.boutonVert);
        cadreIdentiteTour = findViewById(R.id.identiteTour);
        cadreScore = findViewById(R.id.cadreScore);

        // Instanciation du jeu
        jeu = new Jeu();
        tourJoueur = false;
        positionSequence = 0;
        niveau = 500;
        listeBoutonOrdinateur = new ArrayList<ImageButton>();
        listeBoutonJoueur = new ArrayList<ImageButton>();
        sequence = new ArrayList<CouleurCellule>();

        boutonPlay = findViewById(R.id.play);
        boutonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String score = Integer.toString(jeu.getScore());
                cadreScore.setText(score);
                boutonJaune.setImageResource(R.drawable.b_jaune_foncer);
                boutonRouge.setImageResource(R.drawable.b_rouge_foncer);
                boutonBleu.setImageResource(R.drawable.b_bleu_foncer);
                boutonVert.setImageResource(R.drawable.b_vert_foncer);
                boutonPlay.setEnabled(false);
                groupeRadioNiveau.setEnabled(false);
                activerBouton();
                jouerTourOrdinateur();
            }
        });
        groupeRadioNiveau = findViewById(R.id.groupeNiveau);
        groupeRadioNiveau.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton boutonNiveau = (RadioButton) group.findViewById(checkedId);
                if (boutonNiveau.getTag().equals("facile")) {
                    niveau = 1500;
                } else if (boutonNiveau.getTag().equals("moyen")) {
                    niveau = 1000;
                } else if (boutonNiveau.getTag().equals("difficile")) {
                    niveau = 500;
                }
            }
        });
    }

    /**
     * Méthode onClick(View view) redéfinie pour les bouton representant les couleurs du simon, celle ci verifie
     * l'état de la partie et compare les liste de couleur entre celle de l'ordinateur et du joueur
     * @param view
     */

    public void onClick(View view) {
        if (tourJoueur = true) {
            String tag = (String) view.getTag();

            if (tag.equals("rouge")) {
                boutonChoisie = boutonRouge;
            } else if (tag.equals("bleu")) {
                boutonChoisie = boutonBleu;
            } else if (tag.equals("vert")) {
                boutonChoisie = boutonVert;
            } else if (tag.equals("jaune")) {
                boutonChoisie = boutonJaune;
            }
            allumerUnBouton(boutonChoisie);

            if (listeBoutonOrdinateur.get(positionSequence).getTag().equals(boutonChoisie.getTag())) {

                cadreScore.setText(Integer.toString(jeu.getScore()));
                listeBoutonJoueur.add((ImageButton) view);
                positionSequence += 1;

                if (positionSequence == listeBoutonOrdinateur.size()) {
                    jeu.ajusterScore(1);
                    cadreScore.setText(Integer.toString(jeu.getScore()));
                    jouerTourOrdinateur();
                }

            } else {
                lecteurSonErreur.start();
                afficheScoreFin(jeu.getScore());
            }

        } else {
            listeBoutonJoueur = new ArrayList<ImageButton>();

            jouerTourOrdinateur();
        }
    }

    /**
     * Méthode se servant d'un countDownTimer() afin d'allumer un seul bouton sur un court laps de temps.
     * @param button le bouton à allumer.
     */

    private void allumerUnBouton(ImageButton button) {
        jouerSonBouton(button);
        new CountDownTimer(250, 125) {
            @Override
            public void onTick(long l) {
                button.setImageResource(getCouleurClair(button));

            }

            @Override
            public void onFinish() {
                button.setImageResource(getCouleurFoncer(button));
                arretterSonBouton(button);
            }
        }.start();

    }

    /**
     * Méthode se servant d'un countDownTimer() afin d'allumer une serie de bouton sur un court laps de temps.
     * @param buttons la liste de bouton à allumer.
     */

    private void allumerSequenceBouton(List<ImageButton> buttons) {
        desactiverBouton();
        cadreIdentiteTour.setText("Tour de l'ordinateur");
        int taille = (buttons.size() * (niveau * 2)) + niveau;
        new CountDownTimer(taille, niveau) {
            int i = -1;
            Boolean allumer = true;
            @Override
            public void onTick(long l) {


                if (allumer == false) {
                    buttons.get(i).setImageResource(getCouleurClair(buttons.get(i)));
                    jouerSonBouton(buttons.get(i));
                    allumer = true;

                } else {
                    if (i == -1) {
                        i++;
                    } else {
                        buttons.get(i).setImageResource(getCouleurFoncer(buttons.get(i)));
                        arretterSonBouton(buttons.get(i));
                        if (i != buttons.size() - 1) {
                            i++;
                        }
                    }
                    allumer = false;
                }
            }
            @Override
            public void onFinish() {
                cadreIdentiteTour.setText("A votre tour !");
                activerBouton();
            }
        }.start();

    }

    /**
     * Méthode servant a déterminer le drawable (sombre) selon le bouton passé en parametre
     * @param bouton le bouton a analyser
     * @return Un entier (int) correspondant au ResId du drawable nécéssaire
     */


    private int getCouleurFoncer(ImageButton bouton) {
        int couleur;
        couleur = -1;

        String tag = (String) bouton.getTag();

        if (tag.equals("rouge")) {
            couleur = R.drawable.b_rouge_foncer;
        }
        if (tag.equals("vert")) {
            couleur = R.drawable.b_vert_foncer;
        }
        if (tag.equals("bleu")) {
            couleur = R.drawable.b_bleu_foncer;
        }
        if (tag.equals("jaune")) {
            couleur = R.drawable.b_jaune_foncer;
        }
        return couleur;
    }

    /**
     * Méthode servant a déterminer le drawable (clair) selon le bouton passé en parametre
     * @param bouton le bouton a analyser
     * @return Un entier (int) correspondant au ResId du drawable nécéssaire
     */

    private int getCouleurClair(ImageButton bouton) {
        int couleur;
        couleur = -1;

        String tag = (String) bouton.getTag();

        if (tag.equals("rouge")) {
            couleur = R.drawable.b_rouge_clair;
        }
        if (tag.equals("vert")) {
            couleur = R.drawable.b_vert_clair;
        }
        if (tag.equals("bleu")) {
            couleur = R.drawable.b_bleu_clair;
        }
        if (tag.equals("jaune")) {
            couleur = R.drawable.b_jaune_clair;
        }
        return couleur;
    }

    /**
     * Méthode faisant jouer l'ordinateur, en affichant sa séquence et en ajoutant une nouvelle couleur a la séquence
     */


    private void jouerTourOrdinateur() {
        CouleurCellule celluleAlleatoire = jeu.genererCouleurAlleatoire();
        sequence.add(celluleAlleatoire);
        listeBoutonOrdinateur = (ArrayList<ImageButton>) convertirListeCouleurEnBouton(sequence).clone();
        allumerSequenceBouton(listeBoutonOrdinateur);
        tourJoueur = true;
        positionSequence = 0;


    }

    /**
     * Cette méthode permet d'obtenir une liste de bouton représentants des couleurs à partir d'un tableau de
     * cellule donné en paramètre.
     * @param listeDeCouleur La liste de couleur a convertir
     * @return Une liste de ImageButton correspondant
     */

    private ArrayList<ImageButton> convertirListeCouleurEnBouton(ArrayList<CouleurCellule> listeDeCouleur) {
        ArrayList<ImageButton> listeDeBoutonConvertis = new ArrayList<ImageButton>();
        for (CouleurCellule cellule : listeDeCouleur) {
            if (cellule.equals(CouleurCellule.VERT)) {
                listeDeBoutonConvertis.add(boutonVert);
            } else if (cellule.equals(CouleurCellule.BLEU)) {
                listeDeBoutonConvertis.add(boutonBleu);
            } else if (cellule.equals(CouleurCellule.ROUGE)) {
                listeDeBoutonConvertis.add(boutonRouge);
            } else if (cellule.equals(CouleurCellule.JAUNE)) {
                listeDeBoutonConvertis.add(boutonJaune);
            }

        }
        return listeDeBoutonConvertis;
    }

    /**
     * Cette méthode génére l'affichage d'une boite de dialogue indiquant la fin de partie accompagnée
     * d'un bouton permettant le reset du jeu
     * @param score le score de fin de partie (entier)
     */

    private void afficheScoreFin(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PERDU ! \n Votre score est " + score);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> handleReset());

        builder.show();

    }

    /**
     * Méthode gérant la réinitialisation du jeu aprés une défaite remettant toute les variable a 0
     */

    private void handleReset() {
        boutonPlay.setEnabled(true);
        desactiverBouton();
        jeu.setScore(0);
        sequence = new ArrayList<CouleurCellule>();
        listeBoutonJoueur = new ArrayList<ImageButton>();
        listeBoutonOrdinateur = new ArrayList<ImageButton>();
        cadreIdentiteTour.setText("Appuyer sur jouer !");
        String score = Integer.toString(jeu.getScore());
        cadreScore.setText(score);
        groupeRadioNiveau.setEnabled(true);
    }

    /**
     * Méthode activant tout les bouton de couleur pour qu'ils soient intéractifs
     */

    private void activerBouton() {
        boutonRouge.setEnabled(true);
        boutonVert.setEnabled(true);
        boutonBleu.setEnabled(true);
        boutonJaune.setEnabled(true);
    }

    /**
     * Méthode désactivant tout les bouton de couleur pour qu'ils ne soient plus intéractifs
     */

    private void desactiverBouton() {
        boutonRouge.setEnabled(false);
        boutonVert.setEnabled(false);
        boutonBleu.setEnabled(false);
        boutonJaune.setEnabled(false);
    }

    /**
     * Méthode qui permet de lire un fichier sonore selon le bouton qui a été donné en paramètre
     * @param bouton le bouton a analyser
     */

    private void jouerSonBouton(ImageButton bouton) {
        if (bouton.getTag().equals("rouge")) {
            lecteurSonRouge.start();
        } else if (bouton.getTag().equals("bleu")) {
            lecteurSonBleu.start();
        } else if (bouton.getTag().equals("jaune")) {
            lecteurSonJaune.start();
        } else if (bouton.getTag().equals("vert")) {
            lecteurSonVert.start();
        }
    }

    /**
     * Méthode qui permet de mettre en pause la lecture d'un fichier sonore selon le bouton qui a été donné en paramètre,
     * celle ci réinitialise aussi la lecture
     * @param bouton le bouton a analyser
     */

    private void arretterSonBouton(ImageButton bouton) {
        if (bouton.getTag().equals("rouge")) {
            lecteurSonRouge.pause();
            lecteurSonRouge.seekTo(0);
        } else if (bouton.getTag().equals("bleu")) {
            lecteurSonBleu.pause();
            lecteurSonBleu.seekTo(0);
        } else if (bouton.getTag().equals("jaune")) {
            lecteurSonJaune.pause();
            lecteurSonJaune.seekTo(0);
        } else if (bouton.getTag().equals("vert")) {
            lecteurSonVert.pause();
            lecteurSonVert.seekTo(0);
        }
    }
}