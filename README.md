# SyncWriteTest

A simple Java program for testing the synchronous write speed of a disk storage
system (and of the JVM, but the storage is usually the limiting factor).

This program is intended for benchmarking storage devices that are mostly used
synchronously. This applies to almost any software that uses a journalling
system for storage: message brokers, database transaction logs, etc. Most
benchmark tools (e.g., `hdparm`) don't test synchronous write throughput
specifically.

## How to use

There's no need to compile if you have Java 11 or later. 

    $ java SyncWriteTest /path/to/some/file

    Writing 1000 blocks of size 4096
    async: 52 msec, 19230 writes/sec
    sync: 4771 msec, 209 writes/sec

The specified file path needs to be on the storage path that is being tested.
Notice the radical difference in write throughput for 

*The specific file will be overwritten without warning*.

You can tune the number of writes and the write block size by editing the
settings in line 17 or thereabouts. 

## How it works

The program just appends blocks of a fixed size to a file. After each write, it
calls `FileChannel.force()`, which in turn does `fsync()` syscall to flush the
disk buffer.


