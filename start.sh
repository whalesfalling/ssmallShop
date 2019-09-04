# crontab -e
#* * * * * /home/weblogic/start.sh
#* * * * * sleep 10; /home/weblogic/start.sh
#* * * * * sleep 20; /home/weblogic/start.sh
#* * * * * sleep 30; /home/weblogic/start.sh
#* * * * * sleep 40; /home/weblogic/start.sh
#* * * * * sleep 50; /home/weblogic/start.sh

#! /bin/sh
#进程名字可修改
PRO_NAME=litemall.jar
time1=$(date)
#用ps获取$PRO_NAME进程数量
NUM=$(ps aux | grep -w ${PRO_NAME} | grep -v grep | wc -l)
#少于1，重启进程
if [[ ${NUM} -lt 1 ]];then
 echo "###### ${time1} ###### ${PRO_NAME} 进程[${NUM}]不存在，自启程序启动！" >> /home/weblogic/start-log.txt
 echo "------------------start---------------" >> /home/weblogic/start-log.txt
 java -jar /home/weblogic/litemall.jar >> /home/weblogic/logs/log.log &
#大于1，杀掉所有进程，重启
elif [[ ${NUM} -gt 1 ]];then
 echo "###### ${time1} ###### ${PRO_NAME} 进程数[${NUM}]异常，系统将杀死所有 ${PRO_NAME} 进程，然后重启！" >> /home/weblogic/start-log.txt
 ps aux | grep -w ${PRO_NAME} | grep -v grep | cut -c 9-15 | xargs kill -9
 echo "------------------start---------------" >> /home/weblogic/start-log.txt
 java -jar /home/weblogic/litemall.jar >> /home/weblogic/logs/log.log &
fi
#kill僵尸进程
NUM_STAT=$(ps aux | grep -w ${PRO_NAME} | grep T | grep -v grep | wc -l)
if [[ ${NUM_STAT} -gt 0 ]];then
 echo "###### ${time1} ###### ${PRO_NAME} 进程[${NUM}]僵死，系统将杀死 ${PRO_NAME} 进程，然后重启！" >> /home/weblogic/start-log.txt
 ps aux | grep -w ${PRO_NAME} | grep -v grep | cut -c 9-15 | xargs kill -9
 echo "------------------start---------------" >> /home/weblogic/start-log.txt
 java -jar /home/weblogic/litemall.jar >> /home/weblogic/logs/log.log &
fi
# 进程正常
if [[ ${NUM} -eq 1 ]];then
 echo "###### ${time1} ###### ${PRO_NAME} 进程[${NUM}]正常运行中！" >> /home/weblogic/start-log.txt
fi

