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

length(avgbd)
length(avgcacheFull)

dd <- cbind(as.numeric(size),
      as.numeric(avgCacheInit),
      as.numeric(avgCacheAffinity),
      as.numeric(avgCacheFull))
dd <- as.data.frame(dd)
names(dd) <- c("Size","Init","Aff","Full")


head(dd)
dd

write.csv(dd, file = "/home/pdendek/recalculated.csv")

?plot

                      
ns <- c("InitTime","AffTime","FullTime")

plot_colors <- c("red","blue","green")
head(dd)
plot(x=dd[,1],y=dd[,4],col=plot_colors[3],ann=FALSE
#      , type="o"
     ,log="y")
title(xlab= "Size")
title(ylab= "Time [ms]")
points(x=dd[,1],y=dd[,3], 
#        type="o",
       col=plot_colors[2])
points(x=dd[,1],y=dd[,2]
#        , type="o"
       ,col=plot_colors[1])
legend(x=0, 2*10^5, c(ns[3],ns[2],ns[1]) , cex=0.8,pch=c(16,4),
       col=plot_colors);