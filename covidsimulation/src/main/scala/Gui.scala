import scala.swing._
import java.io._
import java.net._
import org.graphstream.graph._
import org.graphstream.graph.implementations._
import akka.actor._
import scala.collection.mutable.ListBuffer

object Gui {
  var graph:SingleGraph = new SingleGraph("Tutorial 1");
  def main() {

    System.setProperty("org.graphstream.ui", "swing"); 
    //val graph = new MyGraph
   // graph.run
    //graph 
        graph.setAttribute("ui.stylesheet", "url(file://"+System.getProperty("user.dir")+"/src/main/scala/style/stylesheet.css)");

    graph.display();

    println("End of main function")
  }
  def addBuildings(str:String,num:Int){
    for (i <- 0 to num){
      val n:Node =graph.addNode(str+i.toString)   
      n.setAttribute("ui.class", "building, "+str);

      //n.setAttribute("type",str)
    }
     
  }
  def addCitizen(str:String,home:Int,work:Int){

      val n:Node= graph.addNode(str)
      n.setAttribute("ui.class", "citizen");
      graph.addEdge("EH"+str,str,"H"+home)
      if(work > -1)
        graph.addEdge("EW"+str,str,"W"+work)
      //graph.addEdge(ppl(p).toString ,ppl(p).toString, "H")

        //case "worker"  => graph.addEdge(ppl(p).toString ,ppl(p).toString, "W")

    
  }
  def infect(str:String){
    val n:Node= graph.getNode(str)
    n.setAttribute("ui.class", "infected");
  }
}