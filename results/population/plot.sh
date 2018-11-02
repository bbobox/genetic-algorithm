#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:280]
        set title "Etude de la taille des populations"
        set term png
        set output "populations.png"
        
	plot 'pop=20.dat' u 1:2 w l, 'pop=50.dat' u 1:2 w l, 'pop=150.dat' u 1:2 w l,'pop=400.dat' u 1:2 w l
EOF
