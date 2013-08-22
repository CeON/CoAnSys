##############################################
######## Pozostałe w grze wskazówki ##########
##############################################

####################################
########### 3CoContribution ########
########### 4CoClassif #############
########### 5CoKeywordPhrase #######
########### 8Year ##################
####################################
pt = proc.time()
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/81 Wmh with null.R" ,sep=""))
pt = proc.time() -pt
print(paste("Load time is:",pt[3]))

tmp <- d

head(dane)
nam <- c("3CoContribution","4CoClassif","5CoKeywordPhrase","8Year")

for(i in 1:4){
  print("===================================")
  print(paste("This is ",i,"th clue (",nam[i],")", sep=""))  
  
  #print(summary(tmp[,i]))
  #print(levels(factor(tmp[,i])))
  
  cmm = table(tmp[,i],tmp[,ncol(tmp)])
  colnames(cmm)=c("NEGATIVE","POSITIVE")
  print(cmm, sep=" & ")
  cm = cmm[2:nrow(cmm),]
  plot(cm)
}