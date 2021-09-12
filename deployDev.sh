#!/bin/bash

serverUser="root"
serverHost="192.168.1.13"

./build.sh

scp -r ./target/docker/* $serverUser@$serverHost:~/Shared/images_new/
ssh $serverUser@$serverHost 'cd ~/Shared/images_new && ./load.sh'
ssh $serverUser@$serverHost 'cd ~/Shared/images_new && ./run.sh'