#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:250]
        set title "Etude des croisements"
        set term png
        set output "crossovers.png"
        
	plot 'monopoint.dat' u 1:2 w l, 'uniform.dat' u 1:2 w l
EOF
