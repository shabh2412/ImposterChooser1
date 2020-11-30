package com.example.imposterchooser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.imposterchooser.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String[] roles;
    ArrayList<Player> players = new ArrayList<>();
    ActivityMainBinding binding;

    int choice;
    int rand;
    int count = 0;
    int playerCount;
    boolean impostorChosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        roles = new String[]{"Impostor", "Crew Mate"};
        initElements();
    }

    private void initElements() {
        binding.mainLayout.setOnClickListener(v -> {
            clearFocuses();
            hideKeyboard();
        });

        binding.setCountBtn.setOnClickListener(v -> {
            setPlayerCount();
            hideKeyboard();
        });
        binding.chooseButton.setOnClickListener(v -> {
            if (players.size()==playerCount){
                Snackbar.make(v, "All players added!", Snackbar.LENGTH_LONG).show();
                return;
            }
            String name = binding.name.getText().toString();
            Random r = new Random();
            if (!impostorChosen) {
                rand = r.nextInt(10000);
                choice = rand % 2 == 0 ? 0 : 1;
            }
            if (choice == 0) {
                impostorChosen = true;
                binding.roleStatus.setText(roles[choice]);
                binding.roleStatus.setTextColor(Color.RED);
                players.add(new Player(name, roles[choice].equals(roles[0])));
                choice = 1;
            } else if ((players.size() == count-1) && !impostorChosen) {
                impostorChosen = true;
                binding.roleStatus.setText(roles[0]);
                binding.roleStatus.setTextColor(Color.RED);
                players.add(new Player(name, roles[choice].equals(roles[0])));
            } else {
                binding.roleStatus.setText(roles[choice]);
                binding.roleStatus.setTextColor(Color.BLUE);
                players.add(new Player(name, roles[choice].equals(roles[0])));
            }
            count++;
            MediaPlayer mp;
            mp = MediaPlayer.create(this, R.raw.among_us_player_added);
            mp.start();
            binding.name.clearFocus();
            hideKeyboard();
            if (players.size()==playerCount){
                Snackbar.make(v, "All players added!", Snackbar.LENGTH_LONG).show();
                binding.name.setEnabled(false);
                binding.chooseButton.setEnabled(false);
            }

        });
        binding.resetButton.setOnClickListener(v -> {
            hideKeyboard();
            binding.name.setText("");
            binding.name.clearFocus();
            binding.roleStatus.setText("");
        });
        binding.viewAll.setOnClickListener(v -> {
            impostorChosen = false;
            hideKeyboard();
            if (!(players.size() > 0)) {
                Snackbar.make(v, "No player added yet!", Snackbar.LENGTH_LONG).show();
            }
            MediaPlayer mp;
            mp = MediaPlayer.create(this, R.raw.amoung_us_sound);
            mp.start();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(messageForAll());
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar.make(v, "Game Reset Successful", Snackbar.LENGTH_LONG).show();
                    players.clear();
                }
            });
            builder.setNegativeButton("Haha! You have cheated!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar.make(v, "You've cheated, Game has been reset!", Snackbar.LENGTH_LONG).show();
                    players.clear();
                }
            });
            builder.show();
            initialSetup();
        });
    }

    private void clearFocuses() {
        binding.name.clearFocus();
        binding.numberOfPlayers.clearFocus();
    }

    private void initialSetup() {

        binding.name.setText("");
        binding.infoInput.setVisibility(View.GONE);
        binding.numberOfPlayers.clearFocus();
        binding.numberOfPlayers.setText("");
        binding.roleStatus.setTextColor(Color.GRAY);
        binding.roleStatus.setText("No Role Allotted");
        binding.numInLayer.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
    }

    private void setPlayerCount() {
        String countTxt = binding.numberOfPlayers.getText().toString();
        if (countTxt.trim().length() > 0) {
            playerCount = Integer.parseInt(countTxt);
            binding.name.setEnabled(true);
            binding.chooseButton.setEnabled(true);
            binding.infoInput.setVisibility(View.VISIBLE);
            binding.numberOfPlayers.clearFocus();
//            binding.numberOfPlayers.setVisibility(View.GONE);
            binding.numInLayer.setVisibility(View.GONE);
        } else {
            Snackbar.make(binding.numberOfPlayers.getRootView(), "Please enter a number", Snackbar.LENGTH_LONG).show();
        }
    }

    private String messageForAll() {
        String eachRole = "";
        for (Player p :
                players) {
            eachRole += p.getNameRole() + "\n";
        }
        return eachRole;
    }

}