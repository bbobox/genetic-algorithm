#!/bin/bash

gnuplot <<- EOF
	set key bot right 
	set term pdf
	set output "pc.pdf"
        set xlabel "iterations"
        set ylabel "fitness"

	set xrange [ * : * ] noreverse writeback
	set x2range [ * : * ] noreverse writeback
	set yrange [ * : * ] noreverse writeback
	set y2range [ * : * ] noreverse writeback
	set zrange [ * : * ] noreverse writeback
	set cbrange [ * : * ] noreverse writeback
	set rrange [ * : * ] noreverse writeback
        
	plot 'pc=0,1.dat' u 1:2 w l, 'pc=0,3.dat' u 1:2 w l, 'pc=0,7.dat'  u 1:2 w l, 'pc=1.dat' u 1:2 w l
EOF


#!/bin/bash


