#!/bin/bash
cd src;


while [[ "$#" > 0 ]]; do case $1 in
  -s|--selection) selection="$2"; shift;;
  -c|--crossover) crossover="$2"; shift;;
  -mut|--mutation) mutation="$2"; shift;;
  -i|--insertion) insertion="$2"; shift;;
  -pop|--population) population="$2"; shift;;
  -t|--tests) tests="$2"; shift;;
  -N|--size) size="$2"; shift;;
  -max|--size) iter_max="$2"; shift;;
  -pc|--size) pc="$2"; shift;;
  -pm|--size) pm="$2"; shift;;

  *) echo "Unknown parameter passed: $1"; exit 1;;
esac; shift; done

rm */*.class;
javac */*.java;
java tsp.TspPopulation $selection $crossover $mutation $insertion $pc $pm $size $iter_max $tests $population;
# > "./tsp_problem/results/testtest.dat";
