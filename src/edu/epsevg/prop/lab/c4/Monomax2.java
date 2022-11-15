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
public class Monomax2 implements Jugador, IAuto {

    private String nom;
    private int color;
    private int profundidad;

    // Variable que almacena la mejor puntuacion posible para el movimiento. (mas
    // infinito)
    private static final int GANADOR = 999999;

    // Variable que almacena la peor puntuacion posible para el movimiento. (menos
    // infinito)
    private static final int PERDEDOR = -999999;

    // Cantidad de veces que se calcula la heurisitca, se reincia cada ronda
    public int contador;

    // Numero de rondas de la partida
    public int rondas;

    // Constructor
    public Monomax2(int profundidad) {
        this.nom = "MONOMAX";
        this.profundidad = profundidad;
        this.contador = 0;
        this.rondas = 0;
    }

    public int moviment(Tauler t, int color) {

        // Establecemos los casos contados a 0
        contador = 0;
        // Obtenemos el instante de tiempo que nos encontramos en nanosegundos
        long Inici_Cronometre = System.nanoTime();

        // Sumamos 1 a la cantidad de rondas jugadas (las rondas comienzan en 1 no en 0)
        rondas += 1;

        // Establecemos el valor inicial como minimo para que cualquiera sea superior.
        int max = Integer.MIN_VALUE;

        // Establecemos el mejor movimiento como la columna 0 (default)
        int millormov = 0;

        System.out.println("\nRONDA " + rondas + "\n");

        // Para cada columna del tablero
        for (int col = 0; col < t.getMida(); col++) {

            // Hacemos copia del tablero
            Tauler tauler_aux = new Tauler(t);

            // Si se puede realizar movimiento a la columna en la que estamos posicionados
            if (tauler_aux.movpossible(col)) {

                // Realizamos el movimiento
                tauler_aux.afegeix(col, color);

                // Establecemos actual como el valor heurisitico que tendra la linea del juego
                // del tablero auxiliar
                int actual = minimax(tauler_aux, color, (profundidad - 1), Integer.MIN_VALUE, Integer.MAX_VALUE, false);

                // Si el valor actual es superior al valor maximo, establecemos max como a
                // actual y millormov como a valor de columna sobre la cual estamos
                if (actual > max) {
                    max = actual;
                    millormov = col;
                }
            }
        }

        // Obtenemos el tiempo que estamos en nanosegundos
        long Final_Cronometre = System.nanoTime();

        // Calculamos el tiempo exacto restanto final meno inicial para ver el tiempo de
        // ejecucion
        long Nanosegons = Final_Cronometre - Inici_Cronometre;

        System.out.println("S'han explorat " + contador + " casos en " + (double) Nanosegons / 1_000_000
                + " ms, millor heurística trobada: " + max + ".\n");

        // Devolvemos la mejor columna a tirar
        return millormov;
    }

    public int heuristica(Tauler t, int color) {
        int puntuacio_final = 0;
        int verticales = 0;
        int horitzontals = 0;
        int diagonal1 = 0; // Diagonal superior
        int diagonal2 = 0; // Diagonal inferior

        // Para todas las columnas comprueba la puntuacion vertical
        // Si el jugador pierde, devuelve PERDEDOR, si gana devuelve GANADOR
        // Si ni gana ni pierde devuelve la acumulacion de la puntuacion vertical de
        // todas las columnas
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

        // Para todas las columnas comprueba la puntuacion horizontal
        // Si el jugador pierde, devuelve PERDEDOR, si gana devuelve GANADOR
        // Si ni gana ni pierde devuelve la acumulacion de la puntuacion horizontal de
        // todas las columnas
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

        for (int fila = 0; fila < t.getMida() - 3; fila++) {
            for (int columna = 0; columna < t.getMida() - 3; columna++) {
                int puntuacio_posicio = bendiciones(t, fila, columna, 1, 1, color);
                if (puntuacio_posicio == GANADOR)
                    return GANADOR;
                if (puntuacio_posicio == PERDEDOR)
                    return PERDEDOR;
                diagonal1 += puntuacio_posicio;
            }
        }

        for (int fila = 3; fila < t.getMida(); fila++) {
            for (int columna = 0; columna <= t.getMida() - 4; columna++) {
                int puntuacio_posicio = bendiciones(t, fila, columna, -1, +1, color);
                if (puntuacio_posicio == GANADOR)
                    return GANADOR;
                if (puntuacio_posicio == PERDEDOR)
                    return PERDEDOR;
                diagonal2 += puntuacio_posicio;
            }

        }

        // CAMBIAR KEKW
        if(this.profundidad == 8){
            puntuacio_final = horitzontals + verticales;
        }
        else{
            puntuacio_final = horitzontals + verticales + diagonal1 + diagonal2;
        }
        return puntuacio_final;
    }

