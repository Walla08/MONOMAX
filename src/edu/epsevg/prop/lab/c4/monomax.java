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
public class Monomax implements Jugador, IAuto{
    private String nom;
    private int color;
    private int profundidad;

    public Monomax(int profundidad){
        this.nom = "MONOMAX";
        this.profundidad = profundidad;
        
    }
    
    public int moviment(Tauler t, int color){
        this.color = color;
        int depth = profundidad;

        int col = columna(t, depth);

        return col;
    }

    public int heuristica(Tauler t){
        return valor
    }

    public int minimax(Tauler t, int profundidad, int alfa, int beta, boolean isMax, int color){
        return resultado
    }

    public int columna(Tauler t, int profundidad){
        return columna
    }
    
    public String nom(){
        return nom;
    }
}
