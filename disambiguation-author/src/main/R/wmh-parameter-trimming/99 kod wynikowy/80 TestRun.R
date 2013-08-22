pt = proc.time()
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/81 Wmh with null.R",sep=""))
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))

## PATTERNS FOR LOOPS ###
frac_pattern = 2/10^(3)
model_pattern = c(-1:1,10:11,20:21)
kernel_pattern = 0:2
coefficient_pattern = seq(0,1,by=0.1)
cost_pattern = 1
  10^(0:2)

tetete <- length(frac_pattern)*length(model_pattern)*length(kernel_pattern)*length(degree_pattern)*length(gamma_pattern)*length(coefficient_pattern)*length(cost_pattern)
print(paste("Num of combinations",tetete))
print(paste("Time prediction", 28/240 * tetete,"[s]",26/240/60 *tetete,"min",26/240/60/60 *tetete,"godz"))
dane <- d
head(dane)

frac_pattern = 2/10^(3)
for(degree_pattern in 1:3){
  for(gamma_pattern in c(0.25,0.5)){
    pt = proc.time()
    result <- wmh_with_null(frac_pattern,model_pattern,kernel_pattern,degree_pattern,
                              gamma_pattern,coefficient_pattern,log="",dane
                            ,cost_pattern
                            )
    pt = proc.time() -pt
    print(paste("[",date(),"]    For deg =",degree_pattern,", gamma =",gamma_pattern," total time is:",pt[3]))    
    dd<-result
    dd[dd[,2]==-1,2] <- "NoChange"
    dd[dd[,2]==0,2] <- "MinusBoolThreeVal"
    dd[dd[,2]==1,2] <- "MinusBoolLinScale"
    dd[dd[,2]==2,2] <- "MinusBoolSigmScale"
    dd[dd[,2]==10,2] <- "MinusBayTwoVal"
    dd[dd[,2]==11,2] <- "MinusBayLinScale"
    dd[dd[,2]==12,2] <- "MinusBaySigmScale"
    dd[dd[,2]==20,2] <- "ZeroBoolThreeVal"
    dd[dd[,2]==21,2] <- "ZeroBoolLinScale"
    dd[dd[,2]==22,2] <- "ZeroBoolSigmScale"    
    dd[dd[,3]==0,3] <- "Line"
    dd[dd[,3]==1,3] <- "Poly"
    dd[dd[,3]==2,3] <- "RBF"
    dd[dd[,3]==3,3] <- "Sigm"
    head(dd[,1:9])
    
    
    fname=paste(getwd(),"/WMH/00 badania/98 wyniki/",date(),"_",pt[3],".csv",sep="")
    write.csv(dd, file = fname)
  }
}

coun=0
fil_lst <-list()
file.dir <- paste(getwd(),"/WMH/00 badania/98 wyniki/",sep="");
for(infile in dir(file.dir, pattern="\\.csv$")){
  coun=coun+1
  file <- read.csv(file=paste(getwd(),"/WMH/00 badania/98 wyniki/",infile,sep=""),header=T, quote="\"",sep=",")
  fil_lst[[coun]] = file
}
load <- do.call("rbind", fil_lst)
# load <- load[,2:ncol(load)]
# fname = paste(getwd(),"/WMH/00 badania/98 wyniki/aggr/","wmh_with_null_",date(),"_",pt[3],".csv",sep="");
# write.csv(load, file = fname)






load <- do.call("rbind", fil_lst)
head(load)
load <- load[,2:ncol(load)]
cbind(names(load),1:12)
# head(load)
load <- load[,c(-1,-10)]
cbind(names(load),1:10)

################################## AUX ###################################
general <- c(1,2,10,3,4,5)
############################ min.correct.rate (BEST) ############################
head( load[order(load$min.correct.rate ,decreasing = TRUE),c(general,6,7,8,9)],n=10 )
############################ avg.correct.rate (BEST) ############################
head( load[order(load$avg.correct.rate ,decreasing = TRUE),c(general,7,6,8,9)],n=10 )
############################ max.correct.rate (BEST) ############################
head( load[order(load$max.correct.rate. ,decreasing = TRUE),c(general,8,6,7,9)],n=10 )
############################ time.elapsed (BEST) ############################
head( load[order(load$time.elapsed ,decreasing = FALSE),c(general,9,6,7,8)],n=30 )

############################ min.correct.rate (WORST) ############################
head( load[order(load$min.correct.rate ,decreasing = FALSE),c(general,6,7,8,9)],n=10 )
############################ avg.correct.rate (WORST) ############################
head( load[order(load$avg.correct.rate ,decreasing = FALSE),c(general,7,6,8,9)],n=10 )
############################ max.correct.rate (WORST) ############################
head( load[order(load$max.correct.rate. ,decreasing = FALSE),c(general,8,6,7,9)],n=10 )
############################ time.elapsed (WORST) ############################
head( load[order(load$time.elapsed ,decreasing = TRUE),c(general,9,6,7,8)],n=30 )

