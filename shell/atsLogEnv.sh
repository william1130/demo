#!/bin/ksh
# ========================================================================================
# 2020/11/19  atsLog Batch environment
# ========================================================================================

LANG=C; export LANG
NLS_LANG=American_America.ZHT16BIG5;export NLS_LANG
LC_CTYPE=zh_TW.BIG5; export LC_CTYPE

ATSLOG_BATCH_HOME=/u01/users/atslogap/atslogBatch; export ATSLOG_BATCH_HOME
LOG_HOME=${ATSLOG_BATCH_HOME}/log/exception; export LOG_HOME
JAVA_HOME=/u01/users/atslogap/jdk1.8; export JAVA_HOME

PATH=${JAVA_HOME}/bin:${ATSLOG_BATCH_HOME}/exe:${ATSLOG_BATCH_HOME}:$PATH; export PATH

umask 027
