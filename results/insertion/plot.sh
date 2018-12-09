#!/bin/bash

cd .;
gnuplot <<- EOF
        set xlabel "iterations"
	set yrange [0:500]
        set ylabel "fitness"
        set title "Analyse des opérateurs d'insertion"
        set term png
        set output "insertions.png"
	plot 'age.dat' u 1:2 w l, 'fitness.dat' u 1:2 w l
EOF
