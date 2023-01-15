import com.github.fommil.netlib.{LAPACK => FLAPACK}
import dev.ludovic.netlib.lapack.{LAPACK => LLAPACK}
import no.uib.cipr.matrix.{DenseMatrix, SVD => FSVD}
import org.apache.commons.math3.linear.{Array2DRowRealMatrix, SingularValueDecomposition}
import org.apache.commons.math3.random.MersenneTwister
import org.netlib.util.intW
import org.ojalgo.OjAlgoUtils
import org.ojalgo.matrix.Primitive64Matrix
import org.ojalgo.matrix.decomposition.SingularValue
import org.ojalgo.random.Uniform

object Program extends App {

  private def time[T](msg: String)(fn: => T): Unit = {
    val startTime = System.currentTimeMillis()
    println(s"Starting: $msg")
    val f = fn
    val endTime = System.currentTimeMillis()
    val totalMillis = endTime - startTime
    println(s"Finished: $msg, $totalMillis ms")
  }

  val m = 2000
  val n = 2000
  val p = math.min(m, n)
  val q = math.max(m, n)
  val ml = m.toLong
  val nl = n.toLong

  println("Luhenry netlib-java")
  locally {
    val lapack = LLAPACK.getInstance()
    println(s"Using LAPACK instance: ${lapack.getClass.getSimpleName}")

    val rng = new MersenneTwister(1234)
    // Col major
    val A = Array.fill(m * n) {
      rng.nextDouble()
    }
    val S = new Array[Double](p)
    val U = new Array[Double](m * m)
    val Vt = new Array[Double](n * n)

    val iwork = new Array[Int](8 * p)
    val worksize = new Array[Double](1)

    val info = new intW(0)
    lapack.dgesdd("A", m, n, new Array[Double](0), m, new Array[Double](0), new Array[Double](0),
      m, new Array[Double](0), n, worksize, -1, iwork, info)
    val lwork = if (info.`val` != 0)
      7 * q * q + 4 * q
    else
      worksize(0).toInt

    val work = new Array[Double](lwork)

    val svd = time(s"Luhenry SVD with $m, $n") {
      lapack.dgesdd("A", m, n, A, m, S, U, m, Vt, n, work, work.length, iwork, info)
    }
    if (info.`val` > 0)
      println("Did not converge after max iterations")
    else if (info.`val` < 0)
      println("Illegal arguments")
  }

  println("\nFommil netlib-java")
  locally {
    val lapack = FLAPACK.getInstance()
    println(s"Using LAPACK instance: ${lapack.getClass.getSimpleName}")

    val A = new DenseMatrix(m, n)
    val rng = new MersenneTwister(1234)
    for (i <- 0 until m)
      for (j <- 0 until n)
        A.set(i, j, rng.nextDouble())

    val svd = time(s"Fommil SVD with $m, $n") {
      FSVD.factorize(A)
    }
  }

  println("\nojalgo")
  locally {
    println(OjAlgoUtils.ENVIRONMENT)
    val U = Uniform.standard()
    val A = Primitive64Matrix.FACTORY.makeFilled(ml, nl, U)
    val svd = time(s"ojalgo SVD with $ml, $nl") {
      val decomp = SingularValue.PRIMITIVE.make()
      decomp.decompose(A)
    }
  }

  println("\nApache Common Math")
  locally {
    val A = new Array2DRowRealMatrix(m, n)
    val rng = new MersenneTwister(1234)
    for (i <- 0 until m)
      for (j <- 0 until n)
        A.setEntry(i, j, rng.nextDouble())

    val svd = time(s"Apache SVD with $m, $n") {
      new SingularValueDecomposition(A)
    }
  }
}
