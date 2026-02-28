#!/bin/ksh
a=$(ps -ef | grep $1 | grep -v "grep" | grep -v "checkRunning" | wc -l)
if [ $a -gt 0 ]; then
  echo "** $1 is running, can not execute at same time."
  exit 1
else
  exit 0
fi
echo ">> check end"
