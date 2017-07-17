#!/bin/bash
chown -R ubuntu:ubuntu /home/ubuntu/worker/mailsender/
cd /home/ubuntu/worker/mailsender/

echo "build new image aucobo/mailsender"
sudo -u ubuntu docker build -t aucobo/mailsender . --no-cache

container=$(sudo -u ubuntu docker container ls -a --filter name=aucobo-mailsender$ -q)
if [ "$container" != "" ];then
  echo "stop container aucobo-mailsender"
  sudo -u ubuntu docker stop aucobo-mailsender
  sudo -u ubuntu docker rm -f aucobo-mailsender
fi
unset container

echo "create container aucobo-mailsender"
sudo -u ubuntu docker create --name aucobo-mailsender --restart=always aucobo/mailsender
sudo -u ubuntu docker start aucobo-mailsender