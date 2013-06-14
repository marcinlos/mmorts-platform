#!/bin/sh

CP=bin:lib/Ice.jar
MAIN=pl.edu.agh.ki.mmorts.testclient.Main
ARGS=--Ice.Config=client/client.config

java -cp ${CP} ${MAIN} ${ARGS}
