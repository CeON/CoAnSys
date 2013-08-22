########################################################################################################
### tips get from: http://stackoverflow.com/questions/1142294/how-do-i-plot-a-classification-graph-of-a-svm-in-r
### tutorial from: http://planatscher.net/svmtut/svmtut.html
### help for plot.svm: http://rss.acs.unt.edu/Rdoc/library/e1071/html/plot.svm.html
### tutorial for interesting plots: http://www.harding.edu/fmccown/r/#autosdatafile
### learning hash tables in R: http://userprimary.net/posts/2010/05/29/rdict-skip-list-hash-table-for-R/
########################################################################################################

if (! "e1071" %in% row.names(installed.packages()))
  install.packages('e1071')
library('e1071')

if (! "ggplot2" %in% row.names(installed.packages()))
  install.packages('')
  require(ggplot2)

# 2) Task: not-linear-separable data
#Prepare the trainingset (data,class).
data2 <- seq(1,10)
classes2 <- c('b','b','b','a','a','a','a','b','b','b')
fake2 <- c(0,rep(1,9))

#plot f-tion will not gonna work
#plot(classes2)
qplot(classes2, data2)

d <- data.frame(val=data2,fake=fake2,class=classes2)

#####################################################
################### linear kernel ###################
#####################################################
model2 <- svm(class~val+fake, data=d,type='C',kernel='linear')

#Check if the model is able to classify your training data. If not: Why not?
predict(model2,data2)
table(predict(model2,data2), classes2)
plot(model2,d)

# Other kernels
# polynomial:gamma,coef0,degree   (gamma*u'*v + coef0)^degree
# radial basis:gamma              exp(-gamma*|u-v|^2)
# sigmoid:gamma, coef0            tanh(gamma*u'*v + coef0)

?svm
################# polynomial kernel #################
paste("all=10")

m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=2)
m
plot(m,d)
cm = table(predict(m,d[,-3]), classes2)
tp = cm[1,1]+cm[2,2]
print(tp)

for(gam in 1/seq(2,10,by=2)){
    print(gam)
    print(table(predict(m,d[,-3]), classes2))
    m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=2, gamma=gam)
    plot(m,d,main=paste(gam))
    cm = table(predict(m,d[,-3]), classes2)
    tp = cm[1,1]+cm[2,2]
    print(tp)
}

for(coe in c(1/seq(1,100,by=2),0)){
    print(coe)
 #   print(table(predict(m,d[,-3]), classes2))
    m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=2, coef0=coe)
#    plot(m,d,main=paste(gam))
    cm = table(predict(m,d[,-3]), classes2)
    tp = cm[1,1]+cm[2,2]
    print(tp)
}

tmp <- c()
for(coe in c(1/seq(1,100,by=2),0)){  
  for(gam in 1/seq(2,10,by=2)){
    for(deg in 1:7){
      
      m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=deg, gamma=gam, coef0=coe)
      #plot(m,d)
      #paste(predict(m,d[,-3]))
      cm = table(predict(m,d[,-3]), classes2)
      tp = cm[1,1]+cm[2,2]
      tmp <- c(tmp,coe,gam,deg,tp)
    }
  }
}


  for(gam in 1/seq(2,10,by=2)){
    for(deg in 1:7){
            
      m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=deg, gamma=gam
               #, coef0=0
               )
      #plot(m,d)
      #paste(predict(m,d[,-3]))
      cm = table(predict(m,d[,-3]), classes2)
      tp = cm[1,1]+cm[2,2]
      tmp <- c(tmp,coe,gam,deg,tp)
    }
  }


for(coe in c(1/seq(1,100,by=2),0)){  
  
    for(deg in 1:7){

      m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=deg, 
               #gamma=1/1, 
               coef0=coe)
      #plot(m,d)
      #paste(predict(m,d[,-3]))
      cm = table(predict(m,d[,-3]), classes2)
      tp = cm[1,1]+cm[2,2]
      tmp <- c(tmp,coe,gam,deg,tp)
    }
  
}

for(coe in c(1/seq(1,100,by=2),0)){  
  for(gam in 1/seq(2,10,by=2)){
    
      m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',
               #degree=deg, 
               gamma=gam, coef0=coe)
      #plot(m,d)
      #paste(predict(m,d[,-3]))
      cm = table(predict(m,d[,-3]), classes2)
      tp = cm[1,1]+cm[2,2]
      tmp <- c(tmp,coe,gam,deg,tp)
    
  }
}

dim(tmp) <- c(nrow(tmp)/4,4)
colnames(tmp) <- tmp <- c("coe","gam","deg","tp")



m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=2)
plot(m,d)
predict(m,d[,-3])
table(predict(m,d[,-3]), classes2)

m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=3)
plot(m,d)
predict(m,d[,-3])
table(predict(m,d[,-3]), classes2)

m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=5)
plot(m,d)
predict(m,d[,-3])
table(predict(m,d[,-3]), classes2)

m <- svm(class~val+fake, data=d,type='C',kernel='polynomial',degree=7)
plot(m,d)
predict(m,d[,-3])
table(predict(m,d[,-3]), classes2)



################### radial kernel ###################

#radial f-tion overflooded "a"-choices when too big===when more then 1

for(gam in 1/seq(1,100,by=10)){
  m <- svm(class~val+fake, data=d,type='C',kernel='radial', gamma=gam)
  predict(m,d[,-3])
  table(predict(m,d[,-3]), classes2)
  plot(m,d)
}

################### sigmoid kernel ##################
m <- svm(class~val+fake, data=d,type='C',kernel='sigmoid')
predict(m,d[,-3])
table(predict(m,d[,-3]), classes2)
plot(m,d)


# Radialny i wykladniczy wygrały!
# 1. Jak dowieźć tego w zautomatyzowany sposób?
# 2. Jak zmiana parametrów pływa na wynikową płaszczyznę?
