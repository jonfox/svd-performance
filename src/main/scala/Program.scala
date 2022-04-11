import com.github.fommil.netlib.{LAPACK => FLAPACK}
import dev.ludovic.netlib.{LAPACK => LLAPACK}
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

  val N = 2200
  val NL = N.toLong

  println("Luhenry netlib-java")
  locally {
    val lapack = LLAPACK.getInstance()
    println(s"Using LAPACK instance: ${lapack.getClass.getSimpleName}")

    val rng = new MersenneTwister(1234)
    // Col major
    val M = Array.fill(N * N) {
      rng.nextDouble()
    }
    val S = new Array[Double](N)
    val U = new Array[Double](N * N)
    val Vt = new Array[Double](N * N)

    val iwork = new Array[Int](8 * N)
    val worksize = new Array[Double](1)

    val info = new intW(0)
    lapack.dgesdd("A", N, N, new Array[Double](0), N, new Array[Double](0), new Array[Double](0),
      N, new Array[Double](0), N, worksize, -1, iwork, info)
    val lwork = if (info.`val` != 0)
      7 * N * N + 4 * N
    else
      worksize(0).toInt

    val work = new Array[Double](lwork)

    val svd = time(s"Luhenry SVD with $N") {
      lapack.dgesdd("A", N, N, M, N, S, U, N, Vt, N, work, work.length, iwork, info)
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

    val M = new DenseMatrix(N, N)
    val rng = new MersenneTwister(1234)
    for (i <- 0 until N)
      for (j <- 0 until N)
        M.set(i, j, rng.nextDouble())

    val svd = time(s"Fommil SVD with $N") {
      FSVD.factorize(M)
    }
  }

  println("\nojalgo")
  locally {
    println(OjAlgoUtils.ENVIRONMENT)
    val U = Uniform.standard()
    val M = Primitive64Matrix.FACTORY.makeFilled(NL, NL, U)
    val svd = time(s"ojalgo SVD with $NL") {
      val decomp = SingularValue.PRIMITIVE.make()
      decomp.decompose(M)
    }
  }

  println("\nApache Common Math")
  locally {
    val M = new Array2DRowRealMatrix(N.toInt, N.toInt)
    val rng = new MersenneTwister(1234)
    for (i <- 0 until N)
      for (j <- 0 until N)
        M.setEntry(i, j, rng.nextDouble())

    val svd = time(s"Apache SVD with $N") {
      new SingularValueDecomposition(M)
    }
  }
}
