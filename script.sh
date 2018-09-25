#!/bin/bash
cd src;
javac ga_solver/*.java;
java ga_solver.Solver > "../performance/moyenne.txt";

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
        set title "Fitness optimal en fonctions du nombre d'iterations"
        set term pdf
        set output "../performance/performance_GAs.pdf"
        
	plot '../performance/moyenne.txt' u 1:2 w l
EOF