    // True max, false min
    public int minimax(Tauler tauler_copia, int color, int profunditat, int alpha, int beta, boolean esBendicion) {
        int color_oponent = color;
        int valor;
        // Si la profundidad ya ha llegado a su limite no habra mas movimientos
        // posibles, devolvemos la heurisica del tablero.
        if (profunditat == 0 || tauler_copia.espotmoure() == false) {
            return heuristica(tauler_copia, color);
        }
        // Inicializamos variables en funcion si es min o max
        if (esBendicion) { // Max
            // Establecemos el valor inicial como al minimo asi cualquier valor sera
            // superior.
            valor = Integer.MIN_VALUE;
        } else { // Min
            // Establecemos el valor inicial como al maximo asi cualquier valor sera
            // inferior.
            valor = Integer.MAX_VALUE;
            // Obtenemos el color del oponente que es el contrario al nuestro.
            if (color == 1) {
                color_oponent = -1;
            } else {
                color_oponent = 1;
            }
        }
        // Para cada columna del tablero
        for (int col = 0; col < tauler_copia.getMida(); col++) {
            // Copia del tablero
            Tauler tauler_aux = new Tauler(tauler_copia);
            // Si se puede realizar un movimiento en la columna que estamos posicionados
            if (tauler_aux.movpossible(col)) {
                // Sumamos 1 a l numero de casos explorados
                contador++;

                if (esBendicion) {
                    // Realizamos el movimiento seleccionado con el color del jugador.
                    tauler_aux.afegeix(col, color);

                    if (tauler_aux.solucio(col, color)) {
                        return GANADOR;
                    }
                    // Calculamos heuristica. Si esta es supererior al valor actual, substituimos
                    // valor por esta.
                    valor = Math.max(valor, minimax(tauler_aux, color, profunditat - 1, alpha, beta, false));

                    // Poda alfa-beta
                    if (beta <= valor) {
                        return valor;
                    }

                    alpha = Math.max(valor, alpha);
                } else { // min
                    // Realizamos el movimiento con el color del oponente ya que estamos en la capa
                    // Min
                    tauler_aux.afegeix(col, color_oponent);

                    if (tauler_aux.solucio(col, color_oponent)) {
                        return PERDEDOR;
                    }
                    // Calculamos heuristica. Si esta es menor al valor actual substituimos por esta
                    valor = Math.min(valor, minimax(tauler_aux, color, profunditat - 1, alpha, beta, true));

                    // Realitzem la poda alpha-beta
                    if (valor <= alpha) {
                        return valor;
                    }

                    beta = Math.min(valor, beta);
                }
            }
        }
        // Devolvemos heuristica
        return valor;

    }

    // Cuenta cuantas hay conectadas del jugador y del oponente
    public int bendiciones(Tauler tauler, int fila, int columna, int increment_fila, int increment_columna, int color) {
        // definimos los colores, tanto el nuestro como el del otro jugador
        int color_oponent;
        if (color == 1) {
            color_oponent = -1;
        } else {
            color_oponent = 1;
        }
        // Contadores para las cantidad de fichas que esten conectadas
        // tanto para nosotros como para el oponente
        int bendiciones_oponente = 0;
        int bendiciones = 0;

        for (int i = 0; i < 4; i++) {
            if (tauler.getColor(fila, columna) == color_oponent) {
                bendiciones_oponente++;
            }

            else if (tauler.getColor(fila, columna) == color) {
                bendiciones++;
            }
            fila += increment_fila;
            columna += increment_columna;
        }

        if (bendiciones_oponente == 4) {
            return PERDEDOR;
        }

        else if (bendiciones == 4) {
            return GANADOR;
        }

        else {
            return bendiciones;
        }
    }

    public String nom() {
        return nom;
    }
}
