#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "probabilités"
        set term pdf
        set output "mutations_operators.pdf"
	set xrange [ * : * ] noreverse writeback
	set x2range [ * : * ] noreverse writeback
	set yrange [ * : * ] noreverse writeback
	set y2range [ * : * ] noreverse writeback
	set zrange [ * : * ] noreverse writeback
	set cbrange [ * : * ] noreverse writeback
	set rrange [ * : * ] noreverse writeback
        
	plot 'operator_0.dat' u 1:2 w l, 'operator_1.dat' u 1:2 w l, 'operator_2.dat' u 1:2 w l,'operator_3.dat' u 1:2 w l
EOF
