`tabela` <- read.table("/home/pdendek/dane_icm/2012-04-20-CEDRAM_N3_NEWPREDICATES/2012-04-22_20-51-35.csv", header=F, quote="\"")

names(tabela) = c("a@b.c","a@",
                  "CO_CONTR","MSC",
                  "KEY_PHRA","KEY_WORDS",
                  "CO_REF","CO_ISSN",
                  "CO_ISBN","FULL_INITZ","SAME","DBASE")
?svm
tmp = tabela

head(tmp)
tmp$DBASE <- NULL
# tmp$MSC <- NULL
tmp$CO_ISBN <- NULL
tmp$FULL_INITZ <- NULL
head(tmp)

tmp <- tmp[tmp$SAME!=0,  ]

tmp_diff <- tmp[tmp$SAME==-1,  ]
tmp_same <- tmp[tmp$SAME==1,  ]
same <- tmp[tmp$SAME==1,  ]
nrow(tmp_same)/nrow(tmp_diff)
#########bind
binded <- rbind(tmp_diff,tmp_diff)
binded <- rbind(binded,binded)
binded <- rbind(binded,tmp_diff)

diff <- binded[1:nrow(tmp_same),]

nrow(diff)
nrow(same)

tgthr <- rbind(diff,same)

head(tgthr)
convertData

x <- tgthr[,-ncol(tgthr)]
y <- tgthr[,ncol(tgthr)]
x <- convertData(x,21)
head(x)
head(y)
xy <- cbind(x,y)

head(xy)
# ?sample

# 1:nrow(x)

randomIndexes = sample(1:nrow(x), nrow(x),replace=F, prob=NULL)
head(randomIndexes)

rx <- x[randomIndexes,]
ry <- y[randomIndexes]

rx <- rx[1:(nrow(rx)-1),]
ry <- ry[1:(length(ry)-1)]
nrow(rx)/3

rhv <- runif(
  nrow( 
    as.data.frame(rx)
    ));

# rhv<0.1

# rx <- rx[rhv<0.4,]
# ry <- ry[rhv<0.4]

(nrow(rx)/3)
##########################################
##########################################
rx1fold <- rx[(0*nrow(rx)/3):(1*nrow(rx)/3),]
ry1fold <- ry[(0*nrow(rx)/3):(1*nrow(rx)/3)]

head(rx1fold)
head(ry1fold)

rx2fold <- rx[(1*nrow(rx)/3):(2*nrow(rx)/3),]
ry2fold <- ry[(1*nrow(rx)/3):(2*nrow(rx)/3)]

head(rx2fold)
head(ry2fold)

rx3fold <- rx[(2*nrow(rx)/3):(3*nrow(rx)/3),]
ry3fold <- ry[(2*nrow(rx)/3):(3*nrow(rx)/3)]

head(rx3fold)
head(ry3fold)

source(paste(getwd(),"/R_AND/linear.R", sep=""))
##########################################
for(i in 0:2){
  if(i==0){
    
    
    workset <- cbind(rx1fold,ry1fold)
    
    names(workset) = c("a@b.c","a@",
                      "CO_CONTR","MSC",
                      "KEY_PHRA","KEY_WORDS",
                      "CO_REF","CO_ISSN",
                      "SAME")
    head(workset)                 
    print(date())                 
    model1 <- ksvm(SAME~., data=as.matrix(workset)
                  , type="C-svc"
                  , scaled=FALSE
                  ,kernel = "vanilladot"
                 )    
    print(date())
    print(giveHyperplane(model1,workset)[1])
    print(date())
    otherX <- rbind(rx2fold,rx3fold)
    otherY <- append(ry2fold,ry3fold)
  
    head(otherX)
    pred1 <- predict(model1,otherX)
    print(table(pred1, otherY))
    print(paste("T=",(table(pred1, otherY)[1,1]+table(pred1, otherY)[2,2])/nrow(otherX)))
  }
  if(i==1){
    
    workset <- cbind(rx,ry)
    names(workset) = c("a@b.c","a@",
                       "CO_CONTR","MSC",
                       "KEY_PHRA","KEY_WORDS",
                       "CO_REF","CO_ISSN",
                       "SAME")
    
    print(date())
    model2 <- ksvm(SAME~., data=as.matrix(workset)
                   , type="C-svc"
                   , scaled=FALSE
                   ,kernel = "vanilladot"
                   )    
    print(date())
    print(giveHyperplane(model2,workset)[1])
    print(date())
    otherX <- rbind(rx1fold,rx3fold)
    otherY <- append(ry1fold,ry3fold)
    
    pred2 <- predict(model2,otherX)
    print(table(pred2, otherY))
    print(paste("T=",(table(pred2, otherY)[1,1]+table(pred2, otherY)[2,2])/nrow(otherX)))
  }
  if(i==2){
    
    workset <- cbind(rx3fold,ry3fold)
    names(workset) = c("a@b.c","a@",
                       "CO_CONTR","MSC",
                       "KEY_PHRA","KEY_WORDS",
                       "CO_REF","CO_ISSN",
                       "SAME")
    
    print(date())
    model3 <- ksvm(SAME~., data=as.matrix(workset)
                   , type="C-svc"
                   , scaled=FALSE
                   ,kernel = "vanilladot"
                   )    
    print(date())
    print(giveHyperplane(model3,workset)[1])
    print(date())
    otherX <- rbind(rx1fold,rx2fold)
    otherY <- append(ry1fold,ry2fold)
    str(ry1fold)
    
    pred3 <- predict(model3,otherX)
    print(table(pred3, otherY))
    print(paste("T=",(table(pred3, otherY)[1,1]+table(pred3, otherY)[2,2])/nrow(otherX)))
  }
}
##########################################
#model <- svm(x,y,type='C',kernel="radial",cachesize = 1024)
#model <- svm(x,y,type='C',kernel="linear",cachesize = 1024)