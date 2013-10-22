#Distributed according to the GNU Lesser General Public Licence. 
#This library is free software; you can redistribute it and/or modify it
#under the terms of the GNU Lesser General Public License as published by   
#the Free Software Foundation; either version 2.1 of the License, or any
#later version.

# author xiaowei zhou
# 2010-7-19
#!/bin/sh

varservermonregport=10724

strIpAddrs=$(ifconfig |grep "inet addr"| cut -f 2 -d ":"|cut -f 1 -d " ")
arrayIpAddrs=($strIpAddrs)
arrayLen=${#arrayIpAddrs[*]}
i=0

echo
echo "Below are the IP addresses of this machine:"
while [ $i -lt $arrayLen ]; do
	echo -n "$i":" "
	echo ${arrayIpAddrs[i]}

	i=$(($i+1))
done
echo "Please choose the one the client will connect to!"
echo -n "Enter the number you choose (e.g. 0,1,etc.):"
read monIpIndex

echo

if [ $monIpIndex -ge 0 -a $monIpIndex -lt $arrayLen ] 2>/dev/null; then

	echo "You choose the IP address:"${arrayIpAddrs[monIpIndex]}
	echo
	export LD_LIBRARY_PATH=.:$LD_LIBRARY_PATH
	java -Dservermonregport=$varservermonregport -Djava.rmi.server.hostname=${arrayIpAddrs[monIpIndex]} -jar ServerMon.jar

else

	echo "The number you entered is invalid! Exiting..."
	exit 1

fi

