results <- read.csv(file=  paste(getwd(),"/WMH/00 badania/98 wyniki/wmh_with_null_Sat Jan 14 19:13:46 2012.csv",sep=""),
         header=TRUE,sep=",",dec=".",na.strings = "NA")
results <- results[,2:ncol(results)]
d <-results

tmp <- d
tmp <- d[d[,10]==NA,]
head(d[,])
# dim(d)
tmp <- d[ ,c(2,3,4,5,6,8,9,11)]

length(mod_le <- levels(factor( tmp$mod )))
length(ker_le <- levels(factor( tmp$ker )))
length(deg_le <- levels(factor( tmp$deg )))
length(gam_le <- levels(factor( tmp$gam )))
length(coef_le <- levels(factor( tmp$coef )))

### MODIFICATIONS -> {AvgCorr,MaxCorr,TimeElapsed} 
### PLOT
for(finval in c(6,7,8)){
  now<-paste(date(),proc.time(),sep="_")
  fil=paste("/home/pdendek/WMH/00 badania/98 wyniki/wmh_with_null_",now,".pdf",sep="")
  pdf(file=fil, height=3.5, width=5)
  # Trim off excess margin space (bottom, left, top, right)
  par(mar=c(4.2, 3.8, 0.2, 0.2))
  ### produce chart of given FinalVal
  g_range <- range(min(tmp[,finval]),max(tmp[,finval]))    
  print(fil)
  print(g_range)
  #first of list
  i = mod_le[1]
  i_mod = tmp[tmp$mod==i,]  
  
  plot(sort(i_mod[,finval],decreasing=FALSE), 
       lty=which(mod_le==i), 
       col=palette()[1], 
       ylim=g_range, 
       axes=TRUE, ann=FALSE, type="l")
  
  
  
  for(i in mod_le[2:length(mod_le)]){
    ### produce line of given MOD
    i_mod = tmp[tmp$mod==i,]
    # Graph trucks with red dashed line and square points
    lines(sort(i_mod[,finval],decreasing=FALSE), lty=which(mod_le==i), 
          col=palette()[which(mod_le==i)]
          )    
    }
  title(xlab="Number of modification version")
  title(ylab=names(tmp)[finval])
  
  legend(1, g_range[2], mod_le, cex=0.8, 
   palette(), lty=1:length(mod_le));
#   title(main=paste("Modifications","vs",names(tmp)[finval]), col.main="red", font.main=4)
  # Turn off device driver (to flush output to PDF)
  dev.off()

  # Restore default margins
  par(mar=c(5, 4, 4, 2) + 0.1)
}

### MODIFICATIONS -> {AvgCorr,MaxCorr,TimeElapsed} 
### SUMMATION


tmp <- d
tmp <- d[d[,10]==NA,]
head(d[,])
# dim(d)
tmp <- d[ ,c(2,3,4,5,6,8,9,11)]

for(finval in c(6,7,8)){
  print(paste("===================",names(tmp)[finval],"==================="))
#   finval=7
  lst <-list()
  iter=0
  for(i in mod_le){
#     i=mod_le[1]
    iter=iter+1;
    ### produce line of given MOD
    i_mod = tmp[tmp$mod==i,]
    # Graph trucks with red dashed line and square points
    lst[[iter]]= data.frame(name=i,avg=sum(i_mod[,finval])/nrow(i_mod)
                            )
    #print(paste(i,": ",sum(i_mod[,finval])/nrow(i_mod),sep=""))
  }
  final = do.call("rbind", lst)
  final <- final[order(final$avg,decreasing=TRUE),]
  print(final)
  print(paste("Difference:",final[1,2]-final[length(mod_le),2]))
}