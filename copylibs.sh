#!/bin/bash


function copyfile()
{
remotelib=$1
shift
localdir=$1
shift 
cp $remotelib/*/*.jar $localdir
}

#filepath=$1
#shift

#for filename in `cat filepath`

copyfile $1 $2 
