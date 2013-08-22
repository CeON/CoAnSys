library(e1071)



data1 <- seq(1,10,by=2)
classes1 <- c('a','a','a','b','b')
dacla <- rbind(data1,classes1)
daclat <- as.table(t(dacla))
colnames(daclat) <- c("val","class")
daclat


cats
m <- svm(Sex~., data = cats)
plot(m, cats)

dacladf <- as.data.frame(t(rbind(1:5,dacla)))
colnames(dacladf) <- c("fake","val","class")
md <- svm(class~., data = dacladf)
plot(md, dacladf)



test1 <- seq(1,10,by=2) + 1

help(svm)
model1 <- svm(data1,classes1,type='C',kernel='linear')
print(model1)


model <- svm(class ~ . ,  x=data1,y=classes1,type='C',kernel='linear')


# There are two types of classification: 
# * C-classification
# * nu-classification
#
# There are for


print(model1)
summary(model1)

predict(model1,data1)
pred <- fitted(model1)
table(pred, classes1)

predict(model1,test1)

help(predict)

dacla <- rbind(data1,classes1)
class(dacla)
daclat <- as.table(t(dacla))
colnames(daclat) <- c("val","class")
colnames(daclat)
rownames(daclat)
