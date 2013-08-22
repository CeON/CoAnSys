# qwertyqwerty=TRUE #BUDOWA PROSTA
qwertyqwerty=FALSE #BUDOWA ZŁOŻONA
################################################
# funckja budujące model
# 0 lin
# 1 pol
# 2 rbf
# 3 sigm
################################################
createModel <- function(d, n, deg,gam, coef,cost){
  if (! "e1071" %in% row.names(installed.packages()))
    install.packages('e1071')
  library('e1071')
  
#   dane <- d
#   n=1
#   deg=3
#   gam=0.25
#   coef=0.1
#   cost=10
  
  if(n==0){
    pt <- proc.time()
    res = LinModel(d,cost)
    val = proc.time() - pt
    return (c(res,time=val[3]))
  } 
  if(n==1){
    pt <- proc.time()
    res = PolModel(d, deg,gam, coef,cost) #zwrócenie danych niezmienionych
    val = proc.time() - pt
    return (c(res,time=val[3]))
  } 
  if(n==2){
    pt <- proc.time()
    res = RBFModel(d, deg,gam, coef,cost) #zwrócenie danych niezmienionych
    val = proc.time() - pt
    return (c(res,time=val[3]))
  }
  if(n==3){
    pt <- proc.time()
    res = SigmoidModel(d, deg,gam, coef,cost) #zwrócenie danych niezmienionych
    val = proc.time() - pt
    return (c(res,time=val[3]))
  }
}
################################################
################# Test block ###################
# tst <- rbind(fs_t,fs_f)
# LinModel(tst)
############### Function block #################
# dat<-dane
# c<-1
#dat <- balanceDataAndReduceNumer(dat,0.001)
LinModel <- function(dat,c){
  tp <- list()
  mintp = 100;
  maxtp = 0;
  w = splitIntoNPieces(d=dat,n=5)
  for(i in 1:length(w)){
#     print("---------------------")
    tst <- mergeAllButOne(w,i)
    x <- tst[,-ncol(tst)]
    y <- tst[,ncol(tst)]
        
    model <- svm(x,y,type='C',kernel='linear',cost=c,cachesize = 1024)
    
    # Check if the model is able to classify your *training* data.
    x <- w[[i]][,-ncol(d)]
    y <- w[[i]][ncol(d)]
    
    pred <- predict(model, x)
    pred <- as.data.frame(pred)
    
    mintp = min(mintp,100*sum(pred==y)/nrow(x))
    maxtp = max(maxtp,100*sum(pred==y)/nrow(x))
    tp[[i]] <- 100*sum(pred==y)/nrow(x)
  } 
  avgtp <- 0
  for(i in 1:length(w)){
    avgtp <- avgtp + tp[[i]]
  }
  avgtp <- avgtp/length(w)
    return (list("min-correct-rate%"=mintp,
               "avg-correct-rate%"=avgtp,
               "max-correct-rate%"=maxtp,
               "warning/error"=NA))
#   return (avgtp)
}
################################################
################################################
################# Test block ###################
# tst <- rbind(fs_t,fs_f)
# PolModel(tst,1,1/10,0)
############### Function block #################
PolModel <- function(d, deg,gam, coef,c){
  tp <- list()
  mintp = 100;
  maxtp = 0;
  dat=d
  w = splitIntoNPieces(d=dat,n=5)
  for(i in 1:length(w)){
#     print("---------------------")
    tst <- mergeAllButOne(w,i)
    x <- tst[,-ncol(tst)]
    y <- tst[,ncol(tst)]
    
    if(qwertyqwerty){
      model <- svm(x,y,type='C',kernel="polynomial",cachesize = 1024)
    }else{
      model <- svm(x,y,type='C',kernel="polynomial",degree=deg,gamma=gam,coef0=coef,cost=c,cachesize = 1024)  
    }
    
    
    # Check if the model is able to classify your *training* data.
    x <- w[[i]][,-ncol(d)]
    y <- w[[i]][ncol(d)]
    
    pred <- predict(model, x)
    pred <- as.data.frame(pred)
    
    mintp = min(mintp,100*sum(pred==y)/nrow(x))
    maxtp = max(maxtp,100*sum(pred==y)/nrow(x))
    tp[[i]] <- 100*sum(pred==y)/nrow(x)
  } 
  avgtp <- 0
  for(i in 1:length(w)){
    avgtp <- avgtp + tp[[i]]
  }
  avgtp <- avgtp/length(w)
  return (list("min-correct-rate%"=mintp,
               "avg-correct-rate%"=avgtp,
               "max-correct-rate%"=maxtp,
               "warning/error"=NA))
}
################################################
################################################
################# Test block ###################
# tst <- balanceDataAndReduceNumber(s0,0.001)
# RBFModel(s0,1,1/10,0)
############### Function block #################
RBFModel <- function(d, deg,gam, coef,c){
  tp <- list()
  mintp = 100;
  maxtp = 0;
  dat=d
  w = splitIntoNPieces(d=dat,n=5)
  for(i in 1:length(w)){
#     print("---------------------")
    
    tst <- mergeAllButOne(w,i)
    x <- tst[,-ncol(tst)]
    y <- tst[,ncol(tst)]
    
    if(qwertyqwerty){
      model <- svm(x,y,type='C',kernel="radial",cachesize = 1024)
    }else{
      model <- svm(x,y,type='C',kernel="radial",degree=deg,gamma=gam,coef0=coef,cost=c,cachesize = 1024)
    }
    
    x <- w[[i]][,-ncol(d)]
    y <- w[[i]][ncol(d)]
    
    # Check if the model is able to classify your *training* data.
    x <- w[[i]][,-ncol(d)]
    y <- w[[i]][ncol(d)]
    
    pred <- predict(model, x)
    pred <- as.data.frame(pred)
    
    mintp = min(mintp,100*sum(pred==y)/nrow(x))
    maxtp = max(maxtp,100*sum(pred==y)/nrow(x))
    tp[[i]] <- 100*sum(pred==y)/nrow(x)
  } 
  avgtp <- 0
  for(i in 1:length(w)){
    avgtp <- avgtp + tp[[i]]
  }

  avgtp <- avgtp/length(w)
  return (list("min-correct-rate%"=mintp,
               "avg-correct-rate%"=avgtp,
               "max-correct-rate%"=maxtp,
               "warning/error"=NA))
}
################################################
################################################
################# Test block ###################
# tst <- balanceDataAndReduceNumber(s0,0.001)
# RBFModel(s0,1,1/10,0)
############### Function block #################
SigmoidModel <- function(d, deg,gam, coef,c){
  tp <- list()
  mintp = 100;
  maxtp = 0;
  dat=d
  w = splitIntoNPieces(d=dat,n=5)
  for(i in 1:length(w)){
#     print("---------------------")
    tst <- mergeAllButOne(w,i)
    x <- tst[,-ncol(tst)]
    y <- tst[,ncol(tst)]
    
    if(qwertyqwerty){
      model <- svm(x,y,type='C',kernel="sigmoid",cachesize = 1024)
    }else{
      model <- svm(x,y,type='C',kernel="sigmoid",cachesize = 1024,degree=deg,gamma=gam,coef0=coef,cost=c)
    }    
    
    x <- w[[i]][,-ncol(d)]
    y <- w[[i]][ncol(d)]
    
    # Check if the model is able to classify your *training* data.
    x <- w[[i]][,-ncol(d)]
    y <- w[[i]][ncol(d)]
    
    pred <- predict(model, x)
    pred <- as.data.frame(pred)
    
    mintp = min(mintp,100*sum(pred==y)/nrow(x))
    maxtp = max(maxtp,100*sum(pred==y)/nrow(x))
    tp[[i]] <- 100*sum(pred==y)/nrow(x)
  } 
  avgtp <- 0
  for(i in 1:length(w)){
    avgtp <- avgtp + tp[[i]]
  }
  avgtp <- avgtp/length(w)
  return (list("min-correct-rate%"=mintp,
               "avg-correct-rate%"=avgtp,
               "max-correct-rate%"=maxtp,
               "warning/error"=NA))
}
################################################