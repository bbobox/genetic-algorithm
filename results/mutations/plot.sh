#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:150]
        set title "Etude des mutations"
        set term png
        set output "mutations.png"
        
	plot '1_flip.dat' u 1:2 w l, '3_flip.dat' u 1:2 w l, '5_flip.dat' u 1:2 w l,'bit_flip.dat' u 1:2 w l, 'adaptive_wheel.dat' u 1:2 w l
EOF
