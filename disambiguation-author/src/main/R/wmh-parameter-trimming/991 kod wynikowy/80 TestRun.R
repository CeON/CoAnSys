pt = proc.time()
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/81 Wmh with null.R",sep=""))
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))

## PATTERNS FOR LOOPS ###
#cost_pattern = 10^(0:3)
# frac_pattern = 2 *
#   1/10^(3)
# model_pattern = c(-1:2,10:12,20:22)
# kernel_pattern = 0:2
# degree_pattern = 2:9
# gamma_pattern = c(
#                   0.125,
#                   0.25,0.5
#                   ,1
#                   )
# coefficient_pattern = 0
#   seq(0,1,by=0.1)
frac_pattern = 2/10^(3)
model_pattern = c(-1:2,10:12,20:22)
kernel_pattern = 0:3
degree_pattern = 
#   3
  1:10
gamma_pattern = 
#   c(0.25,0.5)
  1:8/8
coefficient_pattern = 
#   0
  seq(0,1,by=0.2)
cost_pattern = 10^(0:2)

tetete <- length(frac_pattern)*length(model_pattern)*length(kernel_pattern)*length(degree_pattern)*length(gamma_pattern)*length(coefficient_pattern)*length(cost_pattern)
print(paste("Num of combinations",tetete))
print(paste("Time prediction", 28/240 * tetete,"[s]",26/240/60 *tetete,"min",26/240/60/60 *tetete,"godz"))
dane <- d
head(dane)

for(i in 2*1:4){
  gamma_pattern = (i-1):i/8  
  pt = proc.time()
  result <- wmh_with_null(frac_pattern,model_pattern,kernel_pattern,degree_pattern,
                            gamma_pattern,coefficient_pattern,log="",dane
                          ,cost_pattern
                          )
  pt = proc.time() -pt
  print(paste("Total time is:",pt[3]))
  
  # print(result)
  
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
  
  
  fname = paste("/home/pdendek/WMH/00 badania/98 wyniki/","wmh_with_null_",date(),"_",pt[3],".csv",sep="");
  write.csv(dd, file = fname)
}


