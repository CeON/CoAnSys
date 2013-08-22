plot0NullBoolThreeVal()
plot0NullBoolLinScale()
plot0NullBoolSigmScale()
#########################

plot0NullBoolLinScale <- function(){
  x=c(0,1)
  y=c(-1,1)
  null=c(-1,0)
  
  if (! "ggplot2" %in% row.names(installed.packages()))
    install.packages('')
  require(ggplot2)
  
  plot(min(c(x,null[1])):max(x), min(c(y,null[2])):max(y),xlab="",ylab="",type="n")
  lines(c(0,1),c(0,1),col="black", pch=1)
  points(1,1,col="black", pch=16) #zaznacza pEŁNE kółka
  points(0,0,col="black", pch=1) #zaznacza pEŁNE kółka
  points(0,-1,col="black", pch=16) #zaznacza pEŁNE kółka
  points(null[1],null[2],col="black",pch=4) # zaznacza krzyżyk
  title(main="0NullBoolLinScale", col.main="red", font.main=4)
  legend(x=min(c(x,null[1])), max(y), c("Regular Values","Null Value"), cex=0.8,pch=c(16,4));
}

plot0NullBoolThreeVal <- function(){
  x=c(0,1)
  y=c(-1,1)
  null=c(-1,0)
  
  if (! "ggplot2" %in% row.names(installed.packages()))
    install.packages('')
  require(ggplot2)
  
  plot(min(c(x,null[1])):max(x), min(c(y,null[2])):max(y),xlab="",ylab="",type="n")
  lines(c(0,1),c(1,1),col="black", pch=1)
  points(0,1,col="black", pch=1) #zaznacza pEŁNE kółka
  points(x,y,col="black", pch=16) #zaznacza pEŁNE kółka
  points(null[1],null[2],col="black",pch=4) # zaznacza krzyżyk
  title(main="0NullBoolThreeVal", col.main="red", font.main=4)
  legend(x=min(c(x,null[1])), max(y), c("Regular Values","Null Value"), cex=0.8,pch=c(16,4));
}

plot0NullBoolSigmScale  <- function(){
  
  if (! "e1071" %in% row.names(installed.packages()))
    install.packages('e1071')
  library('e1071')
  if (! "ggplot2" %in% row.names(installed.packages()))
    install.packages('')
  require(ggplot2)
  
  x=c(seq(0,5.3,by=0.1))  
  y=sigmoid(x)
  null=c(-5.3,sigmoid(0))
  
  y_range <- range(0,1)
  x_range <- range(-5.3,5.3)
  plot(x,y, type='l', lwd=2,ylim=y_range,xlim=x_range)
  points(null[1],null[2],col="black",pch=4) # zaznacza krzyżyk
  points(0,0.5,col="black",pch=1) # zaznacza krzyżyk
  points(0,0,col="black",pch=4) # zaznacza krzyżyk
  points(5.3,1,col="black",pch=16) # zaznacza krzyżyk
  title(main="0NullBoolSigmScale", col.main="red", font.main=4)
  legend(x=min(c(x,null[1])), max(y), c("Regular Values","Null Value"), cex=0.8,pch=c(16,4));
}