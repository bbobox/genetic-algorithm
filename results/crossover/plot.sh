#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:250]
        set title "Etude des croisements"
        set term png
        set output "crossovers.png"
        
	plot 'monopoint_crossover.dat' u 1:2 w l, 'uniform_crossover.dat' u 1:2 w l
EOF
