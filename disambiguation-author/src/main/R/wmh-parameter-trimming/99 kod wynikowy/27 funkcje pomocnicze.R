################################################
############ funckja dzieląca na n części
################################################
splitIntoNPieces <- function(d, n){
  rhv <- runif(
            nrow( 
              as.data.frame(d)
            ));
  ret <- list()  
  for(i in 1:n){
    beg = (i-1)/n
    end = i/n
    s <- d[rhv>beg & rhv<end,]
    ret[[i]] <- as.data.frame(s)
  }
  return (ret)
}
################################################
############ funckja składająca ramki z listy
############ oprócz zadanej jednej
################################################
mergeAllButOne <- function(lst, wot){
  tmp=data.frame()
  for(i in 1:length(lst)){
    if(i==wot)next;
    tmp=rbind(tmp,lst[[i]])
  }
  return (tmp)
}
################################################
############ funckja przedstawiająca 
############ dane wielowymiarowe w 2D
################################################
visualizeXDIn2D <- function(d,mod){
  plot(cmdscale(dist(d[,-ncol(d)])),
     col = (as.integer(d[,ncol(d)])+15),
     pch = c("o","+")[1:nrow(d) %in% mod$index + 1])
}
################################################
############ funckja przedstawiająca 
############ przejście X:Val -> Y:Class
################################################
visualizeXDIn2D <- function(x,y){
  if (! "ggplot2" %in% row.names(installed.packages()))
    install.packages('')
  require(ggplot2)
  qplot(y, x)  
}
################################################
############ end of functions
################################################