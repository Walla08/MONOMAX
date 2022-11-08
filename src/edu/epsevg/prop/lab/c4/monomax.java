/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.epsevg.prop.lab.c4;

/**
 *
 * @author Walter y Miquel
 */
public class monomax implements Jugador, IAuto{
    private String nom;
    
    public monomax(){
        nom = "MONOMAX";
    }
    
    public int moviment(Tauler t, int color){
        int col = (int)(t.getMida() * Math.random());
        while (!t.movpossible(col)) {
        col = (int)(t.getMida() * Math.random());
        }
        return col;
    }
    
    public String nom(){
        return nom;
    }
}
