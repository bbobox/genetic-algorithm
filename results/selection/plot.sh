#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:200]
        set title "Analyse du methodes de selection"
        set term png
        set output "pm.png"
        
	plot 'best_select.dat' u 1:2 w l, 'random_select.dat' u 1:2 w l, 'tournament_select.dat' u 1:2 w l
EOF
