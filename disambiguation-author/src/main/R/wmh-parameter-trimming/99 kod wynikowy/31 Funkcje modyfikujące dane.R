################################################
# funckja modyfikująca dane
# -1 no change
# 00 NegBool-3-val
# 01 NegBool-lin-scal
# 02 NegBool-sigm-scal

# 10 Bay-2-val
# 11 Bay-lin-scal
# 12 Bay-sigm-scal

# 20 ZeroBool-3-val
# 21 ZeroBool-lin-scal
# 22 ZeroBool-sigm-scal
################################################
convertData <- function(d, n){
  if(n==-1) return(d) #zwrócenie danych niezmienionych
  if(n==00) return(data2NegBoolThreeVal(d)) #zwrócenie danych niezmienionych
  if(n==01) return(data2NegBoolLinScale(d)) #zwrócenie danych niezmienionych
  if(n==02) return(data2NegBoolSigmScale(d)) #zwrócenie danych niezmienionych
  if(n==10) return(data2BayTwoVal(d)) #zwrócenie danych niezmienionych
  if(n==11) return(data2BayLinScale(d)) #zwrócenie danych niezmienionych  
  if(n==12) return(data2BaySigmScale(d)) #zwrócenie danych niezmienionych
  if(n==20) return(data2ZeroBoolThreeVal(d)) #zwrócenie danych niezmienionych
  if(n==21) return(data2ZeroBoolLinScale(d)) #zwrócenie danych niezmienionych
  if(n==22) return(data2ZeroBoolSigmScale(d)) #zwrócenie danych niezmienionych  
}
################################################
data2NegBoolThreeVal <- function(d){
  for(c in 1:(ncol(d)-1)){
    d[d[,c]<0,c] <- -1
    d[d[,c]>0,c] <- 1
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(data2NegBoolThreeVal(tst))
# summary(data2NegBoolThreeVal(d=tst))
################################################
data2NegBoolLinScale <- function(d){
  for(c in 1:(ncol(d)-1)){
    mx <- abs(max(d[,c]))
    d[d[,c]<0,c] <- -mx
    d[,c] <- d[,c]/mx
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(data2NegBoolLinScale(tst))
# summary(data2NegBoolLinScale(d=tst))
################################################
data2NegBoolSigmScale <- function(d){
  if (! "e1071" %in% row.names(installed.packages()))
    install.packages('e1071')
  library('e1071')
  
  for(c in 1:(ncol(d)-1)){
    mx <- abs(max(d[,c]))
    d[d[,c]<0,c] <- -mx
    multip <- 5.3/mx
    d[,c] <- d[,c] * multip
    d[,c] <- sigmoid(d[,c])
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(data2NegBoolSigmScale(tst))
# summary(data2NegBoolSigmScale(d=tst))
################################################
# 
# data2NegBoolThreeVal <- function(d){
#   for(c in 1:(ncol(d)-1)){
#     d[d[,c]<0,c] <- -1
#     d[d[,c]>0,c] <- 1
#   }
#   return (d)
# }

data2BayTwoVal <- function(d){
  for(c in 1:(ncol(d)-1)){
    d[d[,c]<0,c] <- 0
    d[d[,c]>0,c] <- 1
  }
  return (d)
}
#Test block
# source("/home/pdendek/WMH/00 badania/99 kod wynikowy/27 funkcje pomocnicze.R")
# source("/home/pdendek/WMH/00 badania/99 kod wynikowy/28 wczytanie i upodządkowanie danych.R")
# source("/home/pdendek/WMH/00 badania/99 kod wynikowy/29 Funkcje balansujące dane.R")
# source("/home/pdendek/WMH/00 badania/99 kod wynikowy/30 Funckje modyfikujące NULLe.R")
# source("/home/pdendek/WMH/00 badania/99 kod wynikowy/31 Funkcje modyfikujące dane.R")
# source("/home/pdendek/WMH/00 badania/99 kod wynikowy/32 Funkcje uruchamiające budowę modelu.R")
# tst <- balanceDataAndReduceNumer(d,0.001)
# tst <- data2BayTwoVal(tst)
#   if (! "e1071" %in% row.names(installed.packages()))
#     install.packages('e1071')
#   library('e1071')
# LinModel(tst)
# summary(data2BayTwoVal(tst))
################################################
data2BayLinScale <- function(d){
  for(c in 1:(ncol(d)-1)){
    d[d[,c]<0,c] <- 0
    mx <- max(d[,c])
    d[d[,c]>0,c] <- d[d[,c]>0,c]/mx
    #d[,c] <- d[,c]/mx
    #summary(d)
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(data2BayLinScale(tst))
# summary(data2BayLinScale(tst))
################################################
data2BaySigmScale <- function(d){
  if (! "e1071" %in% row.names(installed.packages()))
    install.packages('e1071')
  library('e1071')
  
  for(c in 1:(ncol(d)-1)){
    d[d[,c]==-1,c] <- -abs(max(d[,c]))
    d[,c] <- sigmoid(d[,c])
    d[d[,c]<0,c] <- 0
    #d[,c] <- d[,c]/mx
    #summary(d)
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(data2BaySigmScale(tst))
# summary(data2BaySigmScale(tst))
################################################
data2ZeroBoolThreeVal <- function(d){
  for(c in 1:(ncol(d)-1)){
    d[d[,c]==-1,c] <- 0.1
    d[d[,c]==0,c] <- -1
    d[d[,c]==0.1,c] <- 0
    d[d[,c]>0,c] <- 1
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(data2NegBoolThreeVal(tst))
# summary(data2NegBoolThreeVal(d=tst))
################################################
data2ZeroBoolLinScale <- function(d){
  for(c in 1:(ncol(d)-1)){
    mx <- abs(max(d[,c]))
    
    d[d[,c]==-1,c] <- 0.1
    d[d[,c]==0,c] <- -mx
    d[d[,c]==0.1,c] <- 0
        
    d[,c] <- d[,c]/mx
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(data2NegBoolLinScale(tst))
# summary(data2NegBoolLinScale(d=tst))
################################################
data2ZeroBoolSigmScale <- function(d){
  if (! "e1071" %in% row.names(installed.packages()))
    install.packages('e1071')
  library('e1071')
#   s0 <- balanceDataAndReduceNumer(d,0.001)
#   d<-s0
  for(c in 1:(ncol(d)-1)){
#     c=1
    mx <- abs(max(d[,c]))
    
    d[d[,c]==-1,c] <- 0.1
    d[d[,c]==0,c] <- -mx
    d[d[,c]==0.1,c] <- 0
        
    multip <- 5.3/mx
    
    d[,c] <- d[,c]/mx
    
    d[,c] <- d[,c]*multip
    d[,c] <- sigmoid(d[,c])
  }
  return (d)
}
#Test block
# tst <- rbind(fs_t,fs_f)
# tst <- tst[,-ncol(tst)]
# d=tst
# head(data2NegBoolSigmScale(tst))
# summary(data2NegBoolSigmScale(d=tst))
################################################