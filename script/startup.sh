cd /lvdata/perftest/

rm -rf zmq-base

git clone git@gitlab.ztesoft.com:zmq/zmq-base.git

chmod -R +x zmq-base

cd zmq-base

./install.sh

scp target/zmq-2.0.0-bin.tar.gz 172.16.17.20:/home/libin/perftest/

scp /lvdata/perftest/shell/remote.sh 172.16.17.20:/home/libin/perftest/

ssh 172.16.17.20 "chmod +x /home/libin/perftest/remote.sh;nohup /home/libin/perftest/remote.sh >/dev/null 2>&1 &"
