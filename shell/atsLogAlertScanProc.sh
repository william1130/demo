#!/bin/ksh
ATSLOG_BATCH_HOME=/u01/users/atslogap/atslogBatch; export ATSLOG_BATCH_HOME
cd ${ATSLOG_BATCH_HOME}
. ./atsLogEnv.sh

NOW=/usr/bin/date
LOG_FILE_NAME=`$NOW +"atsLogAlertScanProc.log.%Y%m%d"`
LOG_FILE=${LOG_HOME}/${LOG_FILE_NAME}

umask 027

cd ${ATSLOG_BATCH_HOME}

a=`ps -ef | grep "atsLogAlertScanProc=true" | grep -v "grep" | wc -l`
if [ $a -gt 0 ]; then
  echo "program already running !" >> ${LOG_FILE} 
else
  echo "program startup." >> ${LOG_FILE} 
  java -DatsLogAlertScanProc=true -Xms128m -Xmx512m -cp ./exe/atsLog_BTH.jar -Djava.security.egd=file:/dev/./urandom proj.nccc.atsLog.batch.AlertScanProc >> ${LOG_FILE} 2>&1 &
fi
