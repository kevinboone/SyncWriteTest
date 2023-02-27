/* A simple Java program to test asynchronous and synchronous disk write
 * performance.  
 *
 * Usage (JDK 11 and later): java SyncWriteTest.java /path/to/some/file
 * Note that the file will be overwritten without warning.
 *
 * Kevin Boone, January 2020 */

import java.nio.*;
import java.nio.channels.*;
import java.io.*;

public class SyncWriteTest
  {
  // Define the size of the block to write
  private static int WRITE_SIZE = 4096;
  // Define the total number of blocks to write
  private static int NUM_WRITES = 1000;

  public static void doWrite (String filename, int writes, boolean sync)
      throws Exception
    {
    RandomAccessFile raf = new RandomAccessFile (filename, "rw");
    FileChannel fc = raf.getChannel();

    for (int i = 0; i < writes; i++)
      {
      ByteBuffer buf = ByteBuffer.allocate (WRITE_SIZE);
      buf.clear();

      while (buf.hasRemaining()) 
	{
	fc.write (buf);
	}
      if (sync)
	fc.force (true);
      }
    raf.close();
    }

  public static void main (String[] args)
      throws Exception
    {
    if (args.length != 1)
      {
      System.err.println ("Usage: java SyncWriteTest {filename}\n");
      System.exit (0);
      }

    String filename = args[0];

    System.out.println ("Writing " + NUM_WRITES + " blocks of size "
       + WRITE_SIZE);

    long t1 = System.currentTimeMillis();
    doWrite (filename, NUM_WRITES, false);
    long t2 = System.currentTimeMillis();
    doWrite (filename, NUM_WRITES, true);
    long t3 = System.currentTimeMillis();
    System.out.println ("async: " + (t2 - t1) + " msec, " 
       + (1000 * NUM_WRITES /(t2 - t1)) + " writes/sec");
    System.out.println ("sync: " + (t3 - t2) + " msec, " 
       + (1000 * NUM_WRITES / (t3 - t2)) + " writes/sec");
    }
  }

