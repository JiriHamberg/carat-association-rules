#!/bin/bash
# Global settings
cores=45
memory=1500g
# Enable dynamic giving up and re-adding executors based on job computational complexity:
dyn="--conf spark.shuffle.service.enabled=true --conf spark.dynamicAllocation.enabled=true"
# Increase performance at the cost of memory
dyn+=" --conf spark.io.compression.snappy.blockSize=256k --conf spark.shuffle.file.buffer=256k --conf spark.default.parallelism=4000"

# Place logs in separate dir not to create a mess.
logdir="logs"
if [ ! -d "$logdir" ]; then mkdir "$logdir"; fi

spark="/home/carat/spark-2.0.1-bin-hadoop2.7"
#spark="/home/carat/spark-2.0.0-bin-hadoop2.7"
#spark="/home/carat/spark-1.6.2-bin-hadoop2.6"
#spark="/home/carat/spark-1.4.1-bin-hadoop2.6"

## Private Spark
#spark="$HOME/spark-2.0.1-bin-hadoop2.7"

# Hadoop native classes
export SPARK_JAVA_OPTS="-Djava.library.path=/home/carat/hadoop-2.7.3/lib/native"
# Speed up Spark communication
export SPARK_LOCAL_IP="127.0.0.1"

localdisk(){
  # Avoid out of disk space error
  export SPARK_LOCAL_DIRS="/home/carat/tmp/spark/$USER"
}

localmem(){
  export SPARK_LOCAL_DIRS="/run/shm/tmp/spark/$USER"
}

#Change to this for less memory pressure:
#localdisk
localmem

class="$1"
# Log file name
log="${logdir}/${class}"
shift
master="local[$cores]"
export SPARK_MASTER="$master"
# master=$( cat /root/spark-ec2/cluster-url )
#jar=$PWD/$( ls target/carat-tools-*-jar-with-dependencies.jar | tail -n 1 )

jar=/home/jirihamb/.m2/repository/fi/helsinki/cs/nodes/carat-tools/1.3.1/carat-tools-1.3.1-jar-with-dependencies.jar

if [ -n "${CONF}" ]; then echo "CONF set to $CONF"; fi

$spark/bin/spark-submit --driver-memory $memory --executor-memory $memory --conf spark.rpc.message.maxSize=128 \
$CONF ${dyn} --conf spark.task.maxFailures=4 --class "$class" \
--master "$master" --jars "$jar" $@ 2>"${log}.err"
#--master "$master" $@ 1>"${log}.log" 2>"${log}.err"
#--driver-class-path "$jar" "$jar" $@ 1>"${log}.log" 2>"${log}.err"

