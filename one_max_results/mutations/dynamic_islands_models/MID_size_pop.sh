#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "taille popoulations"
        set title "Evolution de la taille des populations dans le model en iles dynamique"
        set term pdf
        set output "mutations_operators_MID.pdf"
        
	plot 'operator_0.dat' u 1:2 w l, 'operator_1.dat' u 1:2 w l, 'operator_2.dat' u 1:2 w l,'operator_3.dat' u 1:2 w l
EOF
 #'5_flips_island.dat' u 1:2 w l
