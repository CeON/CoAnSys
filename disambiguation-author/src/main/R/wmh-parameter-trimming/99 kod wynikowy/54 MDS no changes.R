
pt = proc.time()
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/81 Wmh with null.R",sep=""))
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))

tmp <- d

s0 <- balanceDataAndReduceNumer(d,0.003)

model_pattern = c(0:2,10:12,20:22,-1)
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
      
      plot(cmdscale(dist(s1[,-5])),
        col = c("black","red"),
            ylab="",
            xlab="",)
      title(main=tit, col.main="red", font.main=4)
      
      dev.off()
}
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))