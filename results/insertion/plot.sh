#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
        set title "Analyse des probabilités de croisement"
        set term png
        set output "pm.png"
        
	plot 'age_insertion.dat' u 1:2 w l, 'best_insertion.dat' u 1:2 w l
EOF
