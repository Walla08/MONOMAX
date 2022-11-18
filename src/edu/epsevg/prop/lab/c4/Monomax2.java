/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.epsevg.prop.lab.c4;

/**
 * Algoritmo MONOMAX
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

    /**
     * Cantidad de veces que se calcula la heuristica, se reinicia cada ronda
     */
    public int contador;
    
    /**
     * Número de rondas de la partida
     */
    public int rondas;

    // Constructor
    /**
     * Metodo de creacion de MONOMAX.
     * @param profundidad Profundidad que el algoritmo minimax evaluará
     */
    public Monomax2(int profundidad) {
        this.nom = "MONOMAX";
        this.profundidad = profundidad;
        this.contador = 0;
        this.rondas = 0;
    }
    
    /**
     * Es la función principal para el movimiento de una ficha. Se encarga de iniciar la función minimax para poder evaluar 
     * el mejor movimiento posible para cada columna del tablero y retorna la mejor columna donde tirar.
     * 
     * @param t Tablero donde se juega.
     * @param color Color del jugador.
     * @return millormov [Número de columna donde es mejor tirar la ficha.]

     */
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
    
    /**
     * Encargada de devolver el valor heurístico total que tiene cierto Tauler, 
     * donde “color” hace referencia al color de ficha de nuestro jugador (Monomax 🐒 ). 
     * Con este tablero y este color, podemos calcular cuál es el valor heurístico de la mesa.
     * @param t Tablero donde se juega.
     * @param color Color del jugador.
     * @return puntuacio_final [Puntuación de verticales, horizontales y diagonales despues de evaluar el movimiento de una ficha en esa posición.]
     */

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
                int carles_puigdemont = bendiciones(t, fila, columna, 1, 0, color);
                if (carles_puigdemont == GANADOR)
                    return GANADOR;
                if (carles_puigdemont == PERDEDOR)
                    return PERDEDOR;
                verticales += carles_puigdemont;
            }
        }

        // Para todas las columnas comprueba la puntuacion horizontal
        // Si el jugador pierde, devuelve PERDEDOR, si gana devuelve GANADOR
        // Si ni gana ni pierde devuelve la acumulacion de la puntuacion horizontal de
        // todas las columnas
        for (int fila = 0; fila < t.getMida(); fila++) {
            for (int columna = 0; columna < t.getMida() - 3; columna++) {
                int carles_puigdemont = bendiciones(t, fila, columna, 0, 1, color);
                if (carles_puigdemont == GANADOR)
                    return GANADOR;
                if (carles_puigdemont == PERDEDOR)
                    return PERDEDOR;
                horitzontals += carles_puigdemont;
            }
        }

        // Por razones de alguna fuerza mayor (Ronald Koeman), al momento de comparar
        // con profundidad 8 y utilizar diagonales
        if (this.profundidad == 8) {
            puntuacio_final = horitzontals + verticales;
        } else {
            // Si no es profundidad 8 que calcule tambien las diagonales
            for (int fila = 0; fila < t.getMida() - 3; fila++) {
                for (int columna = 0; columna < t.getMida() - 3; columna++) {
                    int carles_puigdemont = bendiciones(t, fila, columna, 1, 1, color);
                    if (carles_puigdemont == GANADOR)
                        return GANADOR;
                    if (carles_puigdemont == PERDEDOR)
                        return PERDEDOR;
                    diagonal1 += carles_puigdemont;
                }
            }

            for (int fila = 3; fila < t.getMida(); fila++) {
                for (int columna = 0; columna <= t.getMida() - 4; columna++) {
                    int carles_puigdemont = bendiciones(t, fila, columna, -1, +1, color);
                    if (carles_puigdemont == GANADOR)
                        return GANADOR;
                    if (carles_puigdemont == PERDEDOR)
                        return PERDEDOR;
                    diagonal2 += carles_puigdemont;
                }

            }

            puntuacio_final = horitzontals + verticales + diagonal1 + diagonal2;
        }
        return puntuacio_final;
    }

    /**
     * Esta función es la encargada de escoger el mejor camino de las opciones exploradas. 
     * Básicamente se encarga de verificar que tan efectivo es un movimiento en una columna,
     * tanto como nosotros como para el rival. Si el camino seleccionado es bueno(depende de 
     * la planificación de la heurística) nos llevará a ganar la partida.
     * 
     * @param tauler_copia Copia del Tablero donde se juega.
     * @param color Color del jugador.
     * @param profunditat Profundidad a evaluar el algoritmo(Iteraciones del minimax).
     * @param alpha Número máximo a escoger por MAX.
     * @param beta Número mínimo a escoger por min.
     * @param esBendicion Parametro que indica si estamos en iteración de MAX o de min.
     * @return valor [Valor de evaluación del minimax(luego de haber pasado por heuristica y bendiciones).]
     */
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
    
    /**
     * Función encargada de calcular las fichas conectadas ya sea del jugador como del oponente. 
     * Para esta función se introducen los parámetros de aumento de filas y aumento de columnas 
     * con los cuales se puede controlar si se está viendo las fichas conectadas en vertical, 
     * horizontal o diagonales
     * 
     * @param tauler Tablero donde se juega.
     * @param fila Fila seleccionada.
     * @param columna Columna seleccionada.
     * @param increment_fila Incremento de fila con el cual se buscará en vertical, horizontal o diagonal.
     * @param increment_columna Incremento de columna con el cual se buscará en vertical, horizontal o diagonal.
     * @param color Color del jugador.
     * @return bendiciones [Número de fichas conectadas(GANADOR, PERDEDOR o el número de conectadas del jugador).]
     */

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

        // Busca las fichas conectadas del jugador y del oponente.
        // para esto se necesitan los parametros de increment_fila e increment_columna,
        // de esta forma se puede hacer este procedimiento en vertical, horizontal y en
        // diagonal.
        for (int i = 0; i < 4; i++) {
            // Si la ficha encontrada es del oponente, aumentamos su contador
            if (tauler.getColor(fila, columna) == color_oponent) {
                bendiciones_oponente++;
            }
            // Si la ficha encontrada es nuestra, aumentamos nuestro contador
            else if (tauler.getColor(fila, columna) == color) {
                bendiciones++;
            }
            // Dependiendo de los parametros introducidos aumentamos la fila, columna o ambos
            // Básicamente con esto estamos haciendo un barrido de todo el tablero.
            fila += increment_fila;
            columna += increment_columna;
        }
        
        // Si las fichas del oponente que esten juntas suman 4 ha ganado
        if (bendiciones_oponente == 4) {
            // Eso no es de Dios ve
            return PERDEDOR;
        }
        
        //Si las fichas nuestras conectadas suman 4 hemos ganado
        else if (bendiciones == 4) {
            // Que bendicion ve
            return GANADOR;
        }

        // Si no se cumple ninguno de los casos anteriores retornará 
        // el número de fichas conectadas del jugador
        else {
            return bendiciones;
        }
    }
    
    /**
     * Función que regresa el nombre de nuestro jugador.
     * En este caso regresará MONOMAX
     * @return nom [Es el nombre del jugador]
     */
    public String nom() {
        return nom;
    }
}
