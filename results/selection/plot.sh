#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:200]
        set title "Analyse du methodes de selection"
        set term png
        set output "pm.png"
        
	plot 'best.dat' u 1:2 w l, 'random.dat' u 1:2 w l, 'tournament.dat' u 1:2 w l
EOF
