package org.i5y.pipe

import java.io.InputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.Promise
import scala.concurrent.future

class Pipe[R](consumer: InputStream => R)(implicit ec: ExecutionContext) {

  private val inputStream = new PipedInputStream()
  private val outputStream = new PipedOutputStream(inputStream)

  private val promise = Promise().tryCompleteWith(future { consumer(inputStream) })

  def write(bytes: Array[Byte]): Unit = {
    if (!promise.isCompleted) {
      try {
        outputStream.write(bytes)
      } catch {
        case e: Exception =>
          promise.tryFailure(e)
      }
    }
  }

  def complete(): Future[R] = {
    if (!promise.isCompleted) {
      try {
        outputStream.close()
      } catch {
        case e: Exception =>
          promise.tryFailure(e)
      }
    }
    promise.future
  }
}
