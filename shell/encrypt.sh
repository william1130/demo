#!/bin/ksh
# ========================================================================================
# 修改日期    修改編號       修改人            修改內容
# ----------------------------------------------------------------------------------------
# 2020/11/19  atsLog 檔案加解密 for atslogBatch
# 2024/02/16  M2023089 William 支援AES演算法
# ========================================================================================

SH_HOME=/u01/users/atslogap/atslogBatch;export SH_HOME
LOG_HOME=/u01/users/atslogap/atslogBatch/log;export LOG_HOME

JOB_MODE=encrypt
DATE=date; export DATE
SYS_DATE=`${DATE} '+%Y-%m-%d'`
LOGFILE=${LOG_HOME}/${JOB_MODE}.log.${SYS_DATE}; export LOGFILE

CURR_TIME=`${DATE} +"%Y/%m/%d %H:%M:%S"`
umask 027

cd $SH_HOME
. ./hsmEnv.sh

if [ "$1" = "" ] || [ "$2" = "" ]
then
   echo "[${CURR_TIME}] Usage : ${JOB_MODE}.sh [SOURCE FILE NAME] [DESTINATION FILE NAME]!!" >> $LOGFILE 
   echo "[${CURR_TIME}] [SOURCE FILE NAME     ] ="$1 >> $LOGFILE
   echo "[${CURR_TIME}] [DESTINATION FILE NAME] ="$2 >> $LOGFILE
   exit 1
fi

sfile_name=$1
dfile_name=$2

if [ "$3" = "" ] || [ "$4" = "" ] 
then
   rsakeylabel=NCCCPUB3
   keyType=3
else
   rsakeylabel=$3
   keyType=$4
fi

java -cp ${SH_HOME}/exe/NCCCencrypt.jar:${SH_HOME}/exe/PSDataProtectSDK.jar \
com.nccc.NCCCencrypt \
--mode $JOB_MODE'File' \
--sourcePath $sfile_name \
--destinationPath $dfile_name \
--keyName $rsakeylabel \
--ssoid $ssoid \
--keyType $keyType \
--angelaIP $angelaIP >> $LOGFILE

rc=$?

if [ $rc -eq 1 ]
then
	echo "[${CURR_TIME}] ${sfile_name} ${JOB_MODE} success!! " >> $LOGFILE
	exit 0
else
	echo "[${CURR_TIME}] ${sfile_name} ${JOB_MODE} failure!! " >> $LOGFILE
	exit 1
fi
