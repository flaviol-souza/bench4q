@echo off
java -Dservermonregport=10724 -Dsubordinatereceiverport=10726 -Dmulticasttargetport=10728 -Dmulticastgroupip=237.83.173.8 -jar ClusterMon.jar