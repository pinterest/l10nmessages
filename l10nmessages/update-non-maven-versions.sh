#!/bin/sh

if [ $# -eq 0 ]; then
    echo "New version required"
    exit 1
fi
NEW_VERSION=$1


echo "should fail"
