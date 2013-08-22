source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/74 Rysowanie WYKRESÓW MODYFIKACJI  0NullBool.R",sep=""))
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/75 Rysowanie WYKRESÓW MODYFIKACJI  NegNullBay.R",sep=""))
source(paste(getwd(),"/WMH/00 badania/99 kod wynikowy/76 Rysowanie WYKRESÓW MODYFIKACJI  NegNullBool.R",sep=""))

myplot <- function(i){
  if(i==1)plotNegNullBoolThreeVal()
  else if(i==2)plotNegNullBoolLinScale()
  else if(i==3)plotNegNullBoolSigmScale()
  else if(i==4)plot0NullBoolThreeVal()
  else if(i==5)plot0NullBoolLinScale()
  else if(i==6)plot0NullBoolSigmScale()
  else if(i==7)plotNegNullBayTwoVal()
  else if(i==8)plotNegNullBayLinScale()
  else if(i==9)plotNegNullBaySigmScale()
}

for(i in 1:9){
  now<-paste(date(),proc.time(),sep="_")
  fil=paste(getwd(),"/WMH/00 badania/98 wyniki/mod_fun_",i,".pdf",sep="")
  pdf(file=fil, height=4, width=5)
  myplot(i)
  dev.off()
}