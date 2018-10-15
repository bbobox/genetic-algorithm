#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:150]
        set title "Analyse des probabilités de croisement"
        set term png
        set output "pm.png"
        
	plot 'pc_case02.dat' u 1:2 w l, 'pc_case05.dat' u 1:2 w l, 'pc_case08.dat' u 1:2 w l,'pc_case1.dat' u 1:2 w l
EOF
