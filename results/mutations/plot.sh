#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
	set yrange [0:350]
        set title "Etude des mutations"
        set term pdf
        set output "mutations.pdf"
        
	plot '1_flip.dat' u 1:2 w l, '3_flips.dat' u 1:2 w l, '5_flips.dat' u 1:2 w l,'bit_flip.dat' u 1:2 w l, 'adaptive_wheel.dat'  u 1:2 w l
EOF
 #'5_flips_island.dat' u 1:2 w l
