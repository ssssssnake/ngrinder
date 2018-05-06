cd /home/libin/perftest

tar zxvf zmq-2.0.0-bin.tar.gz

cd zmq/bin

\cp -f /home/libin/perftest/config/namesrv.properties /home/libin/perftest/zmq/conf/namesrv/

\cp -f /home/libin/perftest/config/broker.properties /home/libin/perftest/zmq/conf/broker/

/home/libin/perftest/zmq/bin/mqshutdown broker

/home/libin/perftest/zmq/bin/mqshutdown namesrv

sleep 5

/home/libin/perftest/zmq/bin/mqnamesrv

/home/libin/perftest/zmq/bin/mqHAbroker
