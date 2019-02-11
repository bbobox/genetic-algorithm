#!/bin/bash
cd src;


while [[ "$#" > 0 ]]; do case $1 in
  -pop|--population) population="$2"; shift;;
  -t|--tests) tests="$2"; shift;;
  -N|--size) size="$2"; shift;;
  -max|--size) iter_max="$2"; shift;;

  *) echo "Unknown parameter passed: $1"; exit 1;;
esac; shift; done

rm */*.class;
javac */*.java;
java ga_solver.DynamicIslandModel $size $iter_max $tests $population ;




