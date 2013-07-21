package org.i5y.pipe

import java.io.IOException
import java.io.InputStream

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.Failure

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class PipeTest extends FunSuite with ShouldMatchers {

  def inputStreamLength(inputStream: InputStream) =
    Iterator.continually(inputStream.read()).takeWhile(_ != -1).size

  def delayedInputStreamLength(inputStream: InputStream) = {
    val result = inputStreamLength(inputStream)
    Thread.sleep(3000)
    result
  }

  def throwOnFifth(inputStream: InputStream) = {
    Iterator.continually(inputStream.read()).take(4)
    throw new IOException
  }

  test("Success") {
    val w = new Pipe(inputStreamLength)
    (1 to 10).foreach { x => w.write("Hello World!!".getBytes()) }
    Await.result(w.complete, 5.seconds) should be(130)
  }

  test("Success Delayed") {
    val w = new Pipe(delayedInputStreamLength)
    (1 to 10).foreach { x => w.write("Hello World!!".getBytes()) }
    Await.result(w.complete, 5.seconds) should be(130)
  }

  test("IOException") {
    val w = new Pipe(throwOnFifth)
    (1 to 10).foreach { x => w.write("Hello World!!".getBytes()) }
    val completionFuture = w.complete()
    Await.ready(completionFuture, 5.seconds)
    completionFuture.value.get match {
      case Failure(x: IOException) =>
      case _ => fail("Was expecting an IOException")
    }
  }
}