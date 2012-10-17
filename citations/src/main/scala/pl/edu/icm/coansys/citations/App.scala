package pl.edu.icm.coansys.citations

/**
 * @author Mateusz Fedoryszak (m.fedoryszak@icm.edu.pl)
 */
object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    println( "Hello World!" )
    println("concat arguments = " + foo(args))
  }

}
