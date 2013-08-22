cacheVSbd <- read.csv("~/Pobrane/AND_Cache_vs_BD.csv")

summary(cacheVSbd)

avgbd <- vector()
avgcacheFull <- vector()
avgcacheInit <- vector()
size <- vector()


for(i in levels(factor(cacheVSbd$Size))){
  i=1
  ca = cacheVSbd[cacheVSbd$Size==as.numeric(i),]
  avgbd = c(avgbd,sum(ca[,2])/nrow(ca))
  avgcacheFull = c(avgcacheFull,sum(ca[,5])/nrow(ca))
  avgcacheInit = c(avgcacheInit,sum(ca[,3])/nrow(ca))
  size = c(size,i)
} 

length(avgbd)
length(avgcacheFull)

dd <- cbind(as.numeric(size),
      as.numeric(avgbd),
      as.numeric(avgcacheFull),
      as.numeric(avgcacheInit))

dd

write.csv(dd, file = "/home/pdendek/recalculated.csv")

plot_colors <- c("red","blue","green")

plot(x=dd[,1],y=dd[,2],col=plot_colors[1],ann=FALSE
     , type="o")
title(xlab= "Size")
title(ylab= "Time [ms]")
lines(x=dd[,1],y=dd[,3], type="o",
   col=plot_colors[2])
lines(x=dd[,1],y=dd[,4], type="o",
   col=plot_colors[3])
legend(x=0, 7*10^6, c("BData","CacheTotal","CacheInit"), cex=0.8,pch=c(16,4),
       col=plot_colors);