init <-function(){

nohup <- read.delim("~/nohup.csv", header=F)
names(nohup) <- c("Size","Init","Aff")
head(nohup)
cacheVSbd <- nohup
summary(cacheVSbd)

avgCacheInit <- vector()
avgCacheAffinity <- vector()
avgCacheFull <- vector()
size <- vector()



for(i in levels(factor(sort(cacheVSbd$Size)))){
  ca = cacheVSbd[cacheVSbd$Size==as.numeric(i),]
  avgCacheInit = c(avgCacheInit,sum(ca[,2])/nrow(ca))
  avgCacheAffinity = c(avgCacheAffinity,sum(ca[,3])/nrow(ca))
  avgCacheFull = c(avgCacheFull,sum(ca[,2])/nrow(ca) + sum(ca[,3])/nrow(ca))
  size = c(size,i)
} 

dd <- cbind(as.numeric(size),
      as.numeric(avgCacheInit),
      as.numeric(avgCacheAffinity),
      as.numeric(avgCacheFull))
dd <- as.data.frame(dd)
names(dd) <- c("Size","Init","Aff","Full")


head(dd)
dd

dd[,2] = dd[,2]/1000
dd[,3] = dd[,3]/1000
dd[,4] = dd[,4]/1000

write.csv(dd, file = "/home/pdendek/recalculated.csv")

# ?plot

                      
ns <- c("InitTime","AffTime","FullTime")
plot_colors <- c("red","blue","green")
namecol <- cbind(ns,plot_colors)

head(dd)
plot(x=dd[,1],y=dd[,4],col=namecol[3,2],ann=FALSE
#      , type="o"
     ,pch=16
#      ,log="y"
#      ,ylim=range(0.1,10000)
     )
title(xlab= "Size")
title(ylab= "Time [sec]")
points(x=dd[,1],y=dd[,3], 
#        type="o",
       ,pch=16
       ,col=namecol[2,2])
points(x=dd[,1],y=dd[,2]
#        , type="o"
       ,pch=16
       ,col=namecol[1,2])


legend(x=0, 2*10^6, namecol[,1] , cex=0.8,pch=c(16,16,16),
       col=namecol[,2]);
}

init()

mcu<-function(x){
  print(0.0001833*x^1.625)
}
v = mcu(c(2115,3007,4064,5113,7162,9810))
mcu(24000)/60
sum(v)/60

plot(x,y)
points(x,mcu(x),col=2)

x <- dd[,1]
y <- dd[,4]
lm.r <-lm(y~ -1 +   x + I(x^2))
lm.r
summary(lm.r)
?lm
x
y
m <- nls(Full ~ I(Size^power), data = dd[,c(1,4)], start = list(power = 1), trace = T)
summary(m)


# yyy0 <- function(x){--6.227     +      2.069*x^0.3037567}
# yyy1 <- function(x){-0.3377555  + 0.0002063*x^1.6}
yyy2 <- function(x){-3.307e-02  +  1.012e-05*x^2 } #minutes
yyy3 <- function(x){0.0608099*x + 0.0005811*x^2 } #seconds

?plot
plot(x,y
#      ,xlim=range(0,24000)
#      ,ylim=range(0,5)
     ,ann=F
     )
# lines(x,yyy0(x),col="red")
lines(x,yyy3(x),col="red")
# lines(x,yyy3(x),col="blue")
title(main="0.0608099*x + 0.0005811*x^2")
title(xlab= "Size")
title(ylab= "Time [seconds]")


yyy3(1)

yyy3(7162)/60

yyy2(24000)/60/24
yyy2(5000)/60