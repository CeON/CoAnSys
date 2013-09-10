if (! "kernlab" %in% row.names(installed.packages()))
  install.packages('kernlab')
require(kernlab)

getPolarW <- function(model,df){
  w=0
  iter=0  
  for(i in alphaindex(model)[[1]]){
    iter=iter+1
    w = w +  alpha(model)[[1]][iter] * df[i,-ncol(df)] *  df[i,ncol(df)]
  } 
  return (w)
}

giveHyperplane <- function(model,df){
  # w = sum_{s \in SV} a_0s \cdot y_s \cdot x_x
  b0 <- b(model)
  w <- getPolarW(model,df)
  index=1
  preequation = "f(x) ="
  equation <- paste(w[1],"*x",index," ",sep="")
  
  for(str in w[2:length(w)]){
    index=index+1
    equation <- paste(equation,"+ ",str,"*x",index," ",sep="")
  }
  equation <- paste(equation,"+ ",b0,sep="")
  return (c(equation,paste(preequation,equation)))
}


