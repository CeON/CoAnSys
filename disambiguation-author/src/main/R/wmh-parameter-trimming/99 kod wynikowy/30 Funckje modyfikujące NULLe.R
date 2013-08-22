################################################
# funckja modyfikująca NULLe
# n=-1 -- no changes
# n=0  -- Null=0
# n=2  -- Null=-abs(max(data$col))
################################################
convertNull <- function(d, n){
  if(n==-1) return(d) #zwrócenie danych niezmienionych
  if(n==0) return(null2Zero(d)) #zwrócenie danych niezmienionych
  if(n==2) return(null2MinusAbsMax(d)) #zwrócenie danych niezmienionych  
}
################################################
null2Zero <- function(d){
  for(c in 1:ncol(d)){
    d[d[,c]==-1,c] <- 0
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(null2Zero(tst))
# summary(null2Zero(d=tst))
################################################
null2MinusAbsMax <- function(d){
  for(c in 1:ncol(d)){
    mx <- -abs(max(d[,c]))
    if(length(d[d[,c]==-1,c])==0){
      print(paste("No null val detected in column number ",c)) 
    }else{
      d[d[,c]==-1,c] <- mx
    }
  }
  return (d)  
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(null2MinusAbsMax(tst))
# summary(null2MinusAbsMax(tst))
################################################
