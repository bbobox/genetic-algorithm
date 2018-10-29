#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:250]
        set title "Analyse des probabilités de croisement"
        set term png
        set output "pc.png"
        
	plot 'pc=0,1.dat' u 1:2 w l, 'pc=0,3.dat' u 1:2 w l, 'pc=0,5.dat'  u 1:2 w l,'pc=0,8.dat' u 1:2 w l, 'pc=1.dat' u 1:2 w l
EOF
