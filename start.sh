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
CMD=$(echo java -jar /home/weblogic/litemall.jar &)
#用ps获取$PRO_NAME进程数量
NUM=$(ps aux | grep -w ${PRO_NAME} | grep -v grep | wc -l)
#少于1，重启进程
if [[ ${NUM} -lt 1 ]];then
 echo "###### ${time1} ###### ${PRO_NAME} 进程不存在，自启程序启动！" >> /home/weblogic/start-log.txt
 ${CMD}
#大于1，杀掉所有进程，重启
elif [[ ${NUM} -gt 1 ]];then
echo "###### ${time1} ###### ${PRO_NAME} 进程数异常，系统将杀死所有 ${PRO_NAME} 进程，然后重启！" >> /home/weblogic/start-log.txt
ps aux | grep -w ${PRO_NAME} | grep -v grep | cut -c 9-15 | xargs kill -9
${CMD}
fi
#kill僵尸进程
NUM_STAT=$(ps aux | grep -w ${PRO_NAME} | grep T | grep -v grep | wc -l)
if [[ ${NUM_STAT} -gt 0 ]];then
 echo "###### ${time1} ###### ${PRO_NAME} 进程僵死，系统将杀死 ${PRO_NAME} 进程，然后重启！" >> /home/weblogic/start-log.txt
 ps aux | grep -w ${PRO_NAME} | grep -v grep | cut -c 9-15 | xargs kill -9
 ${CMD}
fi
# 进程正常
if [[ ${NUM_STAT} -eq 0 ]];then
 echo "###### ${time1} ###### ${PRO_NAME} 进程正常运行中！" >> /home/weblogic/start-log.txt
fi
 
