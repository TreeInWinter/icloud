#! /bin/bash
start_s=2014-03-25
end_s=2014-03-28
tomo()
{
  date=$(date +%Y-%m-%d -d "$1 1 days");
  echo $date;
}
end_s=`tomo $end_s`
path=$(date +%Y-%m-%d)

if [ ! -d $path ]; 
 then 
 echo "创建文件名"$path
 mkdir -p $path
fi 

#1. 利用date命名锁需要的文件名
copyBiz()
{
	echo "创建biz日志目录"
	biz_path=$path/biz
	rm -rf biz_path
	mkdir -p $biz_path

	#2. copy biz数据到该文件夹中
	biz_hosts=(66 67 68)
	biz_count=${#biz_hosts[@]}

	echo "biz日志文件夹有"$biz_count个
	for host in ${biz_hosts[*]}
	do
	  start=$start_s
	  end=$end_s
	  while [ "$start" != "$end" ] 
	  do
	    fileName=/log/241.$host/tops/history/tops-front-operator-biz*-debug-$start.log.gz

	    tmpName=$biz_path/$host-tops-front-operator-biz-debug-$start.log.gz
	    destName=$biz_path/$host-tops-front-operator-biz-debug-$start.log
	    okName=$biz_path/$host-tops-front-operator-biz-debug-$start.ok.log
	    echo "cp "$fileName";"
	    cp $fileName $tmpName
	    echo "解压"$tmpName
	    gzip -d $tmpName
	    echo "过滤数据"
	    grep "精确匹配的报价" $destName > $okName
	    echo "rm -rf $destName"
	    rm -rf $destName
	    start=`tomo $start`
	  done
	done
}
#3. copy biz数据到该文件夹中
copyEterm(){
   	echo "创建eterm日志目录"
	eterm_path=$path/eterm
	rm -rf $eterm_path
	mkdir -p $eterm_path
	
	#2. copy eterm数据到该文件夹中
	eterm_hosts=(82 83 84)
	eterm_count=${#eterm_hosts[@]}

	echo "eterm日志文件夹有"$eterm_count个
	for host in ${eterm_hosts[*]}
	do
	  start=$start_s
	  end=$end_s
	  while [ "$start" != "$end" ] 
	  do
	    fileName=/log/241.$host/eterm/$start.log
	    destName=$eterm_path/$host-$start.log
	    okName=$eterm_path/$host-$start.ok.log
	    echo "cp "$fileName";"
	    cp $fileName $destName
	    echo "过滤数据"
	    grep "c.t.etermface.perf.BizPerf" $destName | grep "TIME" > $okName    
	    echo "rm -rf $destName"
	    rm -rf $destName
	    start=`tomo $start`
	  done
	done
	
}
#4.usage
usage(){
echo "syntax error!"
echo "Usage:"
echo "$(basename $0) biz|eterm|all"
}
#5.all
all(){
copyBiz
copyEterm
}
if [ $# != 1 ];
then
  usage; exit 1;
fi
case $1 in
biz)    copyBiz;;
eterm)   copyEterm;;
all)  all;;
*)        usage;;
esac


