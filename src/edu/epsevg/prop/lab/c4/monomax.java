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
public class Monomax implements Jugador, IAuto {

    private String nom;
    private int color;
    private int profundidad;

    // Variable que emmagatzema la màxima puntuació. (mas infinito)
    private static final int GANADOR = 999999;

    // Variable que emmagatzema la mínima puntuació. (menos infinito)
    private static final int PERDEDOR = -999999;

    /**
     * Variable que emmagatzema la quantitat de casos (heuristiques) comptats,
     * aquesta es reinicia cada ronda.
     */
    public int contador;

    // Constructor
    public Monomax(int profundidad) {
        this.nom = "MONOMAX";
        this.profundidad = profundidad;

    }

    public int moviment(Tauler t, int color) {
        this.color = color;
        int depth = profundidad;

        return 0;
    }

    public int heuristica(Tauler t, int color) {
        int puntuacio_final;
        int verticales = 0;
        int horitzontals = 0;
        int diagonal1 = 0;
        int diagonal2 = 0;

        for (int fila = 0; fila < t.getMida() - 3; fila++) {
            for (int columna = 0; columna < t.getMida(); columna++) {
                int puntuacio_posicio = bendiciones(t, fila, columna, 1, 0, color);
                if (puntuacio_posicio == GANADOR)
                    return GANADOR;
                if (puntuacio_posicio == PERDEDOR)
                    return PERDEDOR;
                verticales += puntuacio_posicio;
            }
        }

        for (int fila = 0; fila < t.getMida(); fila++) {
            for (int columna = 0; columna < t.getMida() - 3; columna++) {
                int puntuacio_posicio = bendiciones(t, fila, columna, 0, 1, color);
                if (puntuacio_posicio == GANADOR)
                    return GANADOR;
                if (puntuacio_posicio == PERDEDOR)
                    return PERDEDOR;
                horitzontals += puntuacio_posicio;
            }
        }

        puntuacio_final = horitzontals + verticales + diagonal1 + diagonal2;
        return puntuacio_final;
    }

    public int minimax(Tauler tauler_copia, int color, int profunditat, int alpha, int beta, boolean esBendicion) {
        if (profunditat == 0 || tauler_copia.espotmoure() == false) {
            return heuristica(tauler_copia, color);
        }
        int valor = Integer.MIN_VALUE;
        for (int col = 0; col < tauler_copia.getMida(); col++) {
            Tauler tauler_aux = new Tauler(tauler_copia);
            if (tauler_aux.movpossible(col)) {
                contador++;
                tauler_aux.afegeix(col, color);

                if (esBendicion) { // max
                    if (tauler_aux.solucio(col, color)) {
                        return GANADOR;
                    }
                    if (beta <= valor) {
                        return valor;
                    }
                    valor = Math.max(valor, minimax(tauler_aux, color, profunditat - 1, alpha, beta, !esBendicion));
                    alpha = Math.max(valor, alpha);

                } else { // min
                    int color_oponent;
                    if (color == 1) {
                        color_oponent = -1;
                    } else {
                        color_oponent = 1;
                    }
                    if (tauler_aux.solucio(col, color_oponent)) {
                        return PERDEDOR;
                    }
                    // Realitzem la poda alpha-beta
                    if (valor <= alpha) {
                        return valor;
                    }
                    valor = Math.min(valor, minimax(tauler_aux, color, profunditat - 1, alpha, beta, !esBendicion));
                    beta = Math.min(valor, beta);
                }
            }
        }
        return valor;

    }

    // Cuenta cuantas hay conectadas
    public int bendiciones(Tauler tauler, int fila, int columna, int increment_fila, int increment_columna, int color) {
        int color_oponent;
        if (color == 1) {
            color_oponent = -1;
        } else {
            color_oponent = 1;
        }

        int connectades_oponent = 0;
        int connectades = 0;

        for (int i = 0; i < 4; i++) {
            if (tauler.getColor(fila, columna) == color_oponent) {
                connectades_oponent++;
            }

            else if (tauler.getColor(fila, columna) == color) {
                connectades++;
            }
            fila += increment_fila;
            columna += increment_columna;
        }

        if (connectades_oponent == 4) {
            return PERDEDOR;
        }

        else if (connectades == 4) {
            return GANADOR;
        }

        else {
            return connectades;
        }
    }

    public String nom() {
        return nom;
    }
}
