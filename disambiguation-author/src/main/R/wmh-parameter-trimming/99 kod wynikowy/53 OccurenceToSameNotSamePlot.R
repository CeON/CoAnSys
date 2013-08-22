pt = proc.time()
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/81 Wmh with null.R",sep=""))
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))

tmp <- d

head(dane)
tmpNames <- c("CA","CC","KP","YT")

for(i in 1:4){
  tetext = paste("Wskazówka ",tmpNames[i], sep="")
  print(tetext)
  tab = table(tmp[,i],tmp[,ncol(tmp)])
  colnames(tab) = c("NIE TA SAMA OSOBA","TA SAMA OSOBA")
  print(tab)
  
  t2 = t(tab)
  
  rna = rownames(tab)
  rna[rna=="-1"]="NULL"
  rownames(tab) = rna
  tab[tab[,1]==0,1]=0.1
  tab[tab[,2]==0,2]=0.1

  now<-paste(date(),proc.time(),sep="_")
  filename=paste(getwd(),"/WMH/00 badania/98 wyniki/value2same_not_same_",now,".pdf",sep="")
  pdf(file=filename, height=4, width=5)
  barplot(t(tab),
            main=tetext,
            ylab="Ilość wystąpień",
            xlab="Wartość cechy",
            space=0,1,
            legend=T,
            #beside=T,
            cex.axis=0.8, 
            las=1,
            cex=0.8,
#             col=gray.colors(2),
            col=heat.colors(2),
            log="y",
            args.legend = list(x="topright"))    
  dev.off()
}
