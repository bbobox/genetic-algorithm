#!/bin/bash

gnuplot <<- EOF
        set xlabel "iterations"
        set ylabel "fitness"
        set term pdf
        set output "crossovers.pdf"
	set key bot right

	set x2range [ * : * ] noreverse writeback
	set yrange [ * : * ] noreverse writeback
	set y2range [ * : * ] noreverse writeback
	set zrange [ * : * ] noreverse writeback
	set cbrange [ * : * ] noreverse writeback
	set rrange [ * : * ] noreverse writeback
        
	plot 'monopoint.dat' u 1:2 w l, 'uniform.dat' u 1:2 w l
EOF


