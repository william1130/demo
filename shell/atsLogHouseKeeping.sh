#!/bin/ksh
ATSLOG_BATCH_HOME=/u01/users/atslogap/atslogBatch; export ATSLOG_BATCH_HOME
cd ${ATSLOG_BATCH_HOME}
. ./atsLogEnv.sh

NOW=/usr/bin/date
LOG_FILE_NAME=`$NOW +"atsLogHouseKeeping.log.%Y%m%d.%H"`
LOG_FILE=${LOG_HOME}/${LOG_FILE_NAME}

umask 027

cd ${ATSLOG_BATCH_HOME}
java -Xms128m -Xmx512m -cp ./exe/atsLog_BTH.jar -Djava.security.egd=file:/dev/./urandom proj.nccc.atsLog.batch.HouseKeepingProc >> ${LOG_FILE} 2>&1 &
