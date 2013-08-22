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

# getPolarW <- function(model,df){
#   
#   model<-model3
#   df<-workset
#   
#   w=0
#   iter=0  
#   for(i in alphaindex(model)[[1]]){
#     i = alphaindex(model)[[1]][1]
#     iter=iter+1
#      w +  alpha(model)[[1]][iter] * df[i,-ncol(df)] *  df[i,ncol(df)]
#   } 
#   return (w)
# }
# 
# getPolarW <- function(model,df){
#   
#   alph * 
#   
#   w=0
#   iter=0  
#   for(i in ){
#     iter=iter+1
# length(workset[alphaindex(model3)[[1]],ncol(workset)] * alpha(model3)[[1]])
# str(workset[alphaindex(model3)[[1]],ncol(workset)] * alpha(model3)[[1]])
# 
# ncol(workset[alphaindex(model3)[[1]],-ncol(workset)])
# 
# dim(t(as.matrix(workset[alphaindex(model3)[[1]],-ncol(workset)])))
# head((as.matrix(workset[alphaindex(model3)[[1]],-ncol(workset)])))
# 
#  matrix(c(1,2,1,2),nrow=2,ncol=2) %*% matrix(c(1,2,3,4),nrow=2,ncol=2)
# 
# str(t(t(as.matrix(workset[alphaindex(model3)[[1]],-ncol(workset)]))) %*% t(workset[alphaindex(model3)[[1]],ncol(workset)] * alpha(model3)[[1]]))
#     w = t(as.matrix(workset[alphaindex(model3)[[1]],-ncol(workset)])) %*% t(workset[alphaindex(model3)[[1]],ncol(workset)] * alpha(model3)[[1]])
# 
# dim(t(as.matrix(workset[alphaindex(model3)[[1]],-ncol(workset)])) )
# dim(t(as.matrix(workset[alphaindex(model3)[[1]],ncol(workset)] * alpha(model3)[[1]])))
# 
# #     head(w)
# #   } 
# #   return (w)
# # }

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


