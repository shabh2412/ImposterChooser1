package com.example.imposterchooser;

public class Player {
    private String Name, Role;
    Player(String Name, boolean role){
        this.Name = Name;
        this.Role = role ? "Impostor" : "Crew Mate";
    }

    public String getNameRole() {
        return Role + "\t:\t" + Name;
    }

}
