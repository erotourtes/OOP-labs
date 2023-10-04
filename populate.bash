#!/bin/bash

cd ./code-getter
if [ ! -f main ]; then
    go build main.go
fi
./main
