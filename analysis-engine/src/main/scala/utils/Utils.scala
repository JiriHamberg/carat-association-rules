package mllibTest.utils

object TimeUtils {

  def timeIt[T](block: => T): (T, Long) = {
		val t0 = System.currentTimeMillis()
		val result = block
		val t1 = System.currentTimeMillis()
		(result, t1 - t0)
	}

}
