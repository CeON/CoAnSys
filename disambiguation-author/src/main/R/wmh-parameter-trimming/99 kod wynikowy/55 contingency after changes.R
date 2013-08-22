pt = proc.time()
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/81 Wmh with null.R",sep=""))
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))

tmp <- d

s0 <- balanceDataAndReduceNumer(d,0.003)

model_pattern = c(0,10,20)
pt = proc.time()
counter=0
for(mod in model_pattern){
      tit <- ""
      if(mod==-1) tit <- "No Changes" #zwrócenie danych niezmienionych
      if(mod==00) tit <- "NegBoolThreeVal" #zwrócenie danych niezmienionych
      if(mod==01) tit <- "NegBoolLinScale" #zwrócenie danych niezmienionych
      if(mod==02) tit <- "NegBoolSigmScale" #zwrócenie danych niezmienionych
      if(mod==10) tit <- "BayTwoVal" #zwrócenie danych niezmienionych
      if(mod==11) tit <- "BayLinScale" #zwrócenie danych niezmienionych  
      if(mod==12) tit <- "BaySigmScale" #zwrócenie danych niezmienionych
      if(mod==20) tit <- "ZeroBoolThreeVal" #zwrócenie danych niezmienionych
      if(mod==21) tit <- "ZeroBoolLinScale" #zwrócenie danych niezmienionych
      if(mod==22) tit <- "ZeroBoolSigmScale" #zwrócenie danych niezmienionych
  
      s1 = convertData(s0,mod)
  
      counter=counter+1
      now<-paste(date(),proc.time(),sep="_")
      filename=paste(getwd(),"/WMH/00 badania/98 wyniki/mds_",counter,".pdf",sep="")
      pdf(file=filename, height=4, width=5)
      
      for(i in 1:4){
        print("===================================")
        print(paste("This is mod",tit,"; ",i,"th clue (",nam[i],")", sep=""))  
  
        #print(summary(tmp[,i]))
        #print(levels(factor(tmp[,i])))
  
        cmm = table(s1[,i],s1[,ncol(s1)])
        colnames(cmm)=c("POSITIVE","NEGATIVE")
        print(cmm, sep=" & ")
        cm = cmm[2:nrow(cmm),]
        plot(cm)
      }
}
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))